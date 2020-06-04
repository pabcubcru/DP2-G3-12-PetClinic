package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EndStay extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
        "Proxy-Connection" -> "keep-alive",
        "Upgrade-Insecure-Requests" -> "1")

    val headers_2 = Map(
        "Origin" -> "http://www.dp2.com/",
        "Proxy-Connection" -> "keep-alive",
        "Upgrade-Insecure-Requests" -> "1")

object Home {
        val home = exec(http("Home")
            .get("/")
            .headers(headers_0))
        .pause(46)
    }

object Login {
    val login = exec(http("LoginForm")
            .get("/login")
            .headers(headers_0)
            .check(css("input[name=_csrf]", "value").saveAs("stoken")))
        .pause(17)
        .exec(http("LoggedAsAdmin")
            .post("/login")
            .headers(headers_2)
            .formParam("username", "admin1")
            .formParam("password", "4dm1n")
            .formParam("_csrf", "${stoken}"))
        .pause(16)
}

object FindOwners {
        val findOwners = exec(http("FindOwners")
            .get("/owners/find")
            .headers(headers_0))
        .pause(8)
    }

object FindOwnersList {
	    val findOwnersList = exec(http("FindOwnersList")
            .get("/owners?lastName=")
            .headers(headers_0))
        .pause(8)
    }

object ShowOwner {
		val showOwner = exec(http("ShowOwner")
			.get("/owners/10")
			.headers(headers_0))
		.pause(9)
}

object EndStaySuccess {
		val endStaySuccess = exec(http("EndStaySuccess")
			.get("/owners/10/pets/13/stays/8/end")
			.headers(headers_0))
			.pause(9)
}

object EndStayError {
		val endStayError = exec(http("EndStayError")
			.get("/owners/10/pets/13/stays/7/end")
			.headers(headers_0))
			.pause(9)
}


	val positiveScn = scenario("EndStaySuccess").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner.showOwner, EndStaySuccess.endStaySuccess)
    val negativeScn = scenario("EndStayError").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner.showOwner, EndStayError.endStayError)

    setUp(
        negativeScn.inject(rampUsers(7000) during (100 seconds)), 
        positiveScn.inject(rampUsers(7000) during (100 seconds))
    ).protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95))
}