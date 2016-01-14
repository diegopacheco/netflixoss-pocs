package com.github.diegopacheco.netflix.pocs.ribbon.cache;

import com.netflix.ribbon.ServerError;
import com.netflix.ribbon.UnsuccessfulResponseException;
import com.netflix.ribbon.http.HttpResponseValidator;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;

public class ServiceResponseValidator implements HttpResponseValidator {
    @Override
    public void validate(HttpClientResponse<ByteBuf> response) throws UnsuccessfulResponseException, ServerError {
        if (response.getStatus().code() != 200) {
            throw new UnsuccessfulResponseException("ERROR - HTTP status code " + response.getStatus());
        }
    }
}
