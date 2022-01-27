package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTest extends BaseTest{

    @BeforeClass
    public static void login(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "ts@ts.com");
        login.put("senha", "ts123456");

        String TOKEN = given()
            .body(login)
        .when()
            .post("/signin")
        .then()
            .statusCode(200)
            .extract().path("token")
        ;

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);

        RestAssured.get("/reset").then().statusCode(200);
    }
    
    @Test
    public void naoDeveAcessarAPISemToken(){

        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401)
        ;
    }

    public Integer getIdContaPeloNome(String nome){        
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
}
