package com.diegopacheco.github.sandbox.java.netflix.ribbon.template;

import io.netty.buffer.ByteBuf;

import com.netflix.ribbon.ResponseValidator;
import com.netflix.ribbon.ServerError;
import com.netflix.ribbon.UnsuccessfulResponseException;

public class ApiResponseValidator implements ResponseValidator<ByteBuf> {

	@Override
	public void validate(ByteBuf response) throws UnsuccessfulResponseException,ServerError {
		System.out.println("Validator: " + response);
	}

}
