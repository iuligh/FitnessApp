package com.backend.DTOS;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriptionPlanDTO {
    private Long id;
    private String name;
    private String displayName;
    private String description;
    private BigDecimal price;
    private String currency;
    private Integer billingCycleMonths;
    private boolean active;
    private Integer maxGymAccessPerWeek;
    private Boolean personalTrainerIncluded;
    private Boolean allGymsAccess;
    private Boolean groupClassesIncluded;
    private Integer personalTrainingSessions;
}
