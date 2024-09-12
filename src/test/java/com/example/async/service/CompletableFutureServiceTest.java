package com.example.async.service;

import org.junit.jupiter.api.Test;

@SpringBootTest
class CompletableFutureServiceTest {

    @Autowired
    private CompletableFutureService completableFutureService;

    @Test
    void 비동기_async(){
        for (int i = 0; i < 10; i++) {
            completableFutureService.asyncMethod(i);
        }
        System.out.println("Tasks executed asynchronously");
    }
    /*
    // 결과
    Tasks executed asynchronously
    Executing task 1 in thread: AsyncThread-2
    Executing task 2 in thread: AsyncThread-3
    Executing task 5 in thread: AsyncThread-2
    Executing task 7 in thread: AsyncThread-2
    Executing task 8 in thread: AsyncThread-2
    Executing task 9 in thread: AsyncThread-2
    Executing task 6 in thread: AsyncThread-3
    Executing task 3 in thread: AsyncThread-4
    Executing task 4 in thread: AsyncThread-5
    Executing task 0 in thread: AsyncThread-1
     */


    @Test
    void 비동기_CompletableFuture(){
        for (int i = 0; i < 10; i++) {
            completableFutureService.asyncMethod2(i);
        }
        System.out.println("Tasks executed asynchronously");
    }
    /*
    // 결과
    Tasks executed asynchronously
    Executing task 1 in thread: AsyncThread-2
    Executing task 5 in thread: AsyncThread-2
    Executing task 6 in thread: AsyncThread-2
    Executing task 7 in thread: AsyncThread-2
    Executing task 8 in thread: AsyncThread-2
    Executing task 9 in thread: AsyncThread-2
    Executing task 0 in thread: AsyncThread-1
    Executing task 2 in thread: AsyncThread-3
    Executing task 3 in thread: AsyncThread-4
    Executing task 4 in thread: AsyncThread-5
     */
}