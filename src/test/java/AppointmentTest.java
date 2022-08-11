import config.BaseApi;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static config.DefaultValues.appointmentDateTime;
import static config.DefaultValues.appointmentId;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static utils.MongoUtils.createAppointment;
import static utils.MongoUtils.createSchedule;
import static utils.MongoUtils.deleteAppointment;
import static utils.MongoUtils.deleteSchedule;


public class AppointmentTest extends BaseApi {

    @Test
    public void shouldCreateAppointment() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{ " +
                " \"id\": \"" + UUID.randomUUID() + "\"," +
                " \"doctorName\": \"dr strange\"," +
                " \"doctorId\":\""+ doctorId +"\"," +
                " \"appointmentDate\": \"" + LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18,30)) + "\"," +
                " \"patientName\": \"pat\"," +
                " \"patientId\": \"333\"" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("appointment")
        .then()
            .statusCode(201);
        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shouldNotCreateAppointmentWhenConflict() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        createAppointment(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{ " +
                " \"doctorName\": \"dr strange\"," +
                " \"doctorId\":\""+ doctorId +"\"," +
                    " \"appointmentDate\": \"" + LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(16,0)) + "\"," +
                " \"patientName\": \"pat\"," +
                " \"patientId\": \"333\"" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("appointment")
        .then()
            .statusCode(400);
        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shouldGetAllAppointments() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        createAppointment(doctorId, UUID.randomUUID().toString());
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment")
        .then()
            .statusCode(200);
        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shouldGetAppointment() {
        String doctorId = UUID.randomUUID().toString();
        String appointmentId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        createAppointment(doctorId, appointmentId);
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment/" + appointmentId)
        .then()
                .body("doctorName", equalTo("dr strange"))
                .body("doctorId", equalTo(doctorId))
                .body("appointmentDate", containsString(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18,30)).toString()))
                .body("patientName", equalTo("pat"))
                .body("patientId", equalTo("333"))
            .statusCode(200);

        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shoulDeleteAppointment() {
        String doctorId = UUID.randomUUID().toString();
        String appointmentId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        createAppointment(doctorId, appointmentId);
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("appointment/" + appointmentId)
        .then()
            .statusCode(204);
        deleteSchedule(doctorId);
        deleteAppointment(doctorId);
    }

    @Test
    public void shouldNotFoundAppointment() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }
}
