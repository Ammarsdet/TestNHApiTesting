package test;


import java.io.File;

import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class PetStoreApiTest {
	
	int catId;
	int petId;
	
	@BeforeTest
	public void setup() {
		
		RestAssured.baseURI = "https://petstore.swagger.io/v2";
	}

	@Test (dependsOnMethods = "postAPet")

	public void getPetById() {
		RestAssured
				.given().accept(ContentType.JSON).when().get("/pet/33312718")
				.then().statusCode(200);

	}
	
	@Test
	public void findByStatus() {
		
		RestAssured
		.given().accept(ContentType.JSON).contentType("application/json").param("status", "pending")
		.when().get("/pet/findByStatus")
		.then().statusCode(200).contentType("application/json");
		
	}
	
	@Test (dependsOnMethods = {"postACat", "updateCat"})
	public void getByID() {
		
	Response myResponse = 	RestAssured
		.given().accept(ContentType.JSON).when().get("/pet/33312717");
	
	myResponse.prettyPrint();
	
	myResponse.then()
	.assertThat().statusCode(200).and().contentType("application/json");
	
	String petName = myResponse.path("name");
	System.out.println("Pet name is: " + petName);
	Assert.assertEquals(petName, "Amber");
	
	int petId = myResponse.path("id");
	System.out.println("Pet id is: " + petId);
	Assert.assertEquals(petId, 33312717);
	
	int categoryId = myResponse.path("category.id");
	System.out.println("Pet category id is: " + categoryId);
	Assert.assertEquals(categoryId, 737);
	
	
	int petTagId = myResponse.path("tags[0].id");
	System.out.println("pet Tag id is: " + petTagId);
	Assert.assertEquals(petTagId, 15);
	
	String secondObjpetTagNmae = myResponse.path("tags[1].name");
	System.out.println("pet name of second tag is: " + secondObjpetTagNmae);
	Assert.assertEquals(secondObjpetTagNmae, "Anatolian");
	
	//using jsonpath prefered more because its clearer. 
	
	String categoeryName = myResponse.jsonPath().get("category.name");
	System.out.println("Category name is: " + categoeryName);
	Assert.assertEquals(categoeryName, "cat");
	
	String catStatus = myResponse.body().jsonPath().get("status");
	Assert.assertEquals(catStatus, "pending");
		
	}
	
	@Test
	public void postACat() {
		
		String catRequestBody = "{\n"
				+ "  \"id\": 33312717,\n"
				+ "  \"category\": {\n"
				+ "    \"id\": 737,\n"
				+ "    \"name\": \"cat\"\n"
				+ "  },\n"
				+ "  \"name\": \"Amber\",\n"
				+ "  \"photoUrls\": [\n"
				+ "    \"string\"\n"
				+ "  ],\n"
				+ "  \"tags\": [\n"
				+ "    {\n"
				+ "      \"id\": 15,\n"
				+ "      \"name\": \"persian\"\n"
				+ "    },\n"
				+ "        {\n"
				+ "      \"id\": 13,\n"
				+ "      \"name\": \"Anatolian\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"status\": \"available\"\n"
				+ "}";
		
	Response myResponse =	RestAssured
		.given().accept(ContentType.JSON).contentType("application/json")
		.body(catRequestBody)
		.when().post("/pet");
	
		myResponse.then().statusCode(200).and().contentType("application/json");
		
		catId = myResponse.jsonPath().get("id");
	}
	
	//update cat status
	
	@Test (dependsOnMethods = "postACat")
	public void updateCat() {
		
		String catPutBody = "{\n"
				+ "  \"id\": 33312717,\n"
				+ "  \"category\": {\n"
				+ "    \"id\": 737,\n"
				+ "    \"name\": \"cat\"\n"
				+ "  },\n"
				+ "  \"name\": \"Amber\",\n"
				+ "  \"photoUrls\": [\n"
				+ "    \"string\"\n"
				+ "  ],\n"
				+ "  \"tags\": [\n"
				+ "    {\n"
				+ "      \"id\": 15,\n"
				+ "      \"name\": \"persian\"\n"
				+ "    },\n"
				+ "        {\n"
				+ "      \"id\": 13,\n"
				+ "      \"name\": \"Anatolian\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"status\": \"pending\"\n"
				+ "}";
		
	Response catResponse =	RestAssured
		.given().accept(ContentType.JSON).contentType("application/json")
		.body(catPutBody)
		.when().put("/pet");
	
		catResponse.then().statusCode(200).and().contentType("application/json");
		Assert.assertEquals(catResponse.jsonPath().get("status"), "pending");
				
		
	}
	
	//RestAssured chain validation 
	
	
	
	@Test
	public void postAPet(){
	
		String requestBody = "{\n"
				+ "  \"id\": 33312718,\n"
				+ "  \"category\": {\n"
				+ "    \"id\": 777,\n"
				+ "    \"name\": \"dog\"\n"
				+ "  },\n"
				+ "  \"name\": \"BOLO\",\n"
				+ "  \"photoUrls\": [\n"
				+ "    \"string\"\n"
				+ "  ],\n"
				+ "  \"tags\": [\n"
				+ "    {\n"
				+ "      \"id\": 11,\n"
				+ "      \"name\": \"GermanShepered\"\n"
				+ "    },\n"
				+ "        {\n"
				+ "      \"id\": 10,\n"
				+ "      \"name\": \"Husky\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"status\": \"available\"\n"
				+ "}";
		
	Response myResponse =	RestAssured
		.given().accept(ContentType.JSON).contentType("application/json").body(requestBody)
		.when().post("/pet");
	
	myResponse.then().statusCode(200).and().contentType("application/json");
	
	myResponse.prettyPrint();
	
	petId = myResponse.jsonPath().get("id");
		
		
	}
	
 	public void deleteAPet() {
		
		Response deleteResponse= RestAssured
		.given().accept(ContentType.JSON).contentType("application/json")
		.when().delete("/pet/" + petId);
		
		deleteResponse.then().statusCode(200).and().contentType("application/json");
		Assert.assertEquals(deleteResponse.body().jsonPath().get("message"), String.valueOf(petId));
		
	}
	
	
     	public void deleteCat() {
		
		Response deleteResponse= RestAssured
		.given().accept(ContentType.JSON).contentType("application/json")
		.when().delete("/pet/" + catId);
		
		deleteResponse.then().statusCode(200).and().contentType("application/json");
		Assert.assertEquals(deleteResponse.body().jsonPath().get("message"), String.valueOf(catId));
		
	}
     	
     	//create a cat with json file
     	
     	@Test
     	public void createCatWithJsonFile() {
    		
    		File catRequestBodyFile = new File("./src/test/resources/JsonTestData/postCat.json");
    		
    		Response myResponse = 
    				RestAssured
    				.given().accept(ContentType.JSON).contentType("application/json")
    				.body(catRequestBodyFile)
    				.when().post("/pet");
    		
    		myResponse.then().statusCode(200).and().contentType("application/json");
    		myResponse.prettyPrint();
    		
    		catId = myResponse.jsonPath().get("id");
     		    		
     	}
     	
     	@Test
    	public void chainValidation(){
    		
     		File catRequestBodyFile = new File("./src/test/resources/JsonTestData/postCat.json");
    		
    		Response myResponse = 
    				RestAssured
    				.given().accept(ContentType.JSON).contentType("application/json")
    				.body(catRequestBodyFile)
    				.when().post("/pet");
    		
    		myResponse.then().assertThat().statusCode(200)
    		.and().assertThat().contentType("application/json")
    		.and().assertThat().body("id", equalTo(111236))
    		.and().assertThat().body("category.id", equalTo(737))
    		.and().assertThat().body("category.name", equalTo("cat"))
    		.and().assertThat().body("name", equalTo("Amber"))
    		.and().assertThat().body("tags[0].id", equalTo(15))
    		.and().assertThat().body("tags[0].name", equalTo("persian"))
    		.and().assertThat().body("tags[1].id", equalTo(13))
    		.and().assertThat().body("tags[1].name", equalTo("Anatolian"))
    		.and().assertThat().body("status", equalTo("available"));
    		
    		myResponse.prettyPrint();
    		
    		catId = myResponse.jsonPath().get("id");
    	}
     	
	
	@AfterTest
	public void cleanup() {
		deleteCat();
		deleteAPet();
		
	}
	
	//negative test cases
	//invalid ID
	@Test
	public void invalidIdupdateCat() {
		
		String catPutBody = "{\n"
				+ "  \"id\": '33312717',\n"
				+ "  \"category\": {\n"
				+ "    \"id\": 737,\n"
				+ "    \"name\": \"cat\"\n"
				+ "  },\n"
				+ "  \"name\": \"Amber\",\n"
				+ "  \"photoUrls\": [\n"
				+ "    \"string\"\n"
				+ "  ],\n"
				+ "  \"tags\": [\n"
				+ "    {\n"
				+ "      \"id\": 15,\n"
				+ "      \"name\": \"persian\"\n"
				+ "    },\n"
				+ "        {\n"
				+ "      \"id\": 13,\n"
				+ "      \"name\": \"Anatolian\"\n"
				+ "    }\n"
				+ "  ],\n"
				+ "  \"status\": \"pending\"\n"
				+ "}";
		
	Response catResponse =	RestAssured
		.given().accept(ContentType.JSON).contentType("application/json")
		.body(catPutBody)
		.when().put("/pet");
	
		catResponse.then().statusCode(400).and().contentType("application/json");
		catResponse.prettyPrint();
		Assert.assertEquals(catResponse.jsonPath().get("message"), "bad input");	
		
	}
	
	
	
	
	
	
	
	



}
