package config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class DefaultValues {
    public static final String doctorId = "999999999";
    public static final String appointmentId = UUID.randomUUID().toString();
    public static final String scheduleId = UUID.randomUUID().toString();
    private static final LocalDate today = LocalDate.now();
    public static final LocalDateTime appointmentDateTime = LocalDateTime.of(today, LocalTime.of(16,0));
}
