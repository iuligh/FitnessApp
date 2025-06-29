package com.backend.config;

import com.backend.model.SubscriptionPlan;
import com.backend.repository.SubscriptionPlanRepository;
import com.backend.services.StripeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(SubscriptionPlanRepository planRepository, StripeService stripeService) {
        return args -> {
            // Only initialize if no plans exist
            if (planRepository.count() == 0) {
                System.out.println("Initializing subscription plans...");
                
                // Create Basic Plan
                SubscriptionPlan basicPlan = new SubscriptionPlan();
                basicPlan.setName("BASIC");
                basicPlan.setDisplayName("Abonament Basic");
                basicPlan.setDescription("Acces standard la sală");
                basicPlan.setPrice(new BigDecimal("100.00"));
                basicPlan.setMaxGymAccessPerWeek(3);
                basicPlan.setPersonalTrainerIncluded(false);
                basicPlan.setAllGymsAccess(false);
                basicPlan.setGroupClassesIncluded(false);
                basicPlan.setPersonalTrainingSessions(0);
                basicPlan.setActive(true);
                
                // Create Premium Plan
                SubscriptionPlan premiumPlan = new SubscriptionPlan();
                premiumPlan.setName("PREMIUM");
                premiumPlan.setDisplayName("Abonament Premium");
                premiumPlan.setDescription("Acces premium la toate facilitățile");
                premiumPlan.setPrice(new BigDecimal("200.00"));
                premiumPlan.setMaxGymAccessPerWeek(7);
                premiumPlan.setPersonalTrainerIncluded(true);
                premiumPlan.setAllGymsAccess(true);
                premiumPlan.setGroupClassesIncluded(true);
                premiumPlan.setPersonalTrainingSessions(4);
                premiumPlan.setActive(true);

                try {
                    // Save plans first to get their IDs
                    planRepository.save(basicPlan);
                    planRepository.save(premiumPlan);
                    
                    System.out.println("Subscription plans initialized successfully");
                } catch (Exception e) {
                    System.err.println("Error initializing subscription plans: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Subscription plans already exist, skipping initialization");
            }
        };
    }
}
