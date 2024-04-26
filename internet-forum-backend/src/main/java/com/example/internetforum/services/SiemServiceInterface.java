package com.example.internetforum.services;

public interface SiemServiceInterface {
    void info(String message, String logger);
    void warning(String message, String logger);
    void error(String message, String logger);
}
