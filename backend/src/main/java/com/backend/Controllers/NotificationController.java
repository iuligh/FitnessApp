// NotificationController.java
package com.backend.Controllers;

import com.backend.DTOS.NotificationDTO;
import com.backend.model.Notification;
import com.backend.repository.NotificationRepository;
import com.backend.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  @Autowired
  private NotificationRepository notificationRepository;

  @GetMapping("/notifications/{userId}")
  public ResponseEntity<?> getNotifications(@PathVariable Long userId) {
    try {
      List<Notification> notifications = notificationService.getNotificationsForUser(userId);
      return ResponseEntity.ok(notifications);
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", "Eroare la incarcarea notificarilor."));
    }
  }

  @PostMapping("/notifications/mark-read/{userId}")
  public ResponseEntity<?> markNotificationsAsRead(@PathVariable Long userId) {
    try {
      notificationService.markAllAsRead(userId);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(Map.of("error", "Eroare la actualizarea notificarilor."));
    }
  }

  @PutMapping("/notifications/{id}/read")
  public ResponseEntity<?> markAsRead(@PathVariable Long id) {
    Optional<Notification> opt = notificationRepository.findById(id);
    if (opt.isEmpty()) return ResponseEntity.notFound().build();

    Notification notif = opt.get();
    notif.setRead(true);
    notificationRepository.save(notif);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/notifications/user/{userId}")
  public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long userId) {
    List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
    List<NotificationDTO> dtos = notifications.stream().map(NotificationDTO::fromEntity).toList();
    return ResponseEntity.ok(dtos);
  }


}
