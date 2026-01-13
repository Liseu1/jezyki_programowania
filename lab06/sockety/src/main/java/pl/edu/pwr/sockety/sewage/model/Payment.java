package pl.edu.pwr.sockety.sewage.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private final int tankerId;
    private final int volumePaid;
    private final LocalDateTime timestamp;

    public Payment(int tankerId, int volumePaid) {
        this.tankerId = tankerId;
        this.volumePaid = volumePaid;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] Cysterna #%d: Opłacono %d L",
                timestamp.format(formatter), tankerId, volumePaid);
    }
}