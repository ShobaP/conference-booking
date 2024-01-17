package com.mashreq.conferencebooking.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@JsonPropertyOrder({ "roomName", "capacity"})
@Data
@AllArgsConstructor
public class Room {
    private String roomName;
    private int capacity;

}
