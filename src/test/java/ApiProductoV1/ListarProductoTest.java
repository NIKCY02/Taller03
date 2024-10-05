package ApiProductoV1;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ApiProductoV1.config.ApiConfig;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Requisito 4. Listar productos usando la api /api/v1/product/")
public class ListarProductoTest extends ApiConfig {

    @BeforeEach
    public void abrirEscenario() {
        OnStage.setTheStage(Cast.ofStandardActors());
    }

    @Test
    @DisplayName("Listar productos de manera exitosa")
    public void listarProductos() {

        OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));

        OnStage.theActorInTheSpotlight().attemptsTo(
            Get.resource("/api/v1/product/")
                .with(request -> request
                    .contentType(ContentType.JSON)
                    .log().all()
                )
        );

        OnStage.theActorInTheSpotlight().should(
            ResponseConsequence.seeThatResponse("El código de respuesta es 200", response -> {
                response.statusCode(200);
            })
        );

        OnStage.theActorInTheSpotlight().should(
            ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero",
                response -> response.body("status", equalTo(true))
            )
        );
    }
    
    //SAD PATH
    @Test
    @DisplayName("Listar producto sad path")
    public void listarproductosad() {
    	OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));

        String skuTemporal = "b941487e-a5da-4418-9829-8b3306ff539d";
        
        OnStage.theActorInTheSpotlight().attemptsTo(
                Get.resource("/api/v1/product/" + skuTemporal + "/")
                    .with(request -> request
                    		.contentType(ContentType.JSON)
                            .log().all()));  
        
        OnStage.theActorInTheSpotlight().should(
                ResponseConsequence.seeThatResponse("El código de respuesta es 404", response -> {
                    response.statusCode(404);
                })
            );

         OnStage.theActorInTheSpotlight().should(
                ResponseConsequence.seeThatResponse("El valor del atributo status debe ser falso",
                    response -> response.body("status", equalTo(false))
                )
            );
    }
}





