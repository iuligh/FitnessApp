package com.backend.services;

import com.backend.model.GymRoom;
import com.backend.repository.GymRoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GymRoomService {
    private static final Logger log = LoggerFactory.getLogger(GymRoomService.class);

    private final GymRoomRepository roomRepo;

    public GymRoomService(GymRoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public List<GymRoom> findAll() {
        log.info("Se solicită lista tuturor sălilor");
        return roomRepo.findAll();
    }

    public GymRoom findById(Long id) {
        log.info("Se caută sala cu ID={}", id);
        return roomRepo.findById(id).orElseThrow(() -> {
            log.warn("Sala nu a fost găsită: ID={}", id);
            return new RuntimeException("GymRoom nu există cu ID=" + id);
        });
    }

    public GymRoom save(GymRoom room) {
        log.info("Salvare/actualizare sală: {}", room.getName());
        return roomRepo.save(room);
    }

    public void deleteById(Long id) {
        log.info("Ștergere sală cu ID={}", id);
        if (!roomRepo.existsById(id)) {
            log.warn("În ștergere, sala nu există: ID={}", id);
            throw new RuntimeException("Nu există sala cu ID=" + id);
        }
        roomRepo.deleteById(id);
        log.info("Sala cu ID={} a fost ștearsă", id);
    }
}
