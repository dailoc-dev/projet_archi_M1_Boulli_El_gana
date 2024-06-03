package org.archi.model;

public class WorkerStatus {
    private String workerId;
    private String status;
    private long lastUpdated;
    private int port;

    public WorkerStatus() {
    }

    public WorkerStatus(String workerId, String status, long lastUpdated, int port) {
        this.workerId = workerId;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.port = port;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "WorkerStatus{" +
                "workerId='" + workerId + '\'' +
                ", status='" + status + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", port=" + port +
                '}';
    }
}
