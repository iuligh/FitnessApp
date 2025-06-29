package com.backend.model;

import com.backend.enums.EWeekDays;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class GymRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nume al sălii (obligatoriu, max 100 caractere)
    @NotBlank
    @Size(max = 100)
    private String name;

    // Descriere text-lungă a sălii (opțional)
    @Column(columnDefinition = "TEXT")
    private String description;

    // Adresa completă (street + number)
    @Size(max = 255)
    private String address;

    // Orașul în care se află sala
    @Size(max = 100)
    private String city;

    // Cartierul / sector
    @Size(max = 100)
    private String neighborhood;

    // Coordonate GPS (pentru afișare pe hartă)
    private Double latitude;
    private Double longitude;

    // Programul zilnic, ex: "08:00-22:00"
    @Size(max = 50)
    private String workingHours;

    // Telefon de contact (opțional)
    @Size(max = 20)
    private String phoneNumber;

    // Email de contact (opțional)
    @Size(max = 100)
    private String email;

    @Size(max = 255)
    private String imageUrl;

    private Integer capacity;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Event> events = new ArrayList<>();


    public GymRoom() {
    }

    public GymRoom(String name, String description,
                   String address, String city,
                   String neighborhood, Double latitude,
                   Double longitude, String workingHours,
                   String phoneNumber, String email,
                   String imageUrl, Integer capacity) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.neighborhood = neighborhood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.workingHours = workingHours;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
    }
}
