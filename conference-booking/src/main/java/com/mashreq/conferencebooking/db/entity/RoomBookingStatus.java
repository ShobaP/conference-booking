package com.mashreq.conferencebooking.db.entity;

import com.mashreq.conferencebooking.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name ="ROOM_BOOKING_STATUS")
public class RoomBookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROOM_ID", referencedColumnName = "room_id")
    private RoomDetails roomDetails;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(name = "BOOKED_START_TIME")
    private LocalTime bookedStartTime;

    @Column(name = "BOOKED_END_TIME")
    private LocalTime bookedEndTime;

    @Column(name = "BOOKED_DATE")
    private LocalDate bookingDate;
    
    @PrePersist
    public void createBookingDate() {
        this.bookingDate = LocalDate.now();
    }

    @PreUpdate
    public void updateBookingDate() {
        this.bookingDate = LocalDate.now();
    }

}
