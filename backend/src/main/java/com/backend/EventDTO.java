package com.backend;

import com.backend.enums.EWeekDays;

import com.backend.model.Event;
import com.backend.model.WeekDay;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

public class EventDTO {
    private Integer id;
    private String title;
    private String description;
    private String imageName;
    // în request, clientul va trimite doar trainerId
    private Long trainerId;
    private String trainerImage;
    // în response, vom popula și numele antrenorului
    private String trainerName;
    private LocalTime startHour;
    private LocalTime endHour;
    private Set<EWeekDays> days;

    public static EventDTO fromEntity(Event e) {
        EventDTO dto = new EventDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setImageName(e.getImageName());
        dto.setTrainerId(e.getTrainer().getUserAccount().getId());
        dto.setTrainerName(e.getTrainer().getUserAccount().getFullName());
        dto.setTrainerImage(e.getTrainer().getUserAccount().getProfileImageUrl());
        dto.setStartHour(e.getStartHour());
        dto.setEndHour(e.getEndHour());
        dto.setDays(e.getEventDays()
                .stream()
                .map(WeekDay::getDay)
                .collect(Collectors.toSet()));
        return dto;
    }

    public String getTrainerImage() {
        return trainerImage;
    }

    public void setTrainerImage(String trainerImage) {
        this.trainerImage = trainerImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public void setEndHour(LocalTime endHour) {
        this.endHour = endHour;
    }

    public Set<EWeekDays> getDays() {
        return days;
    }

    public void setDays(Set<EWeekDays> days) {
        this.days = days;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }
}
