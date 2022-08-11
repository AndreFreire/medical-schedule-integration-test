import config.BaseApi;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static utils.MongoUtils.createAppointment;
import static utils.MongoUtils.createSchedule;
import static utils.MongoUtils.deleteAppointment;
import static utils.MongoUtils.deleteSchedule;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimetableTest extends BaseApi {

    @Test
    public void shouldGetTimetable() {
        String doctorId = UUID.randomUUID().toString();
        String appointmentId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        createAppointment(doctorId, appointmentId);
        given()
            .contentType(ContentType.JSON)
            .param("doctorId", doctorId)
        .when()
            .get("timetable")
        .then()
            .body("[0].doctorName", equalTo("dr strange"))
            .body("[0].doctorId", equalTo(doctorId))
            .body("[0].startAt", containsString(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18,0)).toString()));

             //18:00 have appointment
        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shouldGetEmptyTimetable() {
        given()
            .contentType(ContentType.JSON)
            .param("doctorId", "123")
        .when()
            .get("timetable")
        .then()
            .statusCode(200)
            .body("$", Matchers.hasSize(0));
    }
}
