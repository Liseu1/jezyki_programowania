package pl.edu.pwr.model;

import java.time.LocalDateTime;

public class Offer {
    private final int id;
    private LocalDateTime term;
    private String location;
    private int maxSlots;
    private int vacantSlots;
    private int organizerId;

    public Offer(int id, LocalDateTime term, String location, int maxSlots, int vacantSlots, int organizerId) {
        this.id = id;
        this.term = term;
        this.location = location;
        this.maxSlots = maxSlots;
        this.vacantSlots = vacantSlots;
        this.organizerId = organizerId;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTerm() {
        return term;
    }

    public void setTerm(LocalDateTime term) {
        this.term = term;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getVacantSlots() {
        return vacantSlots;
    }

    public void setVacantSlots(int vacantSlots) {
        this.vacantSlots = vacantSlots;
    }

    public int getOrganizerId() {
        return organizerId;
    }
}
