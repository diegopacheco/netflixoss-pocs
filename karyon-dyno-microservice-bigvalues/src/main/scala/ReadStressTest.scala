import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ReadStressTest extends Simulation {
  
  val httpConf = http
    .baseURL("http://localhost:6002/cache")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  
  val scn = scenario("Stress_Test_Read")
    .exec(http("read")
    .get("/get/k1"))
    .pause(5)

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))
  
}
