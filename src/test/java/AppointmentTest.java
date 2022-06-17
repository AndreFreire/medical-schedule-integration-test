import config.BaseApi;
import io.restassured.http.ContentType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static config.DefaultValues.appointmentDateTime;
import static config.DefaultValues.appointmentId;
import static config.DefaultValues.doctorId;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentTest extends BaseApi {

    @Test
    public void step_1_shouldCreateAppointment() {
        given()
            .body(
            "{ " +
                " \"id\": \"" + appointmentId + "\"," +
                " \"doctorName\": \"dr\"," +
                " \"doctorId\":\""+ doctorId +"\"," +
                " \"appointmentDate\": \"" + appointmentDateTime.plusMinutes(30) + "\"," +
                " \"patientName\": \"pat\"," +
                " \"patientId\": \"333\"" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("appointment")
        .then()
            .statusCode(201);
    }

    @Test
    public void step_2_shouldNotCreateAppointmentWhenConflict() {
        given()
            .body(
            "{ " +
                " \"doctorName\": \"dr\"," +
                " \"doctorId\":\""+ doctorId +"\"," +
                " \"appointmentDate\": \"" + appointmentDateTime.plusMinutes(30) + "\"," +
                " \"patientName\": \"pat\"," +
                " \"patientId\": \"333\"" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("appointment")
        .then()
            .statusCode(400);
    }

    @Test
    public void step_3_shouldGetAllAppointments() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment")
        .then()
            .statusCode(200);
    }

    @Test
    public void step_4_shouldGetAppointment() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment/" + appointmentId)
        .then()
                .body("doctorName", equalTo("dr"))
                .body("doctorId", equalTo(doctorId))
                .body("appointmentDate", containsString(appointmentDateTime.plusMinutes(30).toString()))
                .body("patientName", equalTo("pat"))
                .body("patientId", equalTo("333"))
            .statusCode(200);
    }

    @Test
    public void step_5_shoulDeleteAppointment() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("appointment/" + appointmentId)
        .then()
            .statusCode(204);
    }

    @Test
    public void step_6_shouldNotFoundAppointment() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("appointment/" + appointmentId)
        .then()
            .statusCode(404);
    }
}
