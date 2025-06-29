package com.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainers")
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    private String specialization; // ex: „Yoga”, „Fitness”, „Crossfit”

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User userAccount;

//    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonManagedReference(value = "trainer-events")
//    private List<Event> events = new ArrayList<>();

    // --- Getteri și setteri ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public User getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
    }

    public String getName() {
        return userAccount != null ? userAccount.getFullName() : null;
    }

    public String getProfileImage() {
        return userAccount != null ? userAccount.getProfileImageUrl() : null;
    }
}
