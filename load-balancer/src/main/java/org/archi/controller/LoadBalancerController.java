package org.archi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import java.util.*;

@RestController
public class LoadBalancerController {

    @Value("${service.hello.urls}")
    private String helloServiceUrls;

    @Value("${service.goodbye.urls}")
    private String goodbyeServiceUrls;

    private final RestTemplate restTemplate = new RestTemplate();
    private Map<String, List<String>> serviceToWorkerUrls;

    @PostConstruct
    public void init() {
        serviceToWorkerUrls = new HashMap<>();
        serviceToWorkerUrls.put("hello", Arrays.asList(helloServiceUrls.split(",")));
        serviceToWorkerUrls.put("goodbye", Arrays.asList(goodbyeServiceUrls.split(",")));
    }

    @GetMapping("/hello")
    public String handleHelloRequest(@RequestParam(required = false) String name) {
        return handleRequest("hello", name);
    }

    @GetMapping("/goodbye")
    public String handleGoodbyeRequest(@RequestParam(required = false) String name) {
        return handleRequest("goodbye", name);
    }

    private String handleRequest(String service, String name) {
        List<String> workerUrls = new ArrayList<>(serviceToWorkerUrls.get(service));
        if (workerUrls == null || workerUrls.isEmpty()) {
            return "No workers available for service: " + service;
        }

        Collections.shuffle(workerUrls); // Shuffle the list to randomize the order
        System.out.println("Shuffled URLs: " + workerUrls);

        for (String workerUrl : workerUrls) {
            try {
                System.out.println("Trying URL: " + workerUrl);
                String urlWithParam = name != null && !name.isEmpty() ? workerUrl + "?name=" + name : workerUrl;
                return restTemplate.getForObject(urlWithParam, String.class);
            } catch (ResourceAccessException e) {
                // Log error and try the next worker
                System.err.println("Failed to connect to " + workerUrl + ": " + e.getMessage());
            }
        }

        return "All workers are unavailable for service: " + service;
    }
}

//@RestController
//public class LoadBalancerController {
//
//    @Value("${service.hello.urls}")
//    private String helloServiceUrls;
//
//    @Value("${service.goodbye.urls}")
//    private String goodbyeServiceUrls;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private Map<String, List<String>> serviceToWorkerUrls;
//
//    @PostConstruct
//    public void init() {
//        serviceToWorkerUrls = Map.of(
//                "hello", List.of(helloServiceUrls.split(",")),
//                "goodbye", List.of(goodbyeServiceUrls.split(","))
//        );
//    }
//
//    @GetMapping("/hello")
//    public String handleHelloRequest() {
//        return handleRequest("hello");
//    }
//
//    @GetMapping("/goodbye")
//    public String handleGoodbyeRequest() {
//        return handleRequest("goodbye");
//    }
//
//    private String handleRequest(String service) {
//        List<String> workerUrls = serviceToWorkerUrls.get(service);
//        if (workerUrls == null || workerUrls.isEmpty()) {
//            return "No workers available for service: " + service;
//        }
//
//        for (int i = 0; i < workerUrls.size(); i++) {
//            String workerUrl = workerUrls.get(ThreadLocalRandom.current().nextInt(workerUrls.size()));
//            try {
//                return restTemplate.getForObject(workerUrl, String.class);
//            } catch (ResourceAccessException e) {
//                // Log error and try the next worker
//                System.err.println("Failed to connect to " + workerUrl + ": " + e.getMessage());
//            }
//        }
//
//        return "All workers are unavailable for service: " + service;
//    }
//}

//@RestController
//public class LoadBalancerController {
//
//    @Value("${registry.url}")
//    private String registryUrl;
//
//    @Value("${service.hello.urls}")
//    private List<String> helloServiceUrls;
//
//    @Value("${service.goodbye.urls}")
//    private List<String> goodbyeServiceUrls;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    private Map<String, List<String>> serviceToWorkerUrls;
//
//    @PostConstruct
//    public void init() {
//        serviceToWorkerUrls = Map.of(
//                "hello", helloServiceUrls,
//                "goodbye", goodbyeServiceUrls
//        );
//    }
//
//    @GetMapping("/hello")
//    public String handleHelloRequest() {
//        return handleRequest("hello");
//    }
//
//    @GetMapping("/goodbye")
//    public String handleGoodbyeRequest() {
//        return handleRequest("goodbye");
//    }
//
//    private String handleRequest(String service) {
//        List<String> workerUrls = serviceToWorkerUrls.get(service);
//        if (workerUrls == null || workerUrls.isEmpty()) {
//            return "No workers available for service: " + service;
//        }
//        String workerUrl = workerUrls.get(ThreadLocalRandom.current().nextInt(workerUrls.size()));
//        return restTemplate.getForObject(workerUrl, String.class);
//    }
//}

//@RestController
//public class LoadBalancerController {
//
//    @Value("${registry.url}")
//    private String registryUrl;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final Random random = new Random();
//
//    @GetMapping("/hello")
//    public String handleHelloRequest() {
//        Map<String, Map<String, Object>> workers = restTemplate.getForObject(registryUrl + "/registry/workers", Map.class);
//
//        if (workers == null || workers.isEmpty()) {
//            return "No available workers";
//        }
//
//        List<Map.Entry<String, Map<String, Object>>> availableWorkers = workers.entrySet().stream()
//                .filter(entry -> "available".equals(entry.getValue().get("status")))
//                .toList();
//
//        if (availableWorkers.isEmpty()) {
//            return "No available workers";
//        }
//
//        Map.Entry<String, Map<String, Object>> selectedWorker = availableWorkers.get(random.nextInt(availableWorkers.size()));
//        String workerHost = selectedWorker.getValue().get("workerId").toString();
//        int workerPort = (int) selectedWorker.getValue().get("port");
//        String workerUrl = "http://" + workerHost + ":" + workerPort + "/hello";
//
//        return restTemplate.getForObject(workerUrl, String.class);
//    }
//}


