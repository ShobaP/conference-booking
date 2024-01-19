package com.mashreq.conferencebooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mashreq.conferencebooking.common.enums.BookingStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Room(String roomName, Integer capacity, BookingStatus status) {}
