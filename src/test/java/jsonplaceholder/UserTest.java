package jsonplaceholder;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class UserTest {

    @Test
    public void createNewUser(){

        User user = new User();
        user.setName("Marcin Test");
        user.setUsername("Marcin");
        user.setEmail("email@email.pl");
        user.setPhone("123123");
        user.setWebsite("www.test123.com");

        Geo geo = new Geo();
        geo.setLat("-123");
        geo.setLng("432");

        Address address = new Address();
        address.setStreet("Somestreet");
        address.setSuite("Suit 1");
        address.setCity("SomeCity");
        address.setZipcode("11-111");
        address.setGeo(geo);

        user.setAddress(address);

        Company company = new Company();
        company.setName("SomeCompany");
        company.setCatchPhrase("Some company is the best");
        company.setBs("BS test");

        user.setCompany(company);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("https://jsonplaceholder.typicode.com/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo(user.getName());

    }
}
