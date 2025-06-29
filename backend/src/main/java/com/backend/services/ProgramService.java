package com.backend.services;

import com.backend.EventDTO;
import com.backend.enums.EWeekDays;
import com.backend.model.Program;
import com.backend.model.Event;
import com.backend.model.User;
import com.backend.repository.ProgramRepository;
import com.backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgramService {
    @Autowired
    private final ProgramRepository programRepo;
    private final EventRepository eventRepo;

    public ProgramService(ProgramRepository programRepo, EventRepository eventRepo) {
        this.programRepo = programRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Returnează toate evenimentele pentru userId la o anumită zi:
     * - la care userul s-a înscris
     * - pe care le ține ca trainer
     */
    public List<EventDTO> getProgramForUser(Long userId, EWeekDays day) {
        // 1) Evenimente la care s-a înscris
        List<Event> subscribed = programRepo.findAllByUserId(userId)
                .stream()
                .map(Program::getEvent)
                .filter(e -> e.getEventDays()
                        .stream()
                        .anyMatch(d -> d.getDay() == day))
                .collect(Collectors.toList());

        // 2) Evenimente ținute de trainer
        List<Event> trainerEvents = eventRepo.findByTrainerIdAndDay(userId, day);

        // 3) Le combinăm, eliminăm duplicatele
        Set<Event> all = new LinkedHashSet<>();
        all.addAll(subscribed);
        all.addAll(trainerEvents);

        // 4) Mapăm la DTO
        return all.stream()
                .map(EventDTO::fromEntity)  // presupunem că ai un factory în DTO
                .collect(Collectors.toList());
    }

    public void subscribe(Long userId, Integer eventId) {
        if (programRepo.existsByUserIdAndEventId(userId, eventId))
            throw new IllegalStateException("Already subscribed");
        Program p = new Program();
        p.setUser(new User(userId));
        p.setEvent(new Event(eventId));
        programRepo.save(p);
    }
}
