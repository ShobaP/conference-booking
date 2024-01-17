package com.mashreq.conferencebooking;

import com.mashreq.conferencebooking.db.entity.RoomDetails;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
//@CustomLog
@RequestMapping("/api/v1")
public class BookingController {

    private BookingService bookingService;

    @GetMapping("/available-rooms")
    public List<RoomDetails> getAvailableRooms() {
        return bookingService.getAvailableRooms();
    }

    @PostMapping("/book")
    public BookingRes bookRoom(@Valid @RequestBody BookingReq bookingReq) {
        BookingRes response = bookingService.bookRoom(bookingReq);
        return response;
    }

}
