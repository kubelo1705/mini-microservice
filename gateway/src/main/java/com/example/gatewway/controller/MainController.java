package com.example.gatewway.controller;

import com.example.gatewway.config.Queues;
import model.RequestData;
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
    public String add(@RequestBody RequestData requestData) throws ExecutionException, InterruptedException {
        return executeRequest(requestData, Queues.ADD);
    }

    @RequestMapping("subtract")
    public String subtract(@RequestBody RequestData requestData) throws ExecutionException, InterruptedException {
        return executeRequest(requestData, Queues.SUBTRACT);
    }

    private String executeRequest(RequestData requestData, Queues queue) throws ExecutionException, InterruptedException {
        CustomRequest request = new CustomRequest(requestData, queue);
        MainPool.submitJob(request.getWorkerName(), () -> {
            try {
                rabbitService.pushMessage(request);
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        return (String) request.getFuture().get();
    }
}
