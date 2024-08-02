package com.example.async.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.Executors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmitterController {
    /**
     * ResponseBodyEmitter 라는
     * 클라이언트 측에서 폴링을 따로 사용하지 않고, HTTP 커넥션을 통해 서버에서 이벤트 발생 시 클라이언트 측으로 데이터를 푸시하는 기술
     * */
    @GetMapping("/emitter")
    public ResponseBodyEmitter emitter() {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                for (int i = 1; i <= 50; i++) {
                    emitter.send("<p> Stream " + i + "</p>");
                    Thread.sleep(100);
                }
            } catch (Exception e) {
            }
        });

        return emitter;
    }
}