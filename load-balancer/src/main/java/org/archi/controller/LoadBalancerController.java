package org.archi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class LoadBalancerController {

    @Value("${registry.url}")
    private String registryUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @GetMapping("/hello")
    public String handleHelloRequest() {
        Map<String, Map<String, Object>> workers = restTemplate.getForObject(registryUrl + "/registry/workers", Map.class);

        if (workers == null || workers.isEmpty()) {
            return "No available workers";
        }

        List<Map.Entry<String, Map<String, Object>>> availableWorkers = workers.entrySet().stream()
                .filter(entry -> "available".equals(entry.getValue().get("status")))
                .toList();

        if (availableWorkers.isEmpty()) {
            return "No available workers";
        }

        Map.Entry<String, Map<String, Object>> selectedWorker = availableWorkers.get(random.nextInt(availableWorkers.size()));
        String workerHost = selectedWorker.getValue().get("workerId").toString();
        int workerPort = (int) selectedWorker.getValue().get("port");
        String workerUrl = "http://" + workerHost + ":" + workerPort + "/hello";

        return restTemplate.getForObject(workerUrl, String.class);
    }
}

/*@RestController
public class LoadBalancerController {

    @Value("${registry.url}")
    private String registryUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @GetMapping("/hello")
    public String handleHelloRequest() {
        Map<String, Map<String, Object>> workers = restTemplate.getForObject(registryUrl + "/registry/workers", Map.class);

        if (workers == null || workers.isEmpty()) {
            return "No available workers";
        }

        List<Map.Entry<String, Map<String, Object>>> availableWorkers = workers.entrySet().stream()
                .filter(entry -> "available".equals(entry.getValue().get("status")))
                .toList();

        if (availableWorkers.isEmpty()) {
            return "No available workers";
        }

        Map.Entry<String, Map<String, Object>> selectedWorker = availableWorkers.get(random.nextInt(availableWorkers.size()));
        String workerUrl = "http://localhost:" + selectedWorker.getValue().get("port") + "/hello";

        return restTemplate.getForObject(workerUrl, String.class);
    }
}*/

/*@RestController
public class LoadBalancerController {

    @Value("${registry.url}")
    private String registryUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    @GetMapping("/hello")
    public String handleHelloRequest() {
        Map<String, Map<String, Object>> workers = restTemplate.getForObject(registryUrl + "/registry/workers", Map.class);

        if (workers == null || workers.isEmpty()) {
            return "No available workers";
        }

        List<Map.Entry<String, Map<String, Object>>> availableWorkers = workers.entrySet().stream()
                .filter(entry -> "available".equals(entry.getValue().get("status")))
                .toList();

        if (availableWorkers.isEmpty()) {
            return "No available workers";
        }

        Map.Entry<String, Map<String, Object>> selectedWorker = availableWorkers.get(random.nextInt(availableWorkers.size()));
        String workerUrl = "http://localhost:" + selectedWorker.getValue().get("port") + "/hello";

        return restTemplate.getForObject(workerUrl, String.class);
    }
}*/
