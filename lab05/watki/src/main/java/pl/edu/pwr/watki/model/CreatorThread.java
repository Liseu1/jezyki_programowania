package pl.edu.pwr.watki.model;

import java.util.Random;

public class CreatorThread implements Runnable {

    private final Station station;
    private final Random random;

    public CreatorThread(Station station) {
        this.station = station;
        this.random = new Random();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                int index = random.nextInt(station.getEntryRampSize());
                int destination = random.nextInt(station.getExitRampSize()) + 1;

                Cargo cargo = new Cargo(destination);
                station.addCargo(index, cargo);

                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}