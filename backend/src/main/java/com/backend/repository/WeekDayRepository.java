package com.backend.repository;

import com.backend.model.EventDayId;
import com.backend.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay, EventDayId> {
    void deleteByEventId(Integer eventId);
}
