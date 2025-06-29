package com.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
@Data
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "BASIC", "PREMIUM"

    @Column(nullable = false)
    private String displayName; // e.g., "Abonament Basic"


    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String currency = "RON";

    @Column(nullable = false)
    private Integer billingCycleMonths = 1;

    @Column(nullable = false)
    private String stripePriceId; // ID-ul din Stripe pentru acest plan

    @Column(nullable = false)
    private String stripeProductId; // ID-ul produsului din Stripe

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    // Caracteristici ale abonamentului
    private Integer maxGymAccessPerWeek = 3;
    private Boolean personalTrainerIncluded = false;
    private Boolean allGymsAccess = false;
    private Boolean groupClassesIncluded = false;
    private Integer personalTrainingSessions = 0;
}
