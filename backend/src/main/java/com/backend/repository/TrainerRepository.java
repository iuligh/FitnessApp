package com.backend.repository;

import com.backend.model.GymRoom;
import com.backend.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}