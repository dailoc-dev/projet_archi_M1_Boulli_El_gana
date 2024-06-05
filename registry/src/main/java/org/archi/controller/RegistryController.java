package org.archi.controller;


import org.archi.model.WorkerStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

@RestController
public class RegistryController {

    private final Set<String> nodes = new HashSet<>();
    private final Map<String, Set<String>> workers = new HashMap<>();

    @PostMapping("/register-node")
    public String registerNode(@RequestParam String node) {
        nodes.add(node);
        return "Node registered: " + node;
    }

    @PostMapping("/register-worker")
    public String registerWorker(@RequestParam String node, @RequestParam String worker) {
        workers.computeIfAbsent(node, k -> new HashSet<>()).add(worker);
        return "Worker registered: " + worker + " on node: " + node;
    }

    @GetMapping("/nodes")
    public Set<String> getNodes() {
        return nodes;
    }

    @GetMapping("/workers")
    public Map<String, Set<String>> getWorkers() {
        return workers;
    }
}

/*
@RestController
@RequestMapping("/registry")
public class RegistryController {

    private final Map<String, WorkerStatus> workers = new ConcurrentHashMap<>();

    @PostMapping("/register")
    public void registerWorker(@RequestBody WorkerStatus workerStatus) {
        workers.put(workerStatus.getWorkerId(), workerStatus);
    }

    @GetMapping("/workers")
    public Map<String, WorkerStatus> getWorkers() {
        return workers;
    }

    // On check toutes les 2 minutes si les workers se sont manifestés
    @Scheduled(fixedRate = 120000)
    public void checkWorkers() {
        long now = System.currentTimeMillis();
        workers.entrySet().removeIf(entry -> now - entry.getValue().getLastUpdated() > 120000);
    }

    @PutMapping("/update")
    public void updateWorkerStatus(@RequestBody WorkerStatus workerStatus) {
        workers.put(workerStatus.getWorkerId(), workerStatus);
    }
}*/
