package com.mashreq.conferencebooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class AvailabilityReq implements Serializable {

    @NotNull(message = "Mandatory parameter startTime is missing")
    @Schema(type = "String", pattern = "HH:mm")
//    @JsonView(value ={View.Get.class, View.Post.class})
    private LocalTime startTime;

    @NotNull(message = "Mandatory parameter endTime is missing")
    @Schema(type = "String", pattern = "HH:mm")
//    @JsonView(value ={View.Get.class, View.Post.class})
    private LocalTime endTime;

}
