package com.backend.model;

import com.backend.enums.EWeekDays;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "week_day")
public class WeekDay {
    @EmbeddedId
    private EventDayId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;


    public WeekDay() {}

    public WeekDay(Event event, EWeekDays day) {
        this.event = event;
        this.id = new EventDayId(event.getId(), day);
    }

    public EventDayId getId() {
        return id;
    }

    public void setId(EventDayId id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EWeekDays getDay() {
        return id.getDay();
    }

    public void setDay(EWeekDays day) {
        this.id.setDay(day);
    }
}
