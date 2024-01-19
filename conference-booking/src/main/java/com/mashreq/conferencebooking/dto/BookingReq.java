package com.mashreq.conferencebooking.dto;

import com.mashreq.conferencebooking.config.validator.MaxRoomSize;
import jakarta.validation.constraints.Min;
import lombok.*;


import java.time.LocalTime;

@Data
public class BookingReq extends AvailabilityReq {

//    @JsonView(value ={View.Post.class})
    @Min(value = 2, message = "No of people should be more than 1")
    @MaxRoomSize
    private int capacity;

    @Builder(builderMethodName = "BookingReqBuilder")
    public BookingReq(LocalTime startTime, LocalTime endTime, int capacity) {
        super(startTime, endTime);
        this.capacity = capacity;
    }



}
