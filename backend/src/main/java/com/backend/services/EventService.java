package com.backend.services;

import com.backend.DTOS.EventRoomDTO;
import com.backend.EventDTO;
import com.backend.enums.EWeekDays;
import com.backend.model.Event;
import com.backend.model.Trainer;
import com.backend.model.WeekDay;
import com.backend.repository.EventRepository;
import com.backend.repository.TrainerRepository;
import com.backend.repository.WeekDayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private WeekDayRepository weekDayRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    public List<EventDTO> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<EventDTO> getEventsByDay(EWeekDays day) {
        List<Event> events = eventRepository.findByDay(day);
        return events.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public EventDTO getEventById(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        return eventOptional.map(this::convertToDTO).orElse(null);
    }

    public EventDTO createEvent(EventDTO dto) {
        // 1. Încarcă antrenorul
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer nu a fost găsit cu ID " + dto.getTrainerId()));

        // 2. Construiește entitatea Event
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setImageName(dto.getImageName());
        event.setTrainer(trainer);
        event.setStartHour(dto.getStartHour());
        event.setEndHour(dto.getEndHour());

        // 3. Setează zilele de săptămână (EventDay)
        if (dto.getDays() != null) {
            dto.getDays().forEach(dayEnum -> event.addEventDay(dayEnum));
        }

        // 4. Salvează în baza de date
        Event saved = eventRepository.save(event);

        // 5. Returnează DTO-ul populat (conversia la final)
        return convertToDTO(saved);
    }

    public EventDTO updateEvent(Integer eventId, EventDTO dto) {
        Event existing = eventRepository.findById(Long.valueOf(eventId))
                .orElseThrow(() -> new RuntimeException("Event nu există cu ID " + eventId));

        // Încarcă antrenorul nou (dacă a fost trimis alt trainerId)
        Trainer trainer = trainerRepository.findById(dto.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer nu a fost găsit cu ID " + dto.getTrainerId()));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setImageName(dto.getImageName());
        existing.setTrainer(trainer);
        existing.setStartHour(dto.getStartHour());
        existing.setEndHour(dto.getEndHour());

        // Resetează zilele curente și adaugă iar
        existing.getEventDays().clear();
        if (dto.getDays() != null) {
            dto.getDays().forEach(dayEnum -> existing.addEventDay(dayEnum));
        }

        Event updated = eventRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deleteEvent(Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            weekDayRepository.deleteByEventId(event.getId());
            eventRepository.delete(event);
        }
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setImageName(event.getImageName());

        // setăm trainerId și trainerName în DTO
        if (event.getTrainer() != null) {
            dto.setTrainerId(event.getTrainer().getId());
            dto.setTrainerName(event.getTrainer().getName());
        }

        dto.setStartHour(event.getStartHour());
        dto.setEndHour(event.getEndHour());

        // conversia zilelor de tip WeekDay → Enum EWeekDays
        Set<com.backend.enums.EWeekDays> days = event.getEventDays()
                .stream()
                .map(WeekDay::getDay)
                .collect(Collectors.toSet());
        dto.setDays(days);

        return dto;
    }

    public List<EventRoomDTO> getEventsDTOByRoomId(Long roomId) {
        log.info("Se solicită evenimentele DTO pentru sala ID={}", roomId);
        List<Event> events = eventRepository.findByRoomId(roomId);

        return events.stream().map(event -> {
            EventRoomDTO dto = new EventRoomDTO();
            dto.setId(event.getId());
            dto.setTitle(event.getTitle());
            dto.setDescription(event.getDescription());
            dto.setImageName(event.getImageName());
            dto.setStartHour(event.getStartHour());
            dto.setEndHour(event.getEndHour());

            if (event.getTrainer() != null && event.getTrainer().getUserAccount() != null) {
                dto.setTrainerName(event.getTrainer().getUserAccount().getFullName());
                dto.setTrainerImage(event.getTrainer().getUserAccount().getProfileImageUrl());
            }

            dto.setDays(event.getEventDays().stream()
                    .map(weekDay -> weekDay.getDay())
                    .collect(Collectors.toSet()));
            return dto;
        }).collect(Collectors.toList());
    }

    public Event getEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evenimentul nu există"));
    }


}
