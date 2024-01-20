package com.mashreq.conferencebooking.service;

import com.mashreq.conferencebooking.common.enums.BookingStatus;
import com.mashreq.conferencebooking.db.entity.MaintenanceTime;
import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import com.mashreq.conferencebooking.db.entity.RoomDetails;
import com.mashreq.conferencebooking.db.repository.MaintenanceRepository;
import com.mashreq.conferencebooking.db.repository.RoomRepository;
import com.mashreq.conferencebooking.db.repository.StatusRepository;
import com.mashreq.conferencebooking.dto.BookingReq;
import com.mashreq.conferencebooking.dto.BookingRes;
import com.mashreq.conferencebooking.dto.CancelReq;
import com.mashreq.conferencebooking.exceptions.InvaildBookingDateTimeException;
import com.mashreq.conferencebooking.exceptions.RecordNotFoundException;
import com.mashreq.conferencebooking.exceptions.RoomNotAvailableException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceImplTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private MaintenanceRepository maintenanceRepository;
    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;
    @Test
    public void testBookRoom() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 00))
                .capacity(2).build();
        var roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomDetails.setRoomName("26.01");
        Optional<RoomDetails> roomDetailsOptional = Optional.of(roomDetails);
        when(roomRepository.getBookedRoom(any(), any(), any(), anyInt())).thenReturn(roomDetailsOptional);

        var maintenanceTimes = new ArrayList<MaintenanceTime>();
        MaintenanceTime maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(9,00));
        maintenanceTime.setEndTime(LocalTime.of(9,15));
        maintenanceTimes.add(maintenanceTime);

        maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(13,00));
        maintenanceTime.setEndTime(LocalTime.of(13,15));
        maintenanceTimes.add(maintenanceTime);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceTimes);
        BookingRes response = bookingServiceImpl.bookRoom(bookingReq);
        assertEquals(1, response.getRoom().size());
        assertEquals("26.01", response.getRoom().get(0).roomName());
    }

    @Test(expected = RoomNotAvailableException.class)
    public void testBookRoomNoRoomsAvailable() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 00))
                .capacity(2).build();

        when(roomRepository.getBookedRoom(any(), any(), any(), anyInt())).thenReturn(Optional.empty());

        var maintenanceTimes = new ArrayList<MaintenanceTime>();
        MaintenanceTime maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(9,00));
        maintenanceTime.setEndTime(LocalTime.of(9,15));
        maintenanceTimes.add(maintenanceTime);

        maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(13,00));
        maintenanceTime.setEndTime(LocalTime.of(13,15));
        maintenanceTimes.add(maintenanceTime);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceTimes);
        BookingRes response = bookingServiceImpl.bookRoom(bookingReq);
        assertEquals(1, response.getRoom().size());
        assertEquals("26.01", response.getRoom().get(0).roomName());
    }

    @Test(expected = InvaildBookingDateTimeException.class)
    public void testBookRoomInMaintenanceSameStartTime() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 00))
                .capacity(2).build();


        var maintenanceTimes = new ArrayList<MaintenanceTime>();
        MaintenanceTime maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()),00));
        maintenanceTime.setEndTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()),15));
        maintenanceTimes.add(maintenanceTime);

        maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(13,00));
        maintenanceTime.setEndTime(LocalTime.of(13,15));
        maintenanceTimes.add(maintenanceTime);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceTimes);
        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test(expected = InvaildBookingDateTimeException.class)
    public void testBookRoomInMaintenanceSameEndTime() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).plusMinutes(30).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 15))
                .capacity(2).build();

        var maintenanceTimes = new ArrayList<MaintenanceTime>();
        MaintenanceTime maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()),00));
        maintenanceTime.setEndTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()),15));
        maintenanceTimes.add(maintenanceTime);

        maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(13,00));
        maintenanceTime.setEndTime(LocalTime.of(13,15));
        maintenanceTimes.add(maintenanceTime);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceTimes);

        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test(expected = InvaildBookingDateTimeException.class)
    public void testBookRoomInMaintenanceEndTimeBetweenSlot() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(1).getHour()), 26))
                .capacity(2).build();


        var maintenanceTimes = new ArrayList<MaintenanceTime>();
        MaintenanceTime maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of((LocalTime.now().plusHours(1).plusMinutes(15).getHour()),00));
        maintenanceTime.setEndTime(LocalTime.of((LocalTime.now().plusHours(1).plusMinutes(30).getHour()),15));
        maintenanceTimes.add(maintenanceTime);

        maintenanceTime = new MaintenanceTime();
        maintenanceTime.setStartTime(LocalTime.of(13,00));
        maintenanceTime.setEndTime(LocalTime.of(13,15));
        maintenanceTimes.add(maintenanceTime);

        when(maintenanceRepository.findAll()).thenReturn(maintenanceTimes);
        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test(expected = RoomNotAvailableException.class)
    public void testBookRoomNoAvaialbleRooms() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().plusHours(1).plusMinutes(30).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 15))
                .capacity(2).build();
        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test(expected = InvaildBookingDateTimeException.class)
    public void testBookRoomInMaintenancePastTime() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of((LocalTime.now().minusHours(1).getHour()), 00))
                .endTime(LocalTime.of((LocalTime.now().plusHours(2).getHour()), 00))
                .capacity(2).build();

        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test(expected = InvaildBookingDateTimeException.class)
    public void testBookRoomInMaintenanceMidnight() {
        BookingReq bookingReq = BookingReq.BookingReqBuilder()
                .startTime(LocalTime.of(00,00))
                .endTime(LocalTime.of(00, 15))
                .capacity(2).build();

        bookingServiceImpl.bookRoom(bookingReq);
    }

    @Test
    public void testGetAvailableRooms() {
        var roomBookingStatus = new RoomBookingStatus();
        roomBookingStatus.setBookedStartTime(LocalTime.of(23, 00));
        roomBookingStatus.setBookedEndTime(LocalTime.of(23, 15));
        var roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomBookingStatus.setRoomDetails(roomDetails);
        when(statusRepository.getRoomBookingtatusForCurrentDate(any())).thenReturn(Arrays.asList(roomBookingStatus));
        List<RoomDetails> roomDetailsList = new ArrayList<>();
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomDetails.setRoomName("26.01");
        roomDetailsList.add(roomDetails);
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(2);
        roomDetails.setRoomName("26.02");
        roomDetailsList.add(roomDetails);
        when(roomRepository.findByRoomIdNotIn(any())).thenReturn(Arrays.asList(new RoomDetails()));

        var response = bookingServiceImpl.getAvailableRooms(LocalTime.of(23,00), LocalTime.of(00, 00));
        assertEquals(1, response.getRoom().size());
    }

    @Test
    public void testGetAvailableRoomsWithoutOverlap() {
        var roomBookingStatus = new RoomBookingStatus();
        roomBookingStatus.setBookedStartTime(LocalTime.of(LocalTime.now().getHour(), 00));
        roomBookingStatus.setBookedEndTime(LocalTime.of(LocalTime.now().plusMinutes(30).getHour(), 00));
        var roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomBookingStatus.setRoomDetails(roomDetails);
        when(statusRepository.getRoomBookingtatusForCurrentDate(any())).thenReturn(Arrays.asList(roomBookingStatus));
        List<RoomDetails> roomDetailsList = new ArrayList<>();
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomDetails.setRoomName("26.01");
        roomDetailsList.add(roomDetails);
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(2);
        roomDetails.setRoomName("26.02");
        roomDetailsList.add(roomDetails);
        when(roomRepository.findAll()).thenReturn(roomDetailsList);
        var response = bookingServiceImpl.getAvailableRooms(LocalTime.of(23,00),LocalTime.of(23, 15));
        assertEquals(2, response.getRoom().size());
    }

    @Test
    public void testGetAvailableRoomsWithNoSlots() {
        List<RoomBookingStatus> roomBookingStatusList = new ArrayList<>();
        var roomBookingStatus = new RoomBookingStatus();
        roomBookingStatus.setBookedStartTime(LocalTime.of(23, 00));
        roomBookingStatus.setBookedEndTime(LocalTime.of(23, 00));
        var roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomBookingStatus.setRoomDetails(roomDetails);
        roomBookingStatusList.add(roomBookingStatus);
        roomBookingStatus = new RoomBookingStatus();
        roomBookingStatus.setBookedStartTime(LocalTime.of(23, 15));
        roomBookingStatus.setBookedEndTime(LocalTime.of(23, 30));
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(2);
        roomBookingStatus.setRoomDetails(roomDetails);
        roomBookingStatusList.add(roomBookingStatus);

        when(statusRepository.getRoomBookingtatusForCurrentDate(any())).thenReturn(roomBookingStatusList);
        List<RoomDetails> roomDetailsList = new ArrayList<>();
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(1);
        roomDetails.setRoomName("26.01");
        roomDetailsList.add(roomDetails);
        roomDetails = new RoomDetails();
        roomDetails.setRoomId(2);
        roomDetails.setRoomName("26.02");
        roomDetailsList.add(roomDetails);
        var response = bookingServiceImpl.getAvailableRooms(LocalTime.of(23,00),LocalTime.of(23, 30));
        assertEquals(0, response.getRoom().size());
    }

    @Test
    public void testCancelBooking() {
        when(statusRepository.updateBookingStatus(any(), any(), any(), any())).thenReturn(1);
        var request = new CancelReq("26.01", LocalTime.now(), LocalTime.now());
        BookingRes response = bookingServiceImpl.cancelBooking(request);
        assertEquals(1, response.getRoom().size());
        assertEquals("26.01", response.getRoom().get(0).roomName());
        assertEquals(BookingStatus.CANCELLED, response.getRoom().get(0).status());
    }

    @Test(expected = RecordNotFoundException.class)
    public void testCancelBookingException() {
        when(statusRepository.updateBookingStatus(any(), any(), any(), any())).thenReturn(0);
        var request = new CancelReq("26.01", LocalTime.now(), LocalTime.now());
        BookingRes response = bookingServiceImpl.cancelBooking(request);

   }

}
