package com.example.bottest;

import com.example.bottest.model.Users;
import com.example.bottest.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BotTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BotTestApplication.class, args);
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository) {
        return args -> {
        };
    }
}
