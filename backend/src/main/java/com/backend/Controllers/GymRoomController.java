package com.backend.Controllers;

import com.backend.DTOS.EventRoomDTO;
import com.backend.model.Event;
import com.backend.model.GymRoom;
import com.backend.services.EventService;
import com.backend.services.GymRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "http://localhost:4200")
@Validated
public class GymRoomController {
    private static final Logger log = LoggerFactory.getLogger(GymRoomController.class);

    private final GymRoomService roomService;
    private final EventService eventService;

    public GymRoomController(GymRoomService roomService, EventService eventService) {
        this.roomService = roomService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRooms() {
        try {
            log.info("HTTP GET /api/rooms");
            List<GymRoom> rooms = roomService.findAll();
            return ResponseEntity.ok(rooms);
        } catch (Exception ex) {
            log.error("Eroare la GET /api/rooms", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Nu se pot încărca sălile."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(@PathVariable Long id) {
        try {
            log.info("HTTP GET /api/rooms/{}", id);
            GymRoom room = roomService.findById(id);
            return ResponseEntity.ok(room);
        } catch (RuntimeException rex) {
            log.warn("Sala nu găsită: ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", rex.getMessage()));
        } catch (Exception ex) {
            log.error("Eroare la GET /api/rooms/{}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Eroare internă."));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody GymRoom room) {
        try {
            log.info("HTTP POST /api/rooms — creează sală: {}", room.getName());
            GymRoom saved = roomService.save(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception ex) {
            log.error("Eroare la POST /api/rooms", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Nu s-a putut crea sala."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            log.info("HTTP DELETE /api/rooms/{}", id);
            roomService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException rex) {
            log.warn("Nu se poate șterge sala (nu există sau altă problemă): ID={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", rex.getMessage()));
        } catch (Exception ex) {
            log.error("Eroare la DELETE /api/rooms/{}", id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Eroare internă la ștergerea sălii."));
        }
    }

    /**
     * Endpoint nou: obține toate evenimentele asociate sălii cu ID={roomId}.
     */
    @GetMapping("/{roomId}/events")
    public ResponseEntity<?> getEventsForRoom(@PathVariable Long roomId) {
        try {
            log.info("HTTP GET /api/rooms/{}/events", roomId);
            roomService.findById(roomId); // verificare existență

            List<EventRoomDTO> events = eventService.getEventsDTOByRoomId(roomId);
            if (events.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(events);

        } catch (RuntimeException rex) {
            log.warn("Sala nu găsită: ID={}", roomId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", rex.getMessage()));
        } catch (Exception ex) {
            log.error("Eroare la GET /api/rooms/{}/events", roomId, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Nu se pot încărca evenimentele."));
        }
    }

}
