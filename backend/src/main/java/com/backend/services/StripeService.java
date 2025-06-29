package com.backend.services;

import com.backend.DTOS.CheckoutSessionDTO;
import com.backend.model.SubscriptionPlan;
import com.backend.model.User;
import com.backend.repository.SubscriptionPlanRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;



    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        System.out.println("StripeService: Initialized with API key");
    }

    public CheckoutSessionDTO createCheckoutSession(User user, Long planId, String successUrl, String cancelUrl) throws StripeException {
        System.out.println("StripeService: Creating checkout session for user ID=" + user.getId() + ", planId=" + planId);
        System.out.println("StripeService: User email=" + user.getEmail());
        System.out.println("StripeService: Success URL=" + successUrl + ", Cancel URL=" + cancelUrl);

        SubscriptionPlan plan;
        try {
            plan = subscriptionPlanRepository.findById(planId)
                    .orElseThrow(() -> new RuntimeException("Subscription plan not found: " + planId));
            System.out.println("StripeService: Found plan: id=" + plan.getId() + ", name=" + plan.getDisplayName() + ", stripePriceId=" + plan.getStripePriceId() + ", active=" + plan.isActive());
        } catch (RuntimeException e) {
            System.out.println("StripeService: Error finding plan: " + e.getMessage());
            throw e;
        }

        if (plan.getStripePriceId() == null || plan.getStripePriceId().isEmpty()) {
            System.out.println("StripeService: Error: stripePriceId is null or empty for planId=" + planId);
            throw new RuntimeException("Invalid stripePriceId for plan " + planId);
        }

        SessionCreateParams.Builder builder = new SessionCreateParams.Builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setCustomerEmail(user.getEmail())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build()
                )
                .putMetadata("userId", user.getId().toString())
                .putMetadata("planId", plan.getId().toString());

        try {
            System.out.println("StripeService: Calling Stripe API to create session");
            Session session = Session.create(builder.build());
            System.out.println("StripeService: Checkout session created, sessionId=" + session.getId());

            CheckoutSessionDTO dto = new CheckoutSessionDTO();
            dto.setSessionId(session.getId());
            dto.setSessionUrl(session.getUrl());
            dto.setPublicKey(stripeApiKey);
            return dto;
        } catch (StripeException e) {
            System.out.println("StripeService: Stripe API error: " + e.getMessage());
            throw e;
        }
    }

    public String createStripeProduct(SubscriptionPlan plan) throws StripeException {
        System.out.println("StripeService: Creating Stripe product for plan: " + plan.getDisplayName());
        Map<String, Object> productParams = new HashMap<>();
        productParams.put("name", plan.getDisplayName());
        productParams.put("description", plan.getDescription());
        productParams.put("active", plan.isActive());

        Product product = Product.create(productParams);
        System.out.println("StripeService: Stripe product created: productId=" + product.getId());
        return product.getId();
    }

    public String createStripePrice(SubscriptionPlan plan) throws StripeException {
        System.out.println("StripeService: Creating Stripe price for plan: " + plan.getDisplayName());
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(plan.getStripeProductId())
                .setUnitAmount(plan.getPrice().multiply(new BigDecimal(100)).longValue())
                .setCurrency(plan.getCurrency().toLowerCase())
                .setRecurring(
                        PriceCreateParams.Recurring.builder()
                                .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                                .setIntervalCount((long) plan.getBillingCycleMonths())
                                .build()
                )
                .build();

        Price price = Price.create(priceParams);
        System.out.println("StripeService: Stripe price created: priceId=" + price.getId());
        return price.getId();
    }
}