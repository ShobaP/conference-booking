package com.mashreq.conferencebooking.db.repository;

import com.mashreq.conferencebooking.db.entity.MaintenanceTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<MaintenanceTime, Integer> {
}
