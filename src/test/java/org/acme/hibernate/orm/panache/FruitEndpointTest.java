package org.acme.hibernate.orm.panache;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
/**
 * 
 */
@QuarkusTest
public class FruitEndpointTest {

	@Test
	public void testListAllFruits() {
		// List All
		Response response = RestAssured.given()
				.when()
				.get("/fruits")
				.then()
				.statusCode(200)
				.contentType(MediaType.APPLICATION_JSON)
				.extract().response();
		System.out.println(response.jsonPath().getList("name"));
		assertArrayEquals(new String[]{"Apple", "Banana", "Cherry"}, response.jsonPath().getList("name").toArray());
		//{"Cherry", "Apple", "Banana"} ,
	}
}
