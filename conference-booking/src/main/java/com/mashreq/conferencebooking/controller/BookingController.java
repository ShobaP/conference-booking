package com.mashreq.conferencebooking.controller;

import com.mashreq.conferencebooking.dto.*;
import com.mashreq.conferencebooking.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/room")
public class BookingController {

    private BookingService bookingService;

    @PostMapping("/book")
    public BookingRes bookRoom(@Valid @RequestBody BookingReq bookingReq) {
        log.info("bookRoom start");
        return bookingService.bookRoom(bookingReq);

    }

    @GetMapping("/available")
    public BookingRes getAvailableRooms(@Valid @RequestBody AvailabilityReq availabilityReq) {
        log.info("getAvailableRooms start");
        return bookingService.getAvailableRooms(availabilityReq);
    }

    @PostMapping("/cancel")
    public BookingRes cancelBooking(@Valid @RequestBody CancelReq cancelReq) {
        log.info("cancelBooking start");
        return bookingService.cancelBooking(cancelReq);
    }

    @GetMapping("/status")
    public List<Room> getAllRoomStatus() {
        log.info("getAllRoomStatus start");
        return bookingService.getAllRoomStatus();
    }

}
