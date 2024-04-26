package com.example.internetforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.security.SecureRandom;
import java.util.Random;

@SpringBootApplication
@EnableAsync
public class InternetForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(InternetForumApplication.class, args);
    }


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }
}
