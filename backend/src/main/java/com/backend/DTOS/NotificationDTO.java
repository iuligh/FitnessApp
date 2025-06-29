package com.backend.DTOS;

import com.backend.model.Notification;
import java.time.Instant;

/**
 * Data Transfer Object for Notification entity.
 */
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean read;
    private Instant createdAt;

    public NotificationDTO() {
    }

    /**
     * Constructor mapping all fields.
     * @param id ID of the notification
     * @param message Notification message
     * @param read Read status
     * @param createdAt Timestamp when created
     */
    public NotificationDTO(Long id, String message, boolean read, Instant createdAt) {
        this.id = id;
        this.message = message;
        this.read = read;
        this.createdAt = createdAt;
    }

    /**
     * Factory method to create DTO from entity.
     */
    public static NotificationDTO fromEntity(Notification n) {
        return new NotificationDTO(
                n.getId(),
                n.getMessage(),
                n.isRead(),
                n.getCreatedAt()
        );
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
