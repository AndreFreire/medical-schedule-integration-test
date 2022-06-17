import config.BaseApi;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static config.DefaultValues.doctorId;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimetableTest extends BaseApi {

    @Test
    public void shouldGetTimetable() {
        given()
            .contentType(ContentType.JSON)
            .param("doctorId", doctorId)
        .when()
            .get("timetable")
        .then()
            .body("[0].doctorName", equalTo("dr strange"))
            .body("[0].doctorId", equalTo("999999999"))
            .body("[0].startAt", containsString(LocalDateTime.of(LocalDate.now(), LocalTime.of(14,0)).toString()))

            .body("[1].doctorName", equalTo("dr strange"))
            .body("[1].doctorId", equalTo("999999999"))
            .body("[1].startAt", containsString(LocalDateTime.of(LocalDate.now(), LocalTime.of(15,0)).toString()))

                //14:30 have appointment

            .body("[2].doctorName", equalTo("dr strange"))
            .body("[2].doctorId", equalTo("999999999"))
            .body("[2].startAt", containsString(LocalDateTime.of(LocalDate.now(), LocalTime.of(15,30)).toString()));
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
