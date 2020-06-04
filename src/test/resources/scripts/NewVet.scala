package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class NewVet extends Simulation {

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

object VetList {
	val vetList = exec(http("VetList")
			.get("/vets")
			.headers(headers_0))
		.pause(4)
}

object NewVet {
	val newVet = exec(http("NewVet")
			.get("/vets/new")
			.headers(headers_0))
		.pause(64)
}

object CreateVetSuccess {
	val createVetSuccess = exec(http("CreateVetSuccess")
			.post("/vets/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("firstName", "James")
			.formParam("lastName", "Arthur")
			.formParam("_csrf", "96f9574a-541e-4526-91f7-5becee299076"))
		.pause(4)
}

object CreateVetError {
	val createVetError = exec(http("CreateVetError")
			.post("/vets/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("firstName", "")
			.formParam("lastName", "")
			.formParam("_csrf", "96f9574a-541e-4526-91f7-5becee299076"))
		.pause(4)
}

	val positiveScn = scenario("CreateVetSuccess").exec(Home.home, Login.login, VetList.vetList, NewVet.newVet, CreateVetSuccess.createVetSuccess)
    val negativeScn = scenario("CreateVetError").exec(Home.home, Login.login, VetList.vetList, NewVet.newVet, CreateVetError.createVetError)

	 setUp(
        negativeScn.inject(rampUsers(2500) during (100 seconds)), 
        positiveScn.inject(rampUsers(2500) during (100 seconds))
    ).protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95))
}