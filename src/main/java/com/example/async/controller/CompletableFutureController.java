package com.example.async.controller;

import com.example.async.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CompletableFutureController {

    private final MessageService messageService;

    @GetMapping("/messages")
    @ResponseStatus(code = HttpStatus.OK)
    public void printMessage() throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            CompletableFuture<String> completableFuture = messageService.print(i + "");
            completableFuture
                    .thenAccept(System.out::println)
                    .exceptionally(error -> {
                        System.out.println(error.getMessage());
                        return null;
                    });
        }
    }

    /**====     1. 작업 실행        ===*/
    /**
     * runAsync : 반환 값 x
     * */
    void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
        });

        future.get();
        System.out.println("Thread: " + Thread.currentThread().getName());
    }

    /**
     * supplyAsync : 반환 값 o
     * */
    void supplyAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        });

        System.out.println(future.get());
        System.out.println("Thread: " + Thread.currentThread().getName());
    }


    /**====     2. 작업 콜백        ===*/
    /**
     * thenApply : 반환 값을 받아서 다른 값을 반환함
     * 파라미터 : function (return o)
     * */
    void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        }).thenApply(s -> {
            return s.toUpperCase();
        });

        System.out.println(future.get());
    }

    /**
     * thenAccept : 반환 값을 받아 처리하고 값을 반환하지 않음
     * 파라미터 : comsumer (return x)
     * */
    void thenAccept() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        }).thenAccept(s -> {
            System.out.println(s.toUpperCase());
        });

        future.get();
    }

    /**
     * thenRun : 반환 값을 받지 않고 다른 작업을 실행
     * 파라미터 : Runnable (return x)
     * */
    void thenRun() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            return "Thread: " + Thread.currentThread().getName();
        }).thenRun(() -> {
            System.out.println("Thread: " + Thread.currentThread().getName());
        });

        future.get();
    }


    /**====     3. 작업 조합        ===*/
    /**
     * thenCompose : 두 작업이 이어서 실행하도록 조합하며, 앞선 작업의 결과를 받아서 사용할 수 있음
     * 파라미터 : function (return o)
     * */
    void thenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });

        // Future 간에 연관 관계가 있는 경우
        CompletableFuture<String> future = hello.thenCompose(this::mangKyu);
        System.out.println(future.get());
    }

    private CompletableFuture<String> mangKyu(String message) {
        return CompletableFuture.supplyAsync(() -> {
            return message + " " + "MangKyu";
        });
    }


    /**
     * thenCombine : 두 작업을 독립적으로 실행하고, 둘 다 완료되었을 때 콜백을 실행함
     * 파라미터 : function (return o)
     * */
    void thenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });

        CompletableFuture<String> mangKyu = CompletableFuture.supplyAsync(() -> {
            return "MangKyu";
        });

        CompletableFuture<String> future = hello.thenCombine(mangKyu, (h, w) -> h + " " + w);
        System.out.println(future.get());
    }


    /**
     * allOf : 여러 작업들을 동시에 실행하고, 모든 작업 결과에 콜백을 실행함
     * */
    void allOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        });

        CompletableFuture<String> mangKyu = CompletableFuture.supplyAsync(() -> {
            return "MangKyu";
        });

        List<CompletableFuture<String>> futures = List.of(hello, mangKyu);

        CompletableFuture<List<String>> result = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                .thenApply(v -> futures.stream().
                        map(CompletableFuture::join).
                        collect(Collectors.toList()));

        result.get().forEach(System.out::println);
    }

    /**
     * anyOf : 여러 작업들 중에서 가장 빨리 끝난 하나의 결과에 콜백을 실행함
     * */
    void anyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> hello = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return "Hello";
        });

        CompletableFuture<String> mangKyu = CompletableFuture.supplyAsync(() -> {
            return "MangKyu";
        });

        CompletableFuture<Void> future = CompletableFuture.anyOf(hello, mangKyu).thenAccept(System.out::println);
        future.get();
    }


    /**====     4. 예외 처리        ===*/
    /**
     * exceptionally : 발생한 에러를 받아서 예외를 처리
     * 파라미터 : function (return o)
     * */
    void exceptionally(boolean doThrow) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (doThrow) {
                throw new IllegalArgumentException("Invalid Argument");
            }

            return "Thread: " + Thread.currentThread().getName();
        }).exceptionally(e -> {
            return e.getMessage();
        });

        System.out.println(future.get());
    }

    /**
     * handle : (결과값, 에러)를 반환받아 에러가 발생한 경우와 아닌 경우 모두를 처리할 수 있음
     * 파라미터 : BiFunction (2개의 인자, 1개의 객체를 리턴)
     * */
    void handle(boolean doThrow) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (doThrow) {
                throw new IllegalArgumentException("Invalid Argument");
            }

            return "Thread: " + Thread.currentThread().getName();
        }).handle((result, e) -> {
            return e == null
                    ? result
                    : e.getMessage();
        });

        System.out.println(future.get());
    }
}

