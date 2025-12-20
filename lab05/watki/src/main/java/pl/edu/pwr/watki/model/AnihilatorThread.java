package pl.edu.pwr.watki.model;

public class AnihilatorThread implements Runnable {

    private final Station station;
    private int currentIndex = 0;

    public AnihilatorThread(Station station) {
        this.station = station;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Cargo removed = station.tryRemoveCargo(currentIndex);

                currentIndex++;
                if (currentIndex >= station.getExitRampSize()) {
                    currentIndex = 0;
                }

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
