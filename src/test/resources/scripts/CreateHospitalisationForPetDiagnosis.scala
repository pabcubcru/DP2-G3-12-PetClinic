package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateHospitalisationForPetDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9,en;q=0.8",
		"Origin" -> "http://localhost:8080",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Pragma" -> "no-cache",
		"User-Agent" -> "BingService")

	val headers_9 = Map(
		"Accept" -> "text/plain",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es",
		"Content-Type" -> "text/plain",
		"Proxy-Connection" -> "keep-alive",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Skype/8.59.0.77 Chrome/78.0.3904.130 Electron/7.1.11 Safari/537.36")

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
			.headers(headers_3)
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

	object ShowOwner3 {
		val showOwner3 = exec(http("ShowOwner3")
			.get("/owners/3")
			.headers(headers_0))
		.pause(20)
	}

		object ShowOwner4 {
		val showOwner4 = exec(http("ShowOwner4")
			.get("/owners/4")
			.headers(headers_0))
		.pause(20)
	}

	object CreatedHospitalisationForPet4 {
		val createdHospitalisationForPet4 = exec(http("CreatedHospitalisationForPet4")
			.get("/owners/3/pets/4/hospitalisations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(14)
		.exec(http("CreatedHospitalisationForPet4Success")
			.post("/owners/3/pets/4/hospitalisations/new")
			.headers(headers_3)
			.formParam("treatment", "treatment11")
			.formParam("diagnosis", "diagnosis11")
			.formParam("totalPrice", "100")
			.formParam("_csrf", "${stoken}"))
		.pause(125)
	}

object NotCreatedHospitalisationForPet5 {
		val notCreatedHospitalisationForPet5 = exec(http("NotCreatedHospitalisationForPet5")
			.get("/owners/4/pets/5/hospitalisations/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(14)
		.exec(http("NotCreatedHospitalisationForPet5")
			.post("/owners/4/pets/5/hospitalisations/new")
			.headers(headers_3)
			.formParam("treatment", "treatment11")
			.formParam("diagnosis", "diagnosis11")
			.formParam("totalPrice", "")
			.formParam("_csrf", "${stoken}"))
		.pause(125)
	}

	val positiveScn = scenario("CreatedHospitalisationForPet4").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner3.showOwner3, CreatedHospitalisationForPet4.createdHospitalisationForPet4)
	val negativeScn = scenario("NotCreatedHospitalisationForPet5").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner4.showOwner4, NotCreatedHospitalisationForPet5.notCreatedHospitalisationForPet5)

	setUp(
		negativeScn.inject(rampUsers(50) during (10 seconds)), 
		positiveScn.inject(rampUsers(50) during (10 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}