package com.authorization.springauthorization.service;

import com.authorization.springauthorization.entity.Role;
import com.authorization.springauthorization.entity.Users;
import com.authorization.springauthorization.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordInitializer {
    @Bean
    public CommandLineRunner init(UserRepo  userRepo, PasswordEncoder passwordEncoder) {
        return  args ->{
            if(userRepo.findByUsername("admin").isEmpty()){
                Users admin = new Users();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("passis@123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);

                System.out.println("Admin users is created.");
            }
            if(userRepo.findByUsername("Guest").isEmpty()){

                Users guest = new Users();
                guest.setUsername("Guest");
                guest.setPassword(passwordEncoder.encode("System@123"));
                guest.setRole(Role.GUEST);
                userRepo.save(guest);

                System.out.println("Guest user is created.");
            }

            if(userRepo.findByUsername("user").isEmpty()){

                Users user = new Users();
                user.setUsername("User");
                user.setPassword(passwordEncoder.encode("passis@123"));
                user.setRole(Role.USER);
                userRepo.save(user);

                System.out.println("Users is created.");
            }
        };
    }
}
