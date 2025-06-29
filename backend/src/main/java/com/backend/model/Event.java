package com.backend.model;

import com.backend.enums.EWeekDays;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String title;
    @Column
    private String description;
    @Column
    private String imageName;

    public Event(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @JsonBackReference(value = "trainer-events")
    private Trainer trainer;

    @Column
    private LocalTime startHour;
    @Column
    private LocalTime endHour;

    @ManyToOne
    @JoinColumn(name = "room_id") // se va crea coloana foreign key în tabelul events
    @JsonBackReference
    private GymRoom room;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<WeekDay> eventDays = new HashSet<>();

    public void addEventDay(EWeekDays day) {
        WeekDay eventDay = new WeekDay(this, day);
        eventDays.add(eventDay);
    }

    public void removeEventDay(EWeekDays day) {
        eventDays.removeIf(eventDay -> eventDay.getDay() == day);
    }
}
