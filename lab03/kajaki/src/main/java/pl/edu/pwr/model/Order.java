package pl.edu.pwr.model;

public class Order {
    private final int id;
    private final int organizerId;
    private final int employeeId;
    private final int offerId;
    private OrderStatus status;

    public Order(int id, int organizerId, int employeeId, int offerId, OrderStatus status) {
        this.id = id;
        this.organizerId = organizerId;
        this.employeeId = employeeId;
        this.offerId = offerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public int getOfferId() {
        return offerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
