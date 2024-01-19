package com.mashreq.conferencebooking.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="ROOM_DETAILS")
public class RoomDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROOM_ID")
	private int roomId;
	
	@Column(name = "ROOM_NAME")
	private String roomName;
	
	@Column(name = "CAPACITY")
	private int capacity;

	@OneToOne(fetch = FetchType.EAGER)
	private RoomBookingStatus roomStatus;

}
