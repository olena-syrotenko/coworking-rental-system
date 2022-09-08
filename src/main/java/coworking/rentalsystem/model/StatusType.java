package coworking.rentalsystem.model;

public enum StatusType {
    NEW (1),
    CONFIRMED (2),
    REJECTED (3),
    CANCELED (4),
    PAID (5);

    private int id;

    StatusType(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
