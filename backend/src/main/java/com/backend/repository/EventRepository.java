package com.backend.repository;

import com.backend.enums.EWeekDays;
import com.backend.model.Event;
import com.backend.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitleContaining(String title);

    @Query("SELECT e FROM Event e JOIN e.eventDays d WHERE d.id.day = :day")
    List<Event> findByDay(EWeekDays day);
    List<Event> findByRoomId(Long roomId);
    @Query("""
      select e from Event e 
      join e.eventDays d 
      where e.trainer.userAccount.id = :trainerId 
        and d.id.day = :day
    """)
    List<Event> findByTrainerIdAndDay(
            @Param("trainerId") Long trainerId,
            @Param("day") EWeekDays day);

}
