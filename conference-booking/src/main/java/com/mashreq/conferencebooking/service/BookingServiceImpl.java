package com.mashreq.conferencebooking.service;

import com.mashreq.conferencebooking.common.enums.BookingStatus;
import com.mashreq.conferencebooking.db.entity.MaintenanceTime;
import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import com.mashreq.conferencebooking.db.entity.RoomDetails;
import com.mashreq.conferencebooking.db.repository.MaintenanceRepository;
import com.mashreq.conferencebooking.db.repository.RoomRepository;
import com.mashreq.conferencebooking.db.repository.StatusRepository;
import com.mashreq.conferencebooking.dto.*;
import com.mashreq.conferencebooking.exceptions.InvaildBookingDateTimeException;
import com.mashreq.conferencebooking.exceptions.RecordNotFoundException;
import com.mashreq.conferencebooking.exceptions.RoomNotAvailableException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private RoomRepository roomRepository;
    private MaintenanceRepository maintenanceRepository;
    private StatusRepository statusRepository;

    @Override
    public List<Room> getAllRoomStatus() {
        log.info("---- getAllRoomStatus----");
        return roomRepository.findAllRoomStatus();
    }

    @Override
    public BookingRes bookRoom(BookingReq bookingReq) {
        log.info("----start bookRoom----");
        checkTimeValidity(bookingReq);
        var start = bookingReq.getStartTime();
        var end = bookingReq.getEndTime();

        var res = new BookingRes();
        res.setStartTime(start);
        res.setEndTime(end);

        Optional<RoomDetails> roomDetailsOptional = roomRepository.getBookedRoom(LocalDate.now(), start, end, bookingReq.getCapacity());
        if (roomDetailsOptional.isPresent()) {
            RoomDetails roomDetails = roomDetailsOptional.get();
            Optional<RoomBookingStatus> roomStatusOptional = statusRepository.findByRoomDetails(roomDetails);
            RoomBookingStatus roomBookingStatus = new RoomBookingStatus();
            if (roomStatusOptional.isPresent()) {
                roomBookingStatus = roomStatusOptional.get();
            }
            saveBookingStatus(roomBookingStatus, roomDetails, start, end);
            res.setRoom(List.of(new Room(roomDetails.getRoomName(), roomDetails.getCapacity(), null)));
            res.setBookingDate(LocalDate.now());
        } else {
            log.error("No rooms returned");
            throw new RoomNotAvailableException("No rooms are vacant for booking!");
        }
        log.info("----end bookRoom----");
        return res;
    }

    @Override
    public BookingRes getAvailableRooms(LocalTime startTime, LocalTime endTime) {
        log.info("----start getAvailableRooms----");
        checkTimeValidity(BookingReq.BookingReqBuilder().startTime(startTime).endTime(endTime).build());
        Predicate<RoomBookingStatus> bookedSlotOverlapPredicate = t -> t.getBookedStartTime().equals(startTime)
                || t.getBookedEndTime().equals(endTime)
                || (startTime.isAfter(t.getBookedStartTime()) && startTime.isBefore(t.getBookedEndTime()))
                || (endTime.isAfter(t.getBookedStartTime()) && endTime.isBefore(t.getBookedEndTime()));

        List<RoomBookingStatus> roomBookingStatusList = statusRepository.getRoomBookingtatusForCurrentDate(LocalDate.now());
        List<Integer> roomIdList = roomBookingStatusList.stream().filter(bookedSlotOverlapPredicate).map(RoomBookingStatus::getRoomDetails).map(RoomDetails::getRoomId).toList();
        List<RoomDetails> roomDetailsList = roomIdList.isEmpty() ? roomRepository.findAll() : roomRepository.findByRoomIdNotIn(roomIdList);

        BookingRes res = new BookingRes();
        res.setRoom(roomDetailsList.stream().map(r -> new Room(r.getRoomName(), r.getCapacity(), null)).toList());
        log.info("----end getAvailableRooms----");
        return res;
    }

    private void checkTimeValidity(AvailabilityReq bookingReq) {
        log.info("----start checkTimeValidity----");
        if (bookingReq.getStartTime().isBefore(LocalTime.now())) {
            log.error("Start time before current time");
            throw new InvaildBookingDateTimeException("Booking can be done only for today's date and future time");
        }

        Predicate<MaintenanceTime> maintenanceSlotOverlapPredicate = t -> t.getStartTime().equals(bookingReq.getStartTime())
                || t.getEndTime().equals(bookingReq.getEndTime())
                || (bookingReq.getStartTime().isAfter(t.getStartTime()) && bookingReq.getStartTime().isBefore(t.getEndTime()))
                || (bookingReq.getEndTime().isAfter(t.getStartTime()) && bookingReq.getEndTime().isBefore(t.getEndTime()));

        List<MaintenanceTime> maintenanceTimes = getMaintenanceSlots();
        Optional<MaintenanceTime> maintenanceTimeOptional = maintenanceTimes.stream().filter(maintenanceSlotOverlapPredicate).findFirst();
        if (maintenanceTimeOptional.isPresent()) {
            log.error("Booking time and maintenance time overlap");
            throw new InvaildBookingDateTimeException("Booking time overlaps with the Maintenance timing: "
                    + maintenanceTimeOptional.get().getStartTime()
                    + " -" + maintenanceTimeOptional.get().getEndTime() + " Please choose a different time range");
        }
        log.info("----end checkTimeValidity----");
    }

    @Override
    public BookingRes cancelBooking(CancelReq cancelReq) {
        log.info("----cancelBooking----");
        if (statusRepository.updateBookingStatus(LocalDate.now(), cancelReq.startTime(),
                cancelReq.endTime(), cancelReq.roomName()) == 1) {
            var bookingres = new BookingRes();
            BeanUtils.copyProperties(cancelReq, bookingres);
            bookingres.setBookingDate(LocalDate.now());
            bookingres.setRoom(List.of(new Room(cancelReq.roomName(), null, BookingStatus.CANCELLED)));
            return bookingres;
        } else {
            log.error("No Record Found");
            throw new RecordNotFoundException("No booking found for Cancellation");
        }
    }

    private void saveBookingStatus(RoomBookingStatus roomBookingStatus, RoomDetails roomDetails,
                                   LocalTime start, LocalTime end) {
        log.info("----saveBookingStatus start-----");
        roomBookingStatus.setRoomDetails(roomDetails);
        roomBookingStatus.setBookedStartTime(start);
        roomBookingStatus.setBookedEndTime(end);
        roomBookingStatus.setStatus(BookingStatus.BOOKED);
        statusRepository.save(roomBookingStatus);
        log.info("----saveBookingStatus end-----");
    }

    @Cacheable("maintenance")
    private List<MaintenanceTime> getMaintenanceSlots() {
        return maintenanceRepository.findAll();
    }


}
