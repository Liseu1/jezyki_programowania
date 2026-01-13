package pl.edu.pwr.sockety.office.model;

public record HouseRequest(String ip, int port) {

    @Override
    public String toString() {
        return "Dom: " + ip + ":" + port;
    }
}