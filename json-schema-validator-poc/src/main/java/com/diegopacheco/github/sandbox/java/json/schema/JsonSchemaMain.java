package com.diegopacheco.github.sandbox.java.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

public class JsonSchemaMain {
	public static void main(String[] args) throws Throwable {
		JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
		System.out.println(jsonValidator);
		
		JsonNode draftv4 = JsonLoader.fromResource("/draftv4/schema");
		System.out.println(draftv4);
		
		ProcessingReport r1 = jsonValidator.validate(draftv4, draftv4);
		System.out.println(r1);
		
		ProcessingReport r2 = jsonValidator.validate(draftv4,JsonLoader.fromString("{ \"name\" : \"diego\" }"));
		System.out.println(r2);
		
		try{
			ProcessingReport r3 = jsonValidator.validate(draftv4,JsonLoader.fromString("{ \"name\" : % }"));
			System.out.println(r3);
		}catch(Exception e){
			System.out.println("Error: " + e);
		}
		
	}
}
