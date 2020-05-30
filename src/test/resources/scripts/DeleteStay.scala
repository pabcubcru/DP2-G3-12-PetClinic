package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteStay extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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

    object ShowOwner2 {
        val showOwner2 = exec(http("ShowOwner2")
            .get("/owners/2")
            .headers(headers_0))
        .pause(3)
    }

	object DeleteStaySuccess {
		val deleteStaySuccess = exec(http("DeleteStaySuccess")
			.get("/owners/2/pets/2/stays/5/delete")
			.headers(headers_0))
		.pause(4)
	}

	object ShowOwner10 {
        val showOwner10 = exec(http("ShowOwner10")
            .get("/owners/10")
            .headers(headers_0))
        .pause(3)
    }

	object DeleteStayError {
		val deleteStayError = exec(http("DeleteStayError")
			.get("/owners/10/pets/13/stays/8/delete")
			.headers(headers_0))
		.pause(4)
	}

	val positiveScn = scenario("DeleteStaySuccess").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner2.showOwner2, DeleteStaySuccess.deleteStaySuccess)
    val negativeScn = scenario("DeleteStayError").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner10.showOwner10, DeleteStayError.deleteStayError)

	setUp(
        negativeScn.inject(rampUsers(5000) during (100 seconds)), 
        positiveScn.inject(rampUsers(5000) during (100 seconds))
    ).protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95))
}