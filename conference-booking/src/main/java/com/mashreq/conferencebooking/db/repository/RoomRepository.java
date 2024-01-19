package com.mashreq.conferencebooking.db.repository;

import com.mashreq.conferencebooking.db.entity.RoomBookingStatus;
import com.mashreq.conferencebooking.db.entity.RoomDetails;
import com.mashreq.conferencebooking.dto.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomDetails, Integer> {

    @Query(value = "SELECT top 1 * FROM Room_Details where room_Id not in (select room_Id from ROOM_BOOKING_STATUS where " +
            "BOOKED_DATE = :currentDate and BOOKED_START_TIME = :startTime and booked_End_Time = :endTime AND status = 'BOOKED') " +
            "and capacity >=:capacity order by capacity", nativeQuery = true)
    Optional<RoomDetails> getBookedRoom(@Param("currentDate") LocalDate currentDate, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime, @Param("capacity") int capacity);

    @Query(value = "SELECT r.roomName as room_name, r.capacity as capacity FROM Room_Details r where room_Id not in (select room_Id from ROOM_BOOKING_STATUS where " +
            "BOOKED_START_TIME = :startTime and booked_End_Time = :endTime AND status = 'BOOKED') and capacity >=:capacity order by capacity", nativeQuery = true)
    List<Room> getVacantRooms(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<RoomDetails> findByRoomIdNotIn(List<Integer> roomIdList);

    @Query("SELECT new com.mashreq.conferencebooking.dto.Room(r.roomName, r.capacity, s.status) from RoomDetails r left join RoomBookingStatus s on r.roomId = s.roomDetails.roomId")
    List<Room> findAllRoomStatus();

}
