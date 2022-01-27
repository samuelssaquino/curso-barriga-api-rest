package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.Movimentacao;
import br.ce.wcaquino.rest.utils.DataUtils;
import io.restassured.RestAssured;

public class ContasTest extends BaseTest{

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
    public void deveIncluirContaComSucesso(){       

        given()
            .body("{\"nome\": \"Conta Inserida\"}")
        .when()
            .post("/contas")
        .then()
            .statusCode(201)  
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){       
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");

        given()
            .body("{\"nome\": \"Conta alterada\"}")
            .pathParam("id", CONTA_ID)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveIncluirContaComNomeRepetido(){       

        given()
            .body("{\"nome\": \"Conta mesmo nome\"}")
        .when()
            .post("/contas")
        .then()
            .statusCode(400)
            .body("error", is("Já existe uma conta com esse nome!"))

        ;
    }

    public Integer getIdContaPeloNome(String nome){        
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
}
