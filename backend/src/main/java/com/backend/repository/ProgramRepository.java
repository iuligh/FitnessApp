package com.backend.repository;

import com.backend.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
  boolean existsByUserIdAndEventId(Long userId, Integer eventId);
  List<Program> findAllByUserId(Long userId);
}
