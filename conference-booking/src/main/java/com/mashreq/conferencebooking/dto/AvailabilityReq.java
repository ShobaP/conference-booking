package com.mashreq.conferencebooking.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mashreq.conferencebooking.model.View;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AvailabilityReq implements Serializable {

    @NotNull(message = "Mandatory parameter startTime is missing")
//    @JsonView(value ={View.Get.class, View.Post.class})
    private LocalTime startTime;

    @NotNull(message = "Mandatory parameter endTime is missing")
//    @JsonView(value ={View.Get.class, View.Post.class})
    private LocalTime endTime;

}
