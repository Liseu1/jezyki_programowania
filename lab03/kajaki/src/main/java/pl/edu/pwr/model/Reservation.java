package pl.edu.pwr.model;

public class Reservation {
    private final int id;
    private final int organizerId;
    private final int offerId;
    private final int clientId;
    private int slots;
    private ReservationStatus status;
    
    public Reservation(int id, int organizerId, int offerId, int clientId, int slots, ReservationStatus status) {
        this.id = id;
        this.organizerId = organizerId;
        this.offerId = offerId;
        this.clientId = clientId;
        this.slots = slots;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public int getOfferId() {
        return offerId;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
