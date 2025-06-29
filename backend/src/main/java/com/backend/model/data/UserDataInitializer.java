package com.backend.model.data;

import com.backend.enums.ERole;
import com.backend.model.Role;
import com.backend.model.User;
import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class UserDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            ensureRolesExist();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow();
            Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER).orElseThrow();
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow();
            for (int i = 1; i <= 5; i++) {
                User user = new User();
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@example.com");
                user.setPassword(passwordEncoder.encode("password"));
                user.setFullName("User Simp" + i);
                user.setCity("Bucuresti");
                user.setAddress("Strada Utilizatorului nr. " + i);
                user.setPhoneNumber("07240000" + String.format("%02d", i));
                user.setProfileImageUrl("default.jpg");
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
            }
            List<User> users = List.of(
                    new User("andrei.popescu", "andrei.popescu@example.com", passwordEncoder.encode("password")),
                    new User("maria.ionescu", "maria.ionescu@example.com", passwordEncoder.encode("password")),
                    new User("george.radu", "george.radu@example.com", passwordEncoder.encode("password")),
                    new User("ioana.enache", "ioana.enache@example.com", passwordEncoder.encode("password")),
                    new User("daniel.stan", "daniel.stan@example.com", passwordEncoder.encode("password")),
                    new User("roxana.neagu", "roxana.neagu@example.com", passwordEncoder.encode("password")),
                    new User("alex.petre", "alex.petre@example.com", passwordEncoder.encode("password")),
                    new User("elena.tudor", "elena.tudor@example.com", passwordEncoder.encode("password")),
                    new User("mihai.ilie", "mihai.ilie@example.com", passwordEncoder.encode("password")),
                    new User("ana.dumitru", "ana.dumitru@example.com", passwordEncoder.encode("password")),
                    new User("stefan.voicu", "stefan.voicu@example.com", passwordEncoder.encode("password")),
                    new User("irina.marinescu", "irina.marinescu@example.com", passwordEncoder.encode("password")),
                    new User("david.pavel", "david.pavel@example.com", passwordEncoder.encode("password")),
                    new User("alina.cristescu", "alina.cristescu@example.com", passwordEncoder.encode("password")),
                    new User("cristian.dragomir", "cristian.dragomir@example.com", passwordEncoder.encode("password")),
                    new User("bianca.stanescu", "bianca.stanescu@example.com", passwordEncoder.encode("password")),
                    new User("florin.baciu", "florin.baciu@example.com", passwordEncoder.encode("password")),
                    new User("oana.serban", "oana.serban@example.com", passwordEncoder.encode("password")),
                    new User("vlad.popa", "vlad.popa@example.com", passwordEncoder.encode("password")),
                    new User("camelia.rosu", "camelia.rosu@example.com", passwordEncoder.encode("password")),
                    new User("raul.andrei", "raul.andrei@example.com", passwordEncoder.encode("password")),
                    new User("diana.barbu", "diana.barbu@example.com", passwordEncoder.encode("password")),
                    new User("tudor.constantin", "tudor.constantin@example.com", passwordEncoder.encode("password")),
                    new User("adelina.nistor", "adelina.nistor@example.com", passwordEncoder.encode("password")),
                    new User("marcel.toma", "marcel.toma@example.com", passwordEncoder.encode("password")),
                    new User("sabina.vasile", "sabina.vasile@example.com", passwordEncoder.encode("password")),
                    new User("luca.gheorghe", "luca.gheorghe@example.com", passwordEncoder.encode("password")),
                    new User("daria.pintilie", "daria.pintilie@example.com", passwordEncoder.encode("password")),
                    new User("paul.mocanu", "paul.mocanu@example.com", passwordEncoder.encode("password")),
                    new User("georgiana.floroiu", "georgiana.floroiu@example.com", passwordEncoder.encode("password"))
            );

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                user.setFullName(user.getUsername().replace('.', ' ').toUpperCase());
                user.setCity("Bucuresti");
                user.setAddress("Strada Exemplu nr. " + (i + 1));
                user.setPhoneNumber("07231234" + String.format("%02d", i));
                user.setProfileImageUrl("default.jpg");

                Set<Role> roles = new HashSet<>();
                if (i < 3) {
                    User admin = new User("admin" + i, "admin" + i + "@example.com", passwordEncoder.encode("password"));
                    admin.setFullName("Administrator " + i);
                    admin.setCity("Bucuresti");
                    admin.setAddress("Bd. Admin nr. " + i);
                    admin.setPhoneNumber("070000000" + i);
                    admin.setProfileImageUrl("default.jpg");

                    roles.add(adminRole);
                    admin.setRoles(roles);

                    userRepository.save(admin);;
                } else {
                    roles.add(trainerRole);
                }
                user.setRoles(roles);

                userRepository.save(user);
            }

            System.out.println("UserDataInitializer: 30 utilizatori personalizați au fost creați.");
        }
    }
    private void ensureRolesExist() {
        for (ERole role : ERole.values()) {
            Optional<Role> found = roleRepository.findByName(role);
            if (found.isEmpty()) {
                Role newRole = new Role();
                newRole.setName(role);
                roleRepository.save(newRole);
                System.out.println("Rolul " + role.name() + " a fost adăugat în baza de date.");
            }
        }
    }
}
