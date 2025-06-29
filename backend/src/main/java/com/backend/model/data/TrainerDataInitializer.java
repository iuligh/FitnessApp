package com.backend.model.data;

import com.backend.enums.ERole;
import com.backend.model.Trainer;
import com.backend.model.User;
import com.backend.repository.TrainerRepository;
import com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class TrainerDataInitializer implements CommandLineRunner {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public TrainerDataInitializer(TrainerRepository trainerRepository, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        // Crează înregistrări Trainer doar dacă nu există deja
        if (trainerRepository.count() > 0) {
            return;
        }

        // Lista de specializări unice
        List<String> specializations = Arrays.asList(
                "Fitness", "Yoga", "Pilates", "Crossfit", "Bodybuilding",
                "Zumba", "Aerobic", "Box", "Kickboxing", "Functional Training",
                "Stretching", "Cardio", "TRX", "HIIT", "Spinning",
                "Powerlifting", "Calisthenics", "Mobility", "Step Aerobics", "Circuit Training",
                "Bootcamp", "Tabata", "Kettlebell", "Dance Fitness", "Personal Training"
        );

        // Preia doar utilizatorii cu rol de trainer
        List<User> trainerUsers = userRepository.findAllByRolesName(ERole.ROLE_TRAINER);
        int count = Math.min(trainerUsers.size(), specializations.size());

        for (int i = 0; i < count; i++) {
            Trainer trainer = new Trainer();
            trainer.setUserAccount(trainerUsers.get(i));
            trainer.setSpecialization(specializations.get(i));
            trainerRepository.save(trainer);
        }

        System.out.println("TrainerDataInitializer: " + count + " traineri au fost adăugați cu succes.");
    }
}

