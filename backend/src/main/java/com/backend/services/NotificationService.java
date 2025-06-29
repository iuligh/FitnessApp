package com.backend.services;

import com.backend.enums.NotificationType;
import com.backend.model.Event;
import com.backend.model.Notification;
import com.backend.model.Program;
import com.backend.model.User;
import com.backend.repository.NotificationRepository;
import com.backend.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired private NotificationRepository notifRepo;
    @Autowired private UserService userService;

    public void notify(User receiver, Event event, String msg, NotificationType type) {
        Notification n = new Notification();
        n.setReceiver(receiver);
        n.setEvent(event);
        n.setMessage(msg);
        n.setType(type);
        n.setRead(false);
        notifRepo.save(n);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notifRepo.findByReceiverIdOrderByCreatedAtDesc(userId);
    }

    public void markAllAsRead(Long userId) {
        List<Notification> list = notifRepo.findByReceiverIdOrderByCreatedAtDesc(userId);
        list.forEach(n -> n.setRead(true));
        notifRepo.saveAll(list);
    }
}


