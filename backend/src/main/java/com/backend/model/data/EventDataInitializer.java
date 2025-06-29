// EventDataInitializer.java
package com.backend.model.data;

import com.backend.enums.EWeekDays;
import com.backend.model.Event;
import com.backend.model.GymRoom;
import com.backend.model.Trainer;
import com.backend.repository.EventRepository;
import com.backend.repository.GymRoomRepository;
import com.backend.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Populează baza de date cu 50 de evenimente (câte 10 pentru fiecare dintre primele 5 săli).
 * Un antrenor nu poate avea mai mult de 3 evenimente alocate.
 */
@Component
public class EventDataInitializer implements CommandLineRunner {

    private final EventRepository eventRepo;
    private final GymRoomRepository roomRepo;
    private final TrainerRepository trainerRepo;

    public EventDataInitializer(EventRepository eventRepo,
                                GymRoomRepository roomRepo,
                                TrainerRepository trainerRepo) {
        this.eventRepo = eventRepo;
        this.roomRepo = roomRepo;
        this.trainerRepo = trainerRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // rulează doar dacă nu există deja evenimente
        if (eventRepo.count() > 0) return;

        List<GymRoom> rooms = roomRepo.findAll();
        if (rooms.size() < 5) {
            System.out.println("EventDataInitializer: nu există cel puțin 5 săli – se oprește");
            return;
        }

        // păstrăm primele 5 săli (ordinea de inserare/ID)
        rooms = rooms.subList(0, 5);

        // preluăm toți trainerii disponibili şi pregătim contorul de evenimente per trainer
        List<Trainer> trainers = trainerRepo.findAll();
        if (trainers.isEmpty()) {
            System.out.println("EventDataInitializer: nu există antrenori – se oprește");
            return;
        }
        Map<Long, Integer> trainerEventCount = new HashMap<>();
        trainers.forEach(t -> trainerEventCount.put(t.getId(), 0));

        // liste ajutătoare pentru titluri şi descrieri
        List<String> classTypes = List.of(
                "HIIT Session", "Full Body Burn", "Power Yoga", "Pilates Core", "Upper Body Strength",
                "Cardio Blast", "Mobility Flow", "Kettlebell Power", "Dance Fitness", "TRX Challenge"
        );
        List<String> descriptors = List.of(
                "un antrenament intens de 45 de minute",
                "clasă pentru tonifiere şi rezistenţă",
                "sesiune dedicată posturii şi mobilităţii",
                "workout cu ardere mare de calorii",
                "antrenament funcţional pentru toate nivelurile"
        );
        // ore posibile de start
        int[] possibleHours = {6, 8, 10, 12, 14, 16, 18, 20};
        Random rnd = new Random();

        int generated = 0;
        for (GymRoom room : rooms) {
            for (int i = 0; i < 10; i++) {
                // alege trainer cu < 3 evenimente
                Trainer trainer = pickTrainerWithQuota(trainers, trainerEventCount, rnd);
                if (trainer == null) break; // nu mai avem traineri disponibili

                // generează titlu/descriere
                String className = classTypes.get(rnd.nextInt(classTypes.size()));
                String title = className + " - " + room.getNeighborhood();
                String description = "Participă la " + className.toLowerCase() + ", " +
                        descriptors.get(rnd.nextInt(descriptors.size())) + ".";

                // oră start + o oră durată
                int hour = possibleHours[rnd.nextInt(possibleHours.length)];
                LocalTime start = LocalTime.of(hour, 0);
                LocalTime end = start.plusMinutes(60);

                // zile random (2‑3 zile)
                List<EWeekDays> allDays = Arrays.asList(EWeekDays.values());
                Collections.shuffle(allDays);
                List<EWeekDays> eventDays = allDays.subList(0, rnd.nextInt(2) + 2); // 2 sau 3 zile

                Event ev = new Event();
                ev.setTitle(title);
                ev.setDescription(description);
                ev.setImageName("default.jpg");
                ev.setRoom(room);
                ev.setTrainer(trainer);
                ev.setStartHour(start);
                ev.setEndHour(end);
                eventDays.forEach(ev::addEventDay);

                eventRepo.save(ev);
                trainerEventCount.put(trainer.getId(), trainerEventCount.get(trainer.getId()) + 1);
                generated++;
            }
        }

        System.out.println("EventDataInitializer: Au fost generate " + generated + " evenimente");
    }

    /**
     * Alege un trainer care nu a depăşit limita de 3 evenimente.
     */
    private Trainer pickTrainerWithQuota(List<Trainer> trainers, Map<Long, Integer> counter, Random rnd) {
        List<Trainer> eligible = trainers.stream()
                .filter(t -> counter.getOrDefault(t.getId(), 0) < 3)
                .toList();
        if (eligible.isEmpty()) return null;
        return eligible.get(rnd.nextInt(eligible.size()));
    }
}
