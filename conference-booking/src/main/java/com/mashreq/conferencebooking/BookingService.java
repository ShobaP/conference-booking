package com.mashreq.conferencebooking;

import com.mashreq.conferencebooking.common.enums.BookingStatus;
import com.mashreq.conferencebooking.db.entity.MaintenanceTime;
import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import com.mashreq.conferencebooking.db.entity.RoomDetails;
import com.mashreq.conferencebooking.db.repository.MaintenanceRepository;
import com.mashreq.conferencebooking.db.repository.RoomRepository;
import com.mashreq.conferencebooking.db.repository.StatusRepository;
import com.mashreq.conferencebooking.dto.Room;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
@AllArgsConstructor
@Service
public class BookingService {

    private RoomRepository roomRepository;
    private MaintenanceRepository maintenanceRepository;
    private StatusRepository statusRepository;

    public List<RoomDetails> getAvailableRooms() {
        return roomRepository.findAll();
    }

    public BookingRes bookRoom(BookingReq bookingReq) {
        BookingRes res = new BookingRes();
        //if (bookingReq.bookingReq.getStartTime().isBefore(bookingReq.getEndTime())) {
        if(bookingReq.getStartTime().isAfter(LocalTime.of(23,44))) {

        }
            List<MaintenanceTime> maintenanceTimes = maintenanceRepository.findAll();
            boolean notMaintenanceSlot =
                    maintenanceTimes.stream().
                            noneMatch(t ->  t.getStartTime().equals(bookingReq.getStartTime())
                                    || (t.getStartTime().isBefore(bookingReq.getStartTime())
                                    && t.getEndTime().isAfter(bookingReq.getStartTime())));
            if (notMaintenanceSlot) {
                LocalDateTime start = LocalDate.now().atTime(bookingReq.getStartTime());
                LocalDateTime end = LocalDate.now().atTime(bookingReq.getStartTime().plusMinutes(15));

                if (bookingReq.getCapacity() == 0) { //check time is after current time
                    res.setRoom(roomRepository.getVacantRooms(start, end));
                } else {
                    Optional<RoomDetails> roomDetailsOptional =roomRepository.getBookedRoom(start, end, bookingReq.getCapacity());
                    if(roomDetailsOptional.isPresent()) {
                        RoomDetails roomDetails = roomDetailsOptional.get();
                        Optional<RoomBookingStatus> roomStatusOptional = statusRepository.findByRoomId(roomDetails.getRoomId());
                        RoomBookingStatus roomBookingStatus = new RoomBookingStatus();
                        if(roomStatusOptional.isPresent()) {
                            roomBookingStatus = roomStatusOptional.get();
                        }
                        roomBookingStatus.setRoomId(roomDetails.getRoomId());
                        roomBookingStatus.setBookedStartTime(start);
                        roomBookingStatus.setBookedEndTime(end);
                        roomBookingStatus.setStatus(BookingStatus.BOOKED);
                        statusRepository.save(roomBookingStatus);

                        res.setRoom(List.of(new Room(roomDetails.getRoomName(), roomDetails.getCapacity())));
                        res.setStartTime(start);
                        res.setEndTime(end);
                    } else {
                        res.setStartTime(start);
                        res.setEndTime(end); ;
                    }

                }
            }

        return res;
    }

}





//    || (bookingReq.getStartTime().isAfter(t.getStartTime()) && bookingReq.getStartTime().isBefore(t.getEndTime())
//                || bookingReq.getEndTime().equals(t.getStartTime())
//                || (bookingReq.getEndTime().isAfter(t.getStartTime()) && bookingReq.getEndTime().isBefore(t.getEndTime()))}
