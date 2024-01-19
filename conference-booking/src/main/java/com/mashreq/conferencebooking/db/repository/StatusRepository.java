package com.mashreq.conferencebooking.db.repository;

import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import com.mashreq.conferencebooking.db.entity.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<RoomBookingStatus, Integer> {
    Optional<RoomBookingStatus> findByRoomDetails(RoomDetails roomId);

    @Query("SELECT r from RoomBookingStatus r WHERE bookingDate = :currentDate and status = 'BOOKED'")
    List<RoomBookingStatus> getRoomBookingtatusForCurrentDate(LocalDate currentDate);

    @Transactional
    @Modifying
    @Query("UPDATE RoomBookingStatus set status = 'CANCELLED' where bookedStartTime = :startTime and bookedEndTime = :endTime " +
            "and bookingDate = :currentDate and roomDetails.roomId = (SELECT roomId FROM RoomDetails where roomName = :roomName)")
    int updateBookingStatus(@Param("currentDate") LocalDate currentDate,
                            @Param("startTime") LocalTime startTime,
                            @Param("endTime") LocalTime endTime,@Param("roomName") String roomName);

//    @Query("SELECT * FROM RoomDetails WHERE roomId NOT IN (" +
//            "SELECT roomId FROM RoomStatus WHERE bookedStartTime = :startTime AND " +
//            "bookedEndTime = :endTime) order by capacity")
//    List<RoomDetails> getBookedRoom(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

//    select room_name, capcity FROM room_details WHERE room_id NOT IN
//            (select room_id FROM room_status WHERE
//                    start_time ='2024-01-14 15:00:00'
//                    and end_time =  '2024-01-14 15:15:00') order BY capcity
//
//    select top 1 room_name FROM room_details WHERE room_id NOT IN
//            (select room_id FROM room_status WHERE
//                    start_time ='2024-01-14 15:00:00'
//                    and end_time =  '2024-01-14 15:15:00') and capcity >= 5
//    ORDER by capcity
}
