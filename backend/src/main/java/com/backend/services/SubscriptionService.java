package com.backend.services;

import com.backend.DTOS.CheckoutSessionDTO;
import com.backend.DTOS.SubscriptionPlanDTO;
import com.backend.model.SubscriptionPlan;
import com.backend.model.User;
import com.backend.model.UserSubscription;
import com.backend.repository.SubscriptionPlanRepository;
import com.backend.repository.UserSubscriptionRepository;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;
    
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    
    @Autowired
    private StripeService stripeService;

    @Transactional(readOnly = true)
    public List<SubscriptionPlanDTO> getActiveSubscriptionPlans() {
        try {
            System.out.println("Fetching active subscription plans...");
            List<SubscriptionPlan> plans = subscriptionPlanRepository.findByActiveTrue();
            System.out.println("Found " + plans.size() + " active subscription plans");
            
            // Log each plan for debugging
            for (SubscriptionPlan plan : plans) {
                System.out.println("Plan: " + plan.getName() + ", ID: " + plan.getId());
            }
            
            // Convert each plan to DTO with individual error handling
            return plans.stream()
                    .map(plan -> {
                        try {
                            return convertToDto(plan);
                        } catch (Exception e) {
                            System.err.println("Error converting plan " + plan.getId() + " to DTO: " + e.getMessage());
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(dto -> dto != null) // Filter out any null DTOs from failed conversions
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error in getActiveSubscriptionPlans: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch subscription plans: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public SubscriptionPlanDTO getSubscriptionPlanById(Long planId) {
        return subscriptionPlanRepository.findById(planId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));
    }

    @Transactional
    public CheckoutSessionDTO createCheckoutSession(User user, Long planId, String successUrl, String cancelUrl) throws StripeException {
        // Check if user already has an active subscription
        boolean hasActiveSubscription = userSubscriptionRepository.existsByUserIdAndStatusIn(
                user.getId(), 
                List.of("active", "trialing", "past_due")
        );
        
        if (hasActiveSubscription) {
            throw new IllegalStateException("User already has an active subscription");
        }

        return stripeService.createCheckoutSession(user, planId, successUrl, cancelUrl);
    }

    @Transactional
    public void handleSubscriptionCreated(String subscriptionId, String customerEmail, String priceId, String status) {
        // This method will be called by the webhook
        SubscriptionPlan plan = subscriptionPlanRepository.findByStripePriceId(priceId)
                .orElseThrow(() -> new RuntimeException("Price not found"));
        
        // In a real app, you would fetch the user by email
        // For now, we'll just log it
        System.out.println("Subscription created: " + subscriptionId + " for " + customerEmail);
    }

    @Transactional
    public void handleSubscriptionUpdated(String subscriptionId, String status) {
        UserSubscription subscription = userSubscriptionRepository.findByStripeSubscriptionId(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
                
        subscription.setStatus(status);
        subscription.setUpdatedAt(LocalDateTime.now());
        
        if (status.equals("canceled") || status.equals("unpaid")) {
            subscription.setEndedAt(LocalDateTime.now());
        }
        
        userSubscriptionRepository.save(subscription);
    }

    private SubscriptionPlanDTO convertToDto(SubscriptionPlan plan) {
        try {
            if (plan == null) {
                System.err.println("Cannot convert null SubscriptionPlan to DTO");
                return null;
            }
            
            System.out.println("Converting plan to DTO: " + plan.getId() + " - " + plan.getName());
            
            SubscriptionPlanDTO dto = new SubscriptionPlanDTO();
            dto.setId(plan.getId());
            dto.setName(plan.getName());
            dto.setDisplayName(plan.getDisplayName());
            dto.setDescription(plan.getDescription());
            dto.setPrice(plan.getPrice());
            dto.setCurrency(plan.getCurrency());
            dto.setBillingCycleMonths(plan.getBillingCycleMonths());
            dto.setActive(plan.isActive());
            dto.setMaxGymAccessPerWeek(plan.getMaxGymAccessPerWeek());
            dto.setPersonalTrainerIncluded(plan.getPersonalTrainerIncluded());
            dto.setAllGymsAccess(plan.getAllGymsAccess());
            dto.setGroupClassesIncluded(plan.getGroupClassesIncluded());
            dto.setPersonalTrainingSessions(plan.getPersonalTrainingSessions());
            
            System.out.println("Successfully converted plan to DTO: " + dto.getName());
            return dto;
            
        } catch (Exception e) {
            System.err.println("Error converting plan " + (plan != null ? plan.getId() : "null") + " to DTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert subscription plan to DTO", e);
        }
    }
}
