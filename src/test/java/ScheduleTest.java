import config.BaseApi;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utils.MongoUtils.createSchedule;
import static utils.MongoUtils.deleteSchedule;

public class ScheduleTest extends BaseApi {

    @Test
    public void shouldCreateSchedule() {
        String doctorId = UUID.randomUUID().toString();
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"21:00:00\"," +
                " \"finishAt\": \"22:00:00\"," +
                " \"dayOfWeek\": \"" + LocalDate.now().plusDays(1).getDayOfWeek() + "\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(201);

        deleteSchedule(doctorId);
    }

    @Test
    public void shouldConflictSchedule() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{" +
                " \"doctorName\": \"name\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"18:00:00\"," +
                " \"finishAt\": \"19:00:00\"," +
                " \"dayOfWeek\": \"" + LocalDate.now().plusDays(1).getDayOfWeek() + "\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldConflictSchedule_1() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{" +
                " \"doctorName\": \"dr strange\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"17:30:00\"," +
                " \"finishAt\": \"18:30:00\"," +
                " \"dayOfWeek\": \"" + LocalDate.now().plusDays(1).getDayOfWeek() + "\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldConflictSchedule_2() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{" +
                " \"doctorName\": \"dr strange\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"18:30:00\"," +
                " \"finishAt\": \"19:30:00\"," +
                " \"dayOfWeek\": \"" + LocalDate.now().plusDays(1).getDayOfWeek() + "\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldConflictSchedule_3() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .body(
            "{" +
                " \"doctorName\": \"dr strange\"," +
                " \"doctorId\": \"" + doctorId + "\"," +
                " \"startAt\": \"17:00:00\"," +
                " \"finishAt\": \"20:00:00\"," +
                " \"dayOfWeek\": \"" + LocalDate.now().plusDays(1).getDayOfWeek() + "\"," +
                " \"slotTime\": 30" +
                " }"
            )
            .contentType(ContentType.JSON)
        .when()
            .post("schedule")
        .then()
            .statusCode(409);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldGetAllSchedule() {
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, UUID.randomUUID().toString());
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule")
        .then()
            .statusCode(200);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldGetSchedule() {
        String scheduleId = UUID.randomUUID().toString();
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, scheduleId);
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule/" + scheduleId)
        .then()
            .statusCode(200)
            .body("doctorName", equalTo("dr strange"))
            .body("doctorId", equalTo(doctorId))
            .body("startAt", equalTo("18:00:00"))
            .body("finishAt", equalTo("19:00:00"))
            .body("dayOfWeek", equalTo(LocalDate.now().plusDays(1).getDayOfWeek().toString()))
            .body("slotTime", equalTo(30));
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldDeleteSchedule() {
        String scheduleId = UUID.randomUUID().toString();
        String doctorId = UUID.randomUUID().toString();
        createSchedule(doctorId, scheduleId);
        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("schedule/" + scheduleId)
        .then()
            .statusCode(204);
        deleteSchedule(doctorId);
    }

    @Test
    public void shouldNotFoundSchedule() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("schedule/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }
}
