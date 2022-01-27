package br.ce.wcaquino.rest.tests.refact;

import static br.ce.wcaquino.rest.utils.BarrigaUtils.getIdContaPeloNome;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;

public class ContasTest extends BaseTest{
    
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
}
