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

public class SaldoTest extends BaseTest{

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
    public void deveCalcularSaldoContas(){    
        
        Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");

        given()          
            .when()
                .get("/saldo")
            .then()
                .statusCode(200)
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))            
            ;
    }

    public Integer getIdContaPeloNome(String nome){        
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
}
