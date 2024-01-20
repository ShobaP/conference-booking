package com.mashreq.conferencebooking.service;

import com.mashreq.conferencebooking.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;


@Service
public interface BookingService {

    public List<Room> getAllRoomStatus();
    public BookingRes bookRoom(BookingReq bookingReq);
    public BookingRes getAvailableRooms(LocalTime startTime, LocalTime endTime);
    public BookingRes cancelBooking(CancelReq cancelReq);
}



