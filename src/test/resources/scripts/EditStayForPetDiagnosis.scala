package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditStayForPetDiagnosis extends Simulation {

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

		object ShowOwner2 {
		val showOwner2 = exec(http("ShowOwner2")
			.get("/owners/2")
			.headers(headers_0))
		.pause(20)
	}


	object EditStayForPetDiagnosisSuccess {
		val editStayForPetDiagnosisSuccess = exec(http("EditStayForPetDiagnosisSuccess")
			.get("/owners/1/pets/1/stays/1/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(14)
		.exec(http("EditStayForPetDiagnosisSuccess")
			.post("/owners/1/pets/1/stays/1/edit")
			.headers(headers_2)
			.formParam("startdate", "2020/07/15")
			.formParam("finishdate", "2020/07/19")
			.formParam("specialCares", "editSpecialCares")
			.formParam("price", "77")
			.formParam("_csrf", "${stoken}"))
		.pause(22)
	}

	object EditStayForPetDiagnosisHasErrors {
		val editStayForPetDiagnosisHasErrors = exec(http("EditStayForPetDiagnosisHasErrors")
			.get("/owners/2/pets/2/stays/5/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(11)
		.exec(http("EditStayForPetDiagnosisHasErrors")
			.post("/owners/2/pets/2/stays/5/edit")
			.headers(headers_2)
			.formParam("startdate", "2020/11/01")
			.formParam("finishdate", "2020/10/20")
			.formParam("specialCares", "special")
			.formParam("price", "30")
			.formParam("_csrf", "${stoken}"))
		.pause(25)
	}
	

	val positiveScn = scenario("EditStayForPetDiagnosisSuccess").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner1.showOwner1, EditStayForPetDiagnosisSuccess.editStayForPetDiagnosisSuccess)
	val negativeScn = scenario("EditStayForPetDiagnosisHasErrors").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner2.showOwner2, EditStayForPetDiagnosisHasErrors.editStayForPetDiagnosisHasErrors)

	setUp(
		negativeScn.inject(rampUsers(1720) during (167 seconds)), 
		positiveScn.inject(rampUsers(1720) during (167 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}