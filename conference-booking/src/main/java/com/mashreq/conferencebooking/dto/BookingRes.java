package com.mashreq.conferencebooking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mashreq.conferencebooking.dto.Room;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRes implements Serializable {

    //serialVersionId
    private List<Room> room;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

}
