package com.backend.repository;

import com.backend.model.User;
import com.backend.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    Optional<UserSubscription> findByStripeSubscriptionId(String stripeSubscriptionId);
    
    @Query("SELECT us FROM UserSubscription us " +
           "WHERE us.user.id = :userId " +
           "AND (us.status = 'active' OR us.status = 'trialing' OR us.status = 'past_due')")
    List<UserSubscription> findActiveSubscriptionsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT us FROM UserSubscription us " +
           "WHERE us.currentPeriodEnd < :date " +
           "AND (us.status = 'active' OR us.status = 'trialing' OR us.status = 'past_due')")
    List<UserSubscription> findExpiringSubscriptions(@Param("date") LocalDateTime date);
    
    boolean existsByUserIdAndStatusIn(Long userId, List<String> statuses);
}
