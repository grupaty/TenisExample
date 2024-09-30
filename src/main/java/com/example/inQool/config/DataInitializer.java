package com.example.inQool.config;

import com.example.inQool.model.Court;
import com.example.inQool.model.Role;
import com.example.inQool.model.SurfaceType;
import com.example.inQool.model.User;
import com.example.inQool.repository.CourtRepository;
import com.example.inQool.repository.SurfaceTypeRepository;
import com.example.inQool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Value("${app.data.initialize}")
    private boolean initializeData;

    private final SurfaceTypeRepository surfaceTypeRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    public DataInitializer(SurfaceTypeRepository surfaceTypeRepository, CourtRepository courtRepository, UserRepository userRepository) {
        this.surfaceTypeRepository = surfaceTypeRepository;
        this.courtRepository = courtRepository;
        this.userRepository =  userRepository;
    }

    @Override
    public void run(String... args) {
        if (initializeData) {
            initializeSurfaceTypesAndCourts();
            initializeUsers();
        }
    }

    private void initializeSurfaceTypesAndCourts() {
        SurfaceType clay = new SurfaceType(null, "Clay", 5.0);
        SurfaceType grass = new SurfaceType(null, "Grass", 6.0);
        surfaceTypeRepository.save(clay);
        surfaceTypeRepository.save(grass);

        clay = surfaceTypeRepository.findByName("Clay");
        grass = surfaceTypeRepository.findByName("Grass");

        Court court1 = new Court(null, "Court 1", clay,false);
        Court court2 = new Court(null, "Court 2", grass, false);
        Court court3 = new Court(null, "Court 3", clay, false);
        Court court4 = new Court(null, "Court 4", grass, false);

        courtRepository.save(court1);
        courtRepository.save(court2);
        courtRepository.save(court3);
        courtRepository.save(court4);
    }

    private void initializeUsers() {
        User adminUser = new User(null, "admin", "userSecond", "admin123", "111", Role.ADMIN,false);
        User user1 = new User(null, "user1", "userSecond", "user123", "222", Role.USER, false);
        User user2 = new User(null, "user2", "userSecond","user456", "333", Role.USER, false);

        userRepository.save(adminUser);
        userRepository.save(user1);
        userRepository.save(user2);
    }
}