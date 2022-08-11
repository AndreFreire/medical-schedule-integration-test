package utils;

import config.MongoConnection;
import org.bson.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.mongodb.client.model.Filters.eq;

public class MongoUtils {
    public static void createSchedule(String doctorId, String scheduleId) {
        MongoConnection.getScheduleCollection().insertOne(
                new Document("_id", scheduleId)
                        .append("doctorName", "dr strange")
                        .append("doctorId", doctorId)
                        .append("startAt", LocalTime.of(21, 0))
                        .append("finishAt", LocalTime.of(22, 0))
                        .append("dayOfWeek", LocalDate.now().plusDays(1).getDayOfWeek().toString())
                        .append("slotTime", 30));

    }

    public static void createAppointment(String doctorId, String appointmentId) {
        MongoConnection.getAppointmentCollection().insertOne(
                new Document("_id", appointmentId)
                        .append("doctorName", "dr strange")
                        .append("doctorId", doctorId)
                        .append("appointmentDate", LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(21,30)))
                        .append("patientName", "pat")
                        .append("patientId", "333"));


    }
    public static void deleteSchedule(String doctorId) {
        MongoConnection.getScheduleCollection().deleteMany(eq("doctorId", doctorId));
    }

    public static void deleteAppointment(String doctorId) {
        MongoConnection.getAppointmentCollection().deleteMany(eq("doctorId", doctorId));
    }
}
