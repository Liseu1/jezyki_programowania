package pl.edu.pwr.watki.model;

import java.util.Random;

public class FeederThread implements Runnable {

    private final Station station;
    private int currentPosition;
    private Cargo currentCargo;

    private int stuckCounter = 0;
    private int forcedDirection = 0;
    private boolean escaping = false;

    private final Random random = new Random();

    public FeederThread(Station station, int startPosition) {
        this.station = station;
        this.currentPosition = startPosition;
        this.currentCargo = null;
    }

    @Override
    public void run() {
        station.registerFeeder(currentPosition);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(400 + random.nextInt(200));

                boolean actionDone = false;

                // drop cargo
                if (currentCargo != null) {
                    if (station.tryDrop(currentPosition, currentCargo)) {
                        currentCargo = null;
                        actionDone = true;
                        escaping = false;
                        forcedDirection = 0;
                    }
                }

                // pick cargo
                if (!actionDone && currentCargo == null) {
                    currentCargo = station.tryPickUp(currentPosition);
                    if (currentCargo != null) actionDone = true;
                }

                if (!actionDone) {

                    int trackSize = station.getFeederTrackSize();

                    // move to edge bcs we are stuck
                    if (escaping && forcedDirection != 0) {
                        int target = currentPosition + forcedDirection;

                        boolean atEdge =
                                (forcedDirection == -1 && currentPosition == 0) ||
                                        (forcedDirection == 1 && currentPosition == trackSize - 1);

                        if (atEdge) {
                            escaping = false;
                            forcedDirection = 0;
                        } else if (station.trackIsVacant(target)) {
                            currentPosition = station.moveFeeder(
                                    currentPosition, target, currentCargo);
                            stuckCounter = 0;
                            continue;
                        } else {
                            escaping = false;
                            forcedDirection = 0;
                        }
                    }

                    int nextPosition = currentPosition;

                    // if loaded, decide whether to move left or right
                    if (currentCargo != null) {
                        int destIndex = (currentCargo.destination() - 1)
                                + station.getExitStart();
                        int direction = Integer.compare(destIndex, currentPosition);

                        if (direction > 0) {
                            if (station.trackIsVacant(currentPosition + 1)) {
                                nextPosition = currentPosition + 1;
                            }
                        } else if (direction < 0) {
                            if (station.isLeftNeighborLoaded(currentPosition)) {
                                if (currentPosition < trackSize - 1)
                                    nextPosition = currentPosition + 1;
                            } else {
                                nextPosition = currentPosition - 1;
                            }
                        }
                    }

                    // if empty, check whether someone will try to move past me
                    else {
                        boolean leftNeighborLoaded = station.isLeftNeighborLoaded(currentPosition);
                        boolean rightNeighborLoaded = station.isRightNeighborLoaded(currentPosition);

                        // if someone is coming, move to right
                        if (leftNeighborLoaded) {
                            if (currentPosition < trackSize - 1
                                    && station.trackIsVacant(currentPosition + 1)) {
                                nextPosition = currentPosition + 1;
                            }
                        }
                        // if right side is occupied, move left
                        else if (rightNeighborLoaded) {
                            if (currentPosition > 0
                                    && station.trackIsVacant(currentPosition - 1)) {
                                nextPosition = currentPosition - 1;
                            }
                        }
                        // if no one is coming, get cargo
                        else {
                            int target = findNearestCargo();

                            // move towards target
                            if (currentPosition < target)
                                nextPosition = currentPosition + 1;
                            else if (currentPosition > target)
                                nextPosition = currentPosition - 1;
                        }
                    }

                    // move
                    int oldPos = currentPosition;
                    currentPosition = station.moveFeeder(
                            currentPosition, nextPosition, currentCargo);

                    // we are stuck
                    if (currentPosition == oldPos) {
                        stuckCounter++;
                        if (stuckCounter >= 3 && !escaping) {
                            if (!station.isOccupied(currentPosition - 1)) {
                                forcedDirection = -1;
                                escaping = true;
                            } else if (!station.isOccupied(currentPosition + 1)) {
                                forcedDirection = 1;
                                escaping = true;
                            }
                        }
                    } else { // we are not stuck
                        stuckCounter = 0;
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private int findNearestCargo() {
        for (int i = 0; i < station.getEntryRampSize(); i++) {
            if (station.hasEntryCargo(i)) {
                return station.getEntryStart() + i;
            }
        }
        return station.getEntryStart();
    }
}