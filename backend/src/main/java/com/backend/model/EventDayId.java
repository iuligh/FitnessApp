package com.backend.model;

import com.backend.enums.EWeekDays;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EventDayId implements Serializable {

    private Integer eventId;

    @Enumerated(EnumType.STRING)
    private EWeekDays day;

    public EventDayId() {}

    public EventDayId(Integer eventId, EWeekDays day) {
        this.eventId = eventId;
        this.day = day;
    }

    // Getters, setters, equals, and hashCode methods
    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public EWeekDays getDay() {
        return day;
    }

    public void setDay(EWeekDays day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDayId that = (EventDayId) o;
        return Objects.equals(eventId, that.eventId) && day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, day);
    }
}
