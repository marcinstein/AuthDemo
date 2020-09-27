package trello;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class OrganizationTest {

    private static final String KEY = "YOUR_KEY";
    private static final String TOKEN = "YOUR_TOKEN";

    private static Stream<Arguments> createOrganizationData() {
        return Stream.of(
                Arguments.of("Display Name", "Team description", "teamname", "https://website.team.com"),
                Arguments.of("Display Name", "Team description", "teamname", "http://website.team.com"),
                Arguments.of("Display Name", "Team description", "ttt", "http://website.team.com"),
                Arguments.of("Display Name", "Team description", "team_name", "http://website.team.com"),
                Arguments.of("Display Name", "Team description", "teamname123", "http://website.team.com"),
                Arguments.of("Display Name", "Team description", "teamname123", "fake://website.team.com")
        );
    }

    @DisplayName("Create organization with valid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website {3} ")
    @MethodSource("createOrganizationData")
    public void createOrganization(String displayname, String desc, String name, String website) {

        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .queryParam("displayName", displayname)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post("https://api.trello.com/1/organizations")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayname);

        final String organizationId = json.getString("id");

        given()
                .contentType(ContentType.JSON)
                .queryParam("key", KEY)
                .queryParam("token", TOKEN)
                .when()
                .delete("https://api.trello.com/1/organizations" + "/" + organizationId)
                .then()
                .statusCode(200);
    }


}
