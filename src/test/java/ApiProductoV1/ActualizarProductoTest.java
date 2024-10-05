package ApiProductoV1;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ApiProductoV1.config.ApiConfig;
import ApiProductoV1.dto.ProductRequest;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Requisito 2. Actualizar un producto previamente creado usando la api /api/v1/product/")
public class ActualizarProductoTest extends ApiConfig{

	@BeforeEach
	public void abrirEscenario() {
		OnStage.setTheStage(Cast.ofStandardActors());
	}	
	
	@Test
	@DisplayName("Actualizar un producto previamente creado de manera exitosa")
	public void actualizarProducto() {
		ProductRequest nuevoProducto = ProductRequest.builder()
				.name("Iphone 3000")
				.description("Teelfono de alta gama")
				.price(3500.0f)
				.build();
		
		OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Post.to("/api/v1/product/")
					.with(
							request -> request
								.body(nuevoProducto).log().all()
					)
				);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 201",response -> response.statusCode(201))
		);				
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(true)))
				);		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto fue creado con éxito!"
						,response -> response.body("message", containsString("El producto fue creado con éxito!")))
				);
		
		String skuCreado = SerenityRest.lastResponse().jsonPath().getString("sku");
		
		nuevoProducto.setName("Iphone 3000 Actualizado");
		nuevoProducto.setDescription("Descripcion actualizada");
		nuevoProducto.setPrice(4000.0f);
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Put.to("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuCreado)
							.body(nuevoProducto)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 200",response -> response.statusCode(200))
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(true)))
				);	
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto fue actualizado con éxito"
						,response -> response.body("message", containsString("El producto fue actualizado con éxito")))
				);
		
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Get.resource("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuCreado)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 200",response -> response.statusCode(200))
		);
		
		OnStage.theActorInTheSpotlight().should(
				seeThat("El nombre del producto fue actualizado con exito",
					response -> SerenityRest.lastResponse().jsonPath().getString("products[0].name"),equalTo("Iphone 3000 Actualizado")),
				seeThat("La descripcion del producto fue actualizada con exito",
						response -> SerenityRest.lastResponse().jsonPath().getString("products[0].description"),equalTo("Descripcion actualizada")),
				seeThat("El precio del producto fue actualizado con exito",
						response -> SerenityRest.lastResponse().jsonPath().getFloat("products[0].price"),equalTo(4000.0f))
		);
		
	}
	
	//SAD PATH
	
	@Test
	@DisplayName("Actualizar un producto sad path")
	public void actualizarProductosad() {
		ProductRequest nuevoProductosad = ProductRequest.builder()
				.name("Iphone 4000")
				.description("Telefono de alta gama")
				.price(3800.0f)
				.build();
		
		OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Post.to("/api/v1/product/")
					.with(
							request -> request
								.body(nuevoProductosad).log().all()
					)
				);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 201",response -> response.statusCode(201))
		);				
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(true)))
				);		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto fue creado con éxito!"
						,response -> response.body("message", containsString("El producto fue creado con éxito!")))
				);
		
		String skuCreado = SerenityRest.lastResponse().jsonPath().getString("sku");
		String skuTemporal = "28666900-32ab-4ac6-a2a6-2c9cc6662001";
		
		nuevoProductosad.setName("Iphone 5000 Actualizado");
		nuevoProductosad.setDescription("Descripcion actualizada");
		nuevoProductosad.setPrice(7000.0f);
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Put.to("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuTemporal)
							.body(nuevoProductosad)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 400",response -> response.statusCode(400))
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser falso"
						,response -> response.body("status", equalTo(false)))
				);	
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto no fue encontrado"
						,response -> response.body("message", containsString("El producto no fue encontrado")))
				);
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Get.resource("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuCreado)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 200",response -> response.statusCode(200))
		);
		
		OnStage.theActorInTheSpotlight().should(
				seeThat("El nombre del producto fue actualizado con exito",
					response -> SerenityRest.lastResponse().jsonPath().getString("products[0].name"),equalTo("Iphone 4000")),
				seeThat("La descripcion del producto fue actualizada con exito",
						response -> SerenityRest.lastResponse().jsonPath().getString("products[0].description"),equalTo("Telefono de alta gama")),
				seeThat("El precio del producto fue actualizado con exito",
						response -> SerenityRest.lastResponse().jsonPath().getFloat("products[0].price"),equalTo(3800.0f))
		);
		
	}
}
