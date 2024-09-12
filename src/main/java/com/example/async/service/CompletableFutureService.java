package com.example.async.service;


import java.util.concurrent.CompletableFuture;

@Service
public class CompletableFutureService {

    @Async("asyncExecutor")
    public void asyncMethod(int i) {
        System.out.println("Executing task " + i + " in thread: " + Thread.currentThread().getName());
        try {
            // Simulate some work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // 권장
            e.printStackTrace();
        }
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> asyncMethod2(int i) {
        System.out.println("Executing task " + i + " in thread: " + Thread.currentThread().getName());
        try {
            // Simulate some work
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // 권장
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Task " + i + " completed");
    }
}
