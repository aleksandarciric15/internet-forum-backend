package com.example.internetforum.services.impl;

import com.example.internetforum.models.entities.LogEntity;
import com.example.internetforum.models.enums.LogLevel;
import com.example.internetforum.repositories.LogRepository;
import com.example.internetforum.services.SiemServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SiemService implements SiemServiceInterface {

    private final LogRepository logRepository;

    public SiemService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    public void log(String message, LogLevel level, String logger){
        LogEntity logEntity = new LogEntity();
        logEntity.setId(null);
        logEntity.setMessage(message);
        logEntity.setLevel(level.toString());
        logEntity.setDate(new Date());
        logEntity.setLogger(logger);
        this.logRepository.saveAndFlush(logEntity);
    }

    @Override
    public void info(String message, String logger){
        this.log(message, LogLevel.INFO, logger);
    }

    @Override
    public void warning(String message, String logger){
        this.log(message, LogLevel.WARNING, logger);
    }

    @Override
    public void error(String message, String logger){
        this.log(message, LogLevel.ERROR, logger);
    }
}
