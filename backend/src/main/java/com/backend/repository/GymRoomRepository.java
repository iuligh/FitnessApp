package com.backend.repository;

import com.backend.model.GymRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymRoomRepository extends JpaRepository<GymRoom, Long> { }