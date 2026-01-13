package pl.edu.pwr.sockety.office.model;

public class Tanker {
    private final int id;
    private final String ip;
    private final int port;
    public boolean isReady = true;

    public Tanker(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.id = port;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "ID:" + id + " | " + ip + ":" + port + (isReady ? " [WOLNA]" : " [W TRASIE]");
    }
}