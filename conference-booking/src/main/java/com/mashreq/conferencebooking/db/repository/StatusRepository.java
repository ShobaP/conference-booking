package com.mashreq.conferencebooking.db.repository;

import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<RoomBookingStatus, Integer> {
    Optional<RoomBookingStatus> findByRoomId(int roomId);


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
