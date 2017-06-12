package com.github.diegopacheco.sandbox.java.netflixoss.karyon;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rx.Observable;

public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public Observable<Boolean> authenticate(HttpServerRequest<ByteBuf> request) {
        return Observable.just(Boolean.TRUE);
    }
}