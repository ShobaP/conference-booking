package com.mashreq.conferencebooking.db.entity;



import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="MAINTENANCE_TIME")
public class MaintenanceTime {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private int id;
	
	@Column(name = "START_TIME")
	private LocalTime startTime;

	@Column(name = "END_TIME")
	private LocalTime endTime;
	
}
