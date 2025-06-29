package com.backend.Controllers;

import com.backend.EventDTO;
import com.backend.enums.EWeekDays;
import com.backend.enums.NotificationType;
import com.backend.model.Event;
import com.backend.model.User;
import com.backend.services.EventService;
import com.backend.services.NotificationService;
import com.backend.services.ProgramService;
import com.backend.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class EventsAdminController {

    @Value("${upload.dir}")
    private String uploadDir;

    private static final Logger log = LoggerFactory.getLogger(EventsAdminController.class);

    @Autowired
    private EventService eventService;
    @Autowired private ProgramService programService;
    @Autowired private UserService userService;
    @Autowired private NotificationService notificationService;

    @GetMapping("/events")
    public ResponseEntity<List<EventDTO>> getAllEvents(@RequestParam(required = false) String title) {
        try {
            List<EventDTO> events = eventService.getAllEvents();
            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable("id") long id) {
        EventDTO eventDTO = eventService.getEventById(id);
        if (eventDTO != null) {
            return new ResponseEntity<>(eventDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/events/day/{day}")
    public ResponseEntity<List<EventDTO>> getEventsByDay(@PathVariable("day") String day) {
        try {
            EWeekDays weekDay = EWeekDays.valueOf(day.toUpperCase());
            List<EventDTO> events = eventService.getEventsByDay(weekDay);
            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/events")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            EventDTO createdEvent = eventService.createEvent(eventDTO);
            return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") long id, @RequestBody EventDTO eventDTO) {
        try {
            Optional<EventDTO> updatedEvent = Optional.ofNullable(eventService.updateEvent((int) id, eventDTO));
            if (updatedEvent.isPresent()) {
                return new ResponseEntity<>(updatedEvent.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            eventService.deleteEvent(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/events/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            File targetFile = new File(directory, fileName);
            file.transferTo(targetFile);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/user/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsForUser(@PathVariable("userId") long userId) {
//        List<EventDTO> events = eventService.getEventsForUser(userId);
//        if (events.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
        //return new ResponseEntity<>(events, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/events/{id}/subscribe")
    public ResponseEntity<?> subscribe(@PathVariable Integer id, @RequestParam Long userId) {
        try {
            Event ev = eventService.getEventEntityById(Long.valueOf(id));
            User user = userService.getUserById(userId);

            if (ev == null || user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Evenimentul sau utilizatorul nu există"));
            }

            User trainer = ev.getTrainer().getUserAccount();
            if (trainer == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Evenimentul nu are asociat un antrenor."));
            }

            programService.subscribe(userId, id);

            notificationService.notify(user, ev,
                    "Te-ai înscris la „" + ev.getTitle() + "”", NotificationType.JOINED_EVENT);

            notificationService.notify(trainer, ev,
                    user.getFullName() + " s-a înscris la „" + ev.getTitle() + "”",
                    NotificationType.USER_REGISTERED_TO_YOUR_EVENT);

            return ResponseEntity.ok(Map.of("message", "Subscribed"));
        } catch (IllegalStateException ex) {
            log.warn("Înscriere dublă refuzată: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ești deja înscris la acest eveniment."));
        } catch (RuntimeException ex) {
            log.error("Eroare la înscrierea în eveniment", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            log.error("Eroare generală la subscribe", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "A apărut o eroare neașteptată la înscriere."));
        }
    }
}
