package issue26748;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.jwt.build.Jwt;

@QuarkusTest
public class GraphqlResourceTest {

    static String tokenUser1;
    static String tokenUser2;

    @BeforeAll
    static void generateTokens() {
        tokenUser1 = generateJwtFor("user1");
        tokenUser2 = generateJwtFor("user2");
    }

    @Test

    public void testHelloEndpoint() {
        executeGraphqlRequest(tokenUser1, "user1");
        executeGraphqlRequest(tokenUser2, "user2");
        executeGraphqlRequest(tokenUser2, "user2");
    }

    private void executeGraphqlRequest(String token, String expectedSubject) {
        given().body(
                "{\"query\":\"{\n  whoami {\n    jwtSubFromService\n    jwtSubFromGraphqlResource\n  }\n}\",\"variables\":null}")
                .with().header("Authorization", "Bearer " + token)
                .log().all(true)
                .when().post("/graphql")

                .then()
                .log().all(true)
                .statusCode(200)
                .body("data.whoami.jwtSubFromService", equalTo(expectedSubject))
                .body("data.whoami.jwtSubFromGraphqlResource", equalTo(expectedSubject));
    }

    private static String generateJwtFor(String subjectName) {
        String token = Jwt.issuer("https://example.com/issuer")
                .upn(subjectName + "@quarkus.io")
                .subject(subjectName)
                .groups(new HashSet<>(Arrays.asList("User", "Admin")))

                .sign();
        return token;
    }
}
