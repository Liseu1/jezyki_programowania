package pl.edu.pwr.util;

import java.time.LocalDate;

public class TimeSimulator {
    private static LocalDate simulatedDate = LocalDate.of(2025, 7, 1);

    public static LocalDate getSimulatedDate() {
        return simulatedDate;
    }

    public static void advanceDay() {
        simulatedDate = simulatedDate.plusDays(1);
    }

    public static void setSimulatedDate(LocalDate simulatedDate) {
        TimeSimulator.simulatedDate = simulatedDate;
    }
}
