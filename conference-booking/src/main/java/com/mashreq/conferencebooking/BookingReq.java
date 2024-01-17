package com.mashreq.conferencebooking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class BookingReq implements Serializable {

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    private LocalTime endTime;

    @Pattern(regexp="[2-9]|([1-9][0-9]+)",message="No of people should be more than 1")
    private int capacity;

}
