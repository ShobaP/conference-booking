package com.mashreq.conferencebooking.controller;

import com.mashreq.conferencebooking.dto.*;
import com.mashreq.conferencebooking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/room")
public class BookingController {

    private BookingService bookingService;

    @Operation(summary="Book a conference room by inputting time range and no. of attendees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success! Conference room booked",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = BookingRes.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "500", description = "Invalid Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))})
    })
    @PostMapping("/book")
    public BookingRes bookRoom(@Valid @RequestBody BookingReq bookingReq) {
        log.info("bookRoom start");
        return bookingService.bookRoom(bookingReq);

    }

    @Operation(summary="List of conference rooms available for given time range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List Retrieved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingRes.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "500", description = "Invalid Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))})
    })
    @GetMapping("/available/list")
    public BookingRes getAvailableRooms(@Schema(type = "LocalTime") @RequestParam(value="start", required = false) LocalTime startTime,
                                        @Schema(type = "LocalTime") @RequestParam(value="end", required = false) LocalTime endTime) {
        log.info("getAvailableRooms start");
        return bookingService.getAvailableRooms(startTime, endTime);
    }

    @Operation(summary="Cancel Booked Room")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking Cancelled",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookingRes.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "500", description = "Invalid Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))})
    })
    @PostMapping("/cancel")
    public BookingRes cancelBooking(@Valid @RequestBody CancelReq cancelReq) {
        log.info("cancelBooking start");
        return bookingService.cancelBooking(cancelReq);
    }

    @Operation(summary="Get the latest status of Conference Rooms")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statuses received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Room.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))}),
            @ApiResponse(responseCode = "500", description = "Invalid Request",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Response.class))})
    })
    @GetMapping("/status")
    public List<Room> getAllRoomStatus() {
        log.info("getAllRoomStatus start");
        return bookingService.getAllRoomStatus();
    }

}
