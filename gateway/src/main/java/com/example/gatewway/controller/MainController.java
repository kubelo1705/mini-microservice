package com.example.gatewway.controller;

import com.example.gatewway.config.Queues;
import model.Data;
import com.example.gatewway.request.CustomRequest;
import com.example.gatewway.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pool.MainPool;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("gateway")
public class MainController {
    @Autowired
    private RabbitService rabbitService;

    @RequestMapping("add")
    public String add(@RequestBody Data data) throws ExecutionException, InterruptedException {
        return executeRequest(data, Queues.ADD);
    }

    @RequestMapping("subtract")
    public String subtract(@RequestBody Data data) throws ExecutionException, InterruptedException {
        return executeRequest(data, Queues.SUBTRACT);
    }

    private String executeRequest(Data data, Queues queue) throws ExecutionException, InterruptedException {
        CustomRequest request = new CustomRequest(data, queue);
        MainPool.submitJob(request.getName(), () -> {
            try {
                rabbitService.pushMessage(request);
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        return (String) request.getFuture().get();
    }
}
