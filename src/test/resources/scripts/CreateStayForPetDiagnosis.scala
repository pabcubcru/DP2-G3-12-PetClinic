package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateStayForPetDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.dp2.com",
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

	object ShowOwner1 {
		val showOwner1 = exec(http("ShowOwner1")
			.get("/owners/1")
			.headers(headers_0))
		.pause(20)
	}

		object ShowOwner3 {
		val showOwner3 = exec(http("ShowOwner3")
			.get("/owners/3")
			.headers(headers_0))
		.pause(20)
	}

object CreateSuccessStayForPetDiagnosis{
	val createSuccessStayForPetDiagnosis = exec(http("CreateSuccessStayForPetDiagnosis")
			.get("/owners/1/pets/1/stays/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("CreateSuccessStayForPetDiagnosis")
			.post("/owners/1/pets/1/stays/new")
			.headers(headers_2)
			.formParam("startdate", "2020/05/20")
			.formParam("finishdate", "2020/06/15")
			.formParam("specialCares", "specialCares")
			.formParam("price", "200")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
}

object CreateStayForPetDiagnosisHasErrors{
	val createStayForPetDiagnosisHasErrors = exec(http("CreateStayForPetDiagnosisHasErrors")
			.get("/owners/3/pets/4/stays/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("CreateStayForPetDiagnosisHasErrors")
			.post("/owners/3/pets/4/stays/new")
			.headers(headers_2)
			.formParam("startdate", "2020/05/20")
			.formParam("finishdate", "")
			.formParam("specialCares", "specialCares")
			.formParam("price", "200")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
}
	

	val positiveScn = scenario("CreateSuccessStayForPetDiagnosis").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner1.showOwner1, CreateSuccessStayForPetDiagnosis.createSuccessStayForPetDiagnosis)
	val negativeScn = scenario("CreateStayForPetDiagnosisHasErrors").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner3.showOwner3, CreateStayForPetDiagnosisHasErrors.createStayForPetDiagnosisHasErrors)

	setUp(
		negativeScn.inject(rampUsers(1300) during (180 seconds)), 
		positiveScn.inject(rampUsers(1300) during (180 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}