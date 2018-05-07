package com.github.diegopacheco.sandbox.java.netflix.pocs.karyon.eureka.client.rest.ex;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MyMyExceptionMapper implements ExceptionMapper<DivisionByZeroException>{
		
  public static class Error {
    public String key;
    public String message;
  }
	
	 @Override
   public Response toResponse(DivisionByZeroException exception) {
       Error error = new Error();
       error.key = "division-by-zero";
       error.message = exception.getMessage();
       return Response.status(Status.BAD_REQUEST).entity(error).build();
   }
	
}
