import config.BaseApi;
import io.restassured.http.ContentType;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static config.DefaultValues.doctorId;
import static config.DefaultValues.scheduleId;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleTest extends BaseApi {

    @Test
    public void step_1_shouldCreateSchedule() {
        given()
            .body(
            "{" +
                " \"id\": \"" + scheduleId + "\"," +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"21:00:00\"," +
                " \"finishAt\": \"22:00:00\"," +
                " \"dayOfWeek\": \"WEDNESDAY\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(201);
    }

    @Test
    public void step_2_1_shouldConflictSchedule() {
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"21:00:00\"," +
                " \"finishAt\": \"22:00:00\"," +
                " \"dayOfWeek\": \"WEDNESDAY\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
    }

    @Test
    public void step_2_2_shouldConflictSchedule() {
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"20:30:00\"," +
                " \"finishAt\": \"21:30:00\"," +
                " \"dayOfWeek\": \"WEDNESDAY\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
    }

    @Test
    public void step_2_3_shouldConflictSchedule() {
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"21:30:00\"," +
                " \"finishAt\": \"22:30:00\"," +
                " \"dayOfWeek\": \"WEDNESDAY\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
    }

    @Test
    public void step_2_4_shouldConflictSchedule() {
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"20:00:00\"," +
                " \"finishAt\": \"23:00:00\"," +
                " \"dayOfWeek\": \"WEDNESDAY\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
    }

    @Test
    public void step_3_shouldGetAllSchedule() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule")
        .then()
            .statusCode(200);
    }

    @Test
    public void step_4_shouldGetSchedule() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule/" + scheduleId)
        .then()
            .statusCode(200)
            .body("doctorName", equalTo("name"))
            .body("doctorId", equalTo(doctorId))
            .body("startAt", equalTo("21:00:00"))
            .body("finishAt", equalTo("22:00:00"))
            .body("dayOfWeek", equalTo("WEDNESDAY"))
            .body("slotTime", equalTo(30));
    }

    @Test
    public void step_5_shouldDeleteSchedule() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("schedule/" + scheduleId)
        .then()
            .statusCode(204);
    }

    @Test
    public void step_5_shouldNotFoundSchedule() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule/" + scheduleId)
        .then()
            .statusCode(404);
    }
}
