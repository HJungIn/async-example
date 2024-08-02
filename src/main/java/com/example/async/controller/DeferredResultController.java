package com.example.async.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeferredResultController {
    /**
     * /dr에서 기다리면 /dr/event에서 받아온 값을 보여준다.
     * */
    Queue<DeferredResult<String>> results = new ConcurrentLinkedDeque<>();

    @GetMapping("/dr")
    public DeferredResult<String> dr() {
        System.out.println(" 시작 ");
        DeferredResult<String> dr = new DeferredResult<>(600000L);
        results.add(dr);
        return dr;
    }

    @GetMapping("/dr/count")
    public String drCount() {
        return String.valueOf(results.size());
    }

    @GetMapping("/dr/event")
    public String drEvent(String msg) {
        for (DeferredResult<String> dr : results) {
            dr.setResult("Hello " + msg);
            results.remove(dr);
        }
        return "OK";
    }
}
