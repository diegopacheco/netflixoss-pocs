package com.github.diego.pacheco.sandbox.java.spring.boot.gatling.st

import scala.concurrent.duration.DurationInt

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import scala.collection.immutable.Map

class PingPongHeader extends Simulation {
   
   val httpConf = http
      .baseURL("http://127.0.0.1:8080")
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .acceptEncodingHeader("gzip, deflate")
      .disableFollowRedirect
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

      val scn = scenario("Call get a token get out come back and be happy :-) ")
         .exec(http("give me, please")
                 .post("/giveme")
                 .check(status.is(200))
                 .check(header("Token").transform( s => s.replace("token-", "")).saveAs("token"))
         )
         .exec(http("call other method ")
                 .get("/token/${token}")
                 .check(status.is(200))
         )

      setUp(
         scn.inject(constantUsersPerSec(1).during(60 seconds))
      ).protocols(httpConf).assertions(global.responseTime.max.lessThan(1000))

  }
