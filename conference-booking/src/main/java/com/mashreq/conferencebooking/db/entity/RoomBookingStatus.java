package com.mashreq.conferencebooking.db.entity;

import com.mashreq.conferencebooking.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name ="ROOM_BOOKING_STATUS")
public class RoomBookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ROOM_ID")
    private int roomId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "BOOKED_START_TIME")
    private LocalDateTime bookedStartTime;

    @Column(name = "BOOKED_END_TIME")
    private LocalDateTime bookedEndTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "room_id")
    private RoomDetails roomDetails;

    //prepersist

}
