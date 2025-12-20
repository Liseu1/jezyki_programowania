package pl.edu.pwr.watki.model;

public class Station {

    private final Cargo[] entryRamp;
    private final Cargo[] exitRamp;
    private final Cargo[] feeder;
    private StationListener listener;
    private final boolean[] feederPresent;

    private final int entryOffset;
    private final int exitOffset;

    public Station(int entryRampSize, int exitRampSize, int threads) {
        this.entryRamp = new Cargo[entryRampSize];
        this.exitRamp = new Cargo[exitRampSize];

        int feederSize = exitRampSize + 2 * (threads - 1);
        this.feeder = new Cargo[feederSize];
        this.feederPresent = new boolean[feederSize];

        this.entryOffset = (feederSize - entryRampSize) / 2;
        this.exitOffset = (feederSize - exitRampSize) / 2;
    }

    public void setListener(StationListener listener) {
        this.listener = listener;
    }

    public int getEntryStart() { return entryOffset; }
    public int getExitStart() { return exitOffset; }
    public int getEntryRampSize() { return entryRamp.length; }
    public int getExitRampSize() { return exitRamp.length; }
    public int getFeederTrackSize() { return feeder.length; }

    public boolean hasEntryCargo(int rampIndex) {
        if (rampIndex < 0 || rampIndex >= entryRamp.length) return false;
        return entryRamp[rampIndex] != null;
    }

    public boolean trackIsVacant(int index) {
        return !isOccupied(index);
    }

    public boolean isLeftNeighborLoaded(int currentPos) {
        int idx = currentPos - 1;
        return idx >= 0 && feeder[idx] != null;
    }

    public boolean isRightNeighborLoaded(int currentPos) {
        int idx = currentPos + 1;
        return idx < feeder.length && feeder[idx] != null;
    }

    public boolean isOccupied(int index) {
        return index < 0 || index >= feeder.length || feederPresent[index];
    }

    synchronized public void addCargo(int index, Cargo cargo) {
        while(this.entryRamp[index] != null) {
            try {
                wait();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        this.entryRamp[index] = cargo;
        if (listener != null) listener.changeField(Ramp.ENTRY_RAMP, index, cargo, false);
        notifyAll();
    }

    synchronized public Cargo tryRemoveCargo(int index) {
        if (this.exitRamp[index] == null) return null;
        Cargo c = this.exitRamp[index];
        this.exitRamp[index] = null;
        if (listener != null) listener.changeField(Ramp.EXIT_RAMP, index, null, false);
        notifyAll();
        return c;
    }

    synchronized public void registerFeeder(int index) {
        feederPresent[index] = true;
        feeder[index] = null;
        if (listener != null) listener.changeField(Ramp.FEEDER, index, null, true);
    }

    synchronized public int moveFeeder(int currentPos, int nextPos, Cargo cargo) {
        if (currentPos == nextPos) return currentPos;
        if (isOccupied(nextPos)) return currentPos;

        feederPresent[currentPos] = false;
        feeder[currentPos] = null;
        if (listener != null) listener.changeField(Ramp.FEEDER, currentPos, null, false);

        feederPresent[nextPos] = true;
        feeder[nextPos] = cargo;
        if (listener != null) listener.changeField(Ramp.FEEDER, nextPos, cargo, true);

        notifyAll();
        return nextPos;
    }

    synchronized public Cargo tryPickUp(int feederPos) {
        int rampIndex = feederPos - entryOffset;
        if (rampIndex >= 0 && rampIndex < entryRamp.length) {
            if (entryRamp[rampIndex] != null) {
                Cargo cargo = entryRamp[rampIndex];
                entryRamp[rampIndex] = null;
                feeder[feederPos] = cargo;
                feederPresent[feederPos] = true;
                if (listener != null) {
                    listener.changeField(Ramp.ENTRY_RAMP, rampIndex, null, false);
                    listener.changeField(Ramp.FEEDER, feederPos, cargo, true);
                }
                notifyAll();
                return cargo;
            }
        }
        return null;
    }

    synchronized public boolean tryDrop(int feederPos, Cargo cargo) {
        int rampIndex = feederPos - exitOffset;
        if (rampIndex >= 0 && rampIndex < exitRamp.length) {
            if (exitRamp[rampIndex] == null && cargo.destination() == rampIndex + 1) {
                exitRamp[rampIndex] = cargo;
                feeder[feederPos] = null;
                feederPresent[feederPos] = true;
                if (listener != null) {
                    listener.changeField(Ramp.EXIT_RAMP, rampIndex, cargo, false);
                    listener.changeField(Ramp.FEEDER, feederPos, null, true);
                }
                notifyAll();
                return true;
            }
        }
        return false;
    }
}