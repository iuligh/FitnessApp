package com.backend.DTOS;

import com.backend.enums.EWeekDays;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
public class EventRoomDTO {
    private Integer id;
    private String title;
    private String description;
    private String imageName;
    private String trainerName;
    private String trainerImage;
    private LocalTime startHour;
    private LocalTime endHour;
    private Set<EWeekDays> days;
}
