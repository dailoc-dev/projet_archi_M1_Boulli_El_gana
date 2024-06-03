package org.archi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {


    @Value("${worker.id}")
    private String workerId;

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Hello from " + workerId;
        }
        return "Hello " + name + ", I'm " + workerId;
    }

    @GetMapping("/goodbye")
    public String goodbye(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Goodbye from " + workerId;
        }
        return "Goodbye " + name + ", I'm " + workerId;
    }
}

