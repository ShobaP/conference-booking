package com.mashreq.conferencebooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRes implements Serializable {

    //serialVerssionId
    private List<Room> room;
    private LocalDate bookingDate;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime startTime;
    @Schema(type = "String", pattern = "HH:mm:SS")
    private LocalTime endTime;

}
