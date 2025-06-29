package com.backend.Controllers;

import com.backend.DTOS.CheckoutSessionDTO;
import com.backend.DTOS.SubscriptionPlanDTO;
import com.backend.model.User;
import com.backend.security.services.UserDetailsImpl;
import com.backend.services.SubscriptionService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.backend.services.StripeService;
import java.util.Arrays;
import java.util.List;


// CORS is handled by WebSecurityConfig
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private StripeService stripeService;

    @GetMapping("/plans")
    public ResponseEntity<?> getActiveSubscriptionPlans() {
        try {
            System.out.println("Received request to fetch active subscription plans");
            List<SubscriptionPlanDTO> plans = subscriptionService.getActiveSubscriptionPlans();
            System.out.println("Successfully retrieved " + plans.size() + " active subscription plans");
            return ResponseEntity.ok(plans);
        } catch (Exception e) {
            System.err.println("Error in getActiveSubscriptionPlans controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching subscription plans: " + 
                (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        }
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<SubscriptionPlanDTO> getSubscriptionPlan(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionPlanById(id));
    }

    @PostMapping("/checkout/{planId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createCheckoutSession(
            @PathVariable Long planId,
            @RequestParam String successUrl,
            @RequestParam String cancelUrl,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            if (userDetails == null) {
                return ResponseEntity.status(401).body("User details not found");
            }
            
            // Create a proper user object with all required fields
            User user = new User();
            user.setId(userDetails.getId());
            user.setEmail(userDetails.getEmail());
            user.setUsername(userDetails.getUsername());

            System.out.println("Creating checkout session for user: " + user.getEmail() + " (ID: " + user.getId() + ")");
            
            CheckoutSessionDTO session = stripeService.createCheckoutSession(user, planId, successUrl, cancelUrl);
            return ResponseEntity.ok(session);
            
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating checkout session: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload, 
                                              @RequestHeader("Stripe-Signature") String sigHeader) {
        // In a real app, you would verify the webhook signature here
        // and process different types of events
        return ResponseEntity.ok("Webhook received");
    }
}
