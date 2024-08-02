package com.example.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class MessageService {

    @Async
    public CompletableFuture<String> print(String message) throws InterruptedException {
        System.out.println("Task Start - " + message);
        Thread.sleep(3000);
        return CompletableFuture.completedFuture("hji-" + message);
    }
}
