package org.archi.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebServerController {

    @Value("${registry.url}")
    private String registryUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/launch/{service}")
    public String launchService(@RequestParam String service, @RequestParam int nbworkers) {
        // Fetch nodes from registry
        String[] nodes = restTemplate.getForObject(registryUrl + "/nodes", String[].class);

        if (nodes == null || nodes.length == 0) {
            return "No nodes available";
        }

        // Distribute workers across nodes
        Map<String, Integer> workersPerNode = new HashMap<>();
        for (int i = 0; i < nbworkers; i++) {
            String node = nodes[i % nodes.length];
            workersPerNode.put(node, workersPerNode.getOrDefault(node, 0) + 1);
        }

        workersPerNode.forEach((node, count) -> {
            DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://" + node + ":2375").build();
            for (int i = 0; i < count; i++) {
                String workerName = service + "-worker-" + (i + 1);
                CreateContainerResponse container = dockerClient.createContainerCmd("worker-image")
                        .withName(workerName)
                        .withEnv("worker.id=" + workerName, "registry.url=" + registryUrl)
                        .withHostConfig(HostConfig.newHostConfig().withPortBindings(PortBinding.parse("808" + (i + 1) + ":8081")))
                        .exec();
                dockerClient.startContainerCmd(container.getId()).exec();
                restTemplate.postForObject(registryUrl + "/register-worker?node=" + node + "&worker=" + workerName, null, String.class);
            }
        });

        return "Launched " + nbworkers + " workers for service: " + service;
    }
}

