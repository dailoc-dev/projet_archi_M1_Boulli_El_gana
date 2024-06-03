package org.archi.scheduler;

import org.archi.model.WorkerStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


@Component
public class WorkerScheduler {

    @Value("${registry.url}")
    private String registryUrl;

    @Value("${worker.id}")
    private String workerId;

    @Value("${server.port}")
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void registerWorker() {
        boolean registered = false;
        int attempt = 0;

        // Lors du lancement, il est possible que les workers tentent de s'enregistrer avant que le registry ne soit prêt
        // Donc on essaie plusieurs fois jusqu'à ce que le registry soit disponible
        while (!registered && attempt < 10) {
            try {
                WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis(), port);
                restTemplate.postForEntity(registryUrl + "/registry/register", status, Void.class);
                registered = true;
            } catch (Exception e) {
                attempt++;
                System.out.println("Failed to register worker. Attempt " + attempt + " of 10.");
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // On notifie le registry toutes les 60s
    @Scheduled(fixedRate = 60000)
    public void notifyRegistry() {
        WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis(), port);
        restTemplate.put(registryUrl + "/registry/update", status);
    }
}

/*@Component
public class WorkerScheduler {

    @Value("${registry.url}")
    private String registryUrl;

    @Value("${worker.id}")
    private String workerId;

    @Value("${server.port}")
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void registerWorker() {
        boolean registered = false;
        int attempt = 0;
        while (!registered && attempt < 10) {
            try {
                WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis(), port);
                restTemplate.postForEntity(registryUrl + "/registry/register", status, Void.class);
                registered = true;
            } catch (Exception e) {
                attempt++;
                System.out.println("Failed to register worker. Attempt " + attempt + " of 10.");
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void notifyRegistry() {
        WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis(), port);
        restTemplate.put(registryUrl + "/registry/update", status);
    }
}*/

/*@Component
public class WorkerScheduler {

    @Value("${registry.url}")
    private String registryUrl;

    @Value("${worker.id}")
    private String workerId;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void registerWorker() {
        boolean registered = false;
        int attempt = 0;
        while (!registered && attempt < 10) {
            try {
                WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis());
                restTemplate.postForEntity(registryUrl + "/registry/register", status, Void.class);
                registered = true;
            } catch (Exception e) {
                attempt++;
                System.out.println("Failed to register worker. Attempt " + attempt + " of 10.");
                try {
                    Thread.sleep(5000); // Wait for 5 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void notifyRegistry() {
        WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis());
        restTemplate.put(registryUrl + "/registry/update", status);
    }
}*/

/*
@Component
public class WorkerScheduler {

    @Value("${registry.url}")
    private String registryUrl;

    @Value("${worker.id}")
    private String workerId;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 60000)
    public void notifyRegistry() {
        WorkerStatus status = new WorkerStatus(workerId, "available", System.currentTimeMillis());
        restTemplate.postForEntity(registryUrl + "/registry/update", status, Void.class);
    }
}
*/
