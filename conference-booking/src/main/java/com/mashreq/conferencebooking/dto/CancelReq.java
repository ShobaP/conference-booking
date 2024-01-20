package com.mashreq.conferencebooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

public record CancelReq(String roomName, @Schema(type = "String", pattern = "HH:mm")LocalTime startTime,
                        @Schema(type = "String", pattern = "HH:mm")LocalTime endTime) {}
