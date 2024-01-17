package com.mashreq.conferencebooking;

import com.mashreq.conferencebooking.dto.Room;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingRes implements Serializable {

    //serialVersionId
    private List<Room> room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
