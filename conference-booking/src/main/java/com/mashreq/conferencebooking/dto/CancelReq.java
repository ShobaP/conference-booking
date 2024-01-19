package com.mashreq.conferencebooking.dto;

import lombok.Data;

import java.time.LocalTime;

public record CancelReq(String roomName, LocalTime startTime, LocalTime endTime) {}
