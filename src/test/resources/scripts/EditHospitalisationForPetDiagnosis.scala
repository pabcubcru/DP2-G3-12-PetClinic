package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditHospitalisationForPetDiagnosis extends Simulation {
val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
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

object EditHospitalisationForPetDiagnosis{
	val editHospitalisationForPetDiagnosis = exec(http("EditHospitalisationForPetDiagnosis")
			.get("/owners/1/pets/1/hospitalisations/4/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(19)
		.exec(http("EditHospitalisationForPetDiagnosisSuccess")
			.post("/owners/1/pets/1/hospitalisations/4/edit")
			.headers(headers_2)
			.formParam("treatment", "treatmentEdit")
			.formParam("diagnosis", "diagnosis4")
			.formParam("totalPrice", "50.0")
			.formParam("hospitalisationStatus.name", "HOSPITALISED")
			.formParam("_csrf", "${stoken}"))
		.pause(27)

}


object EditHospitalisationHasErrors{
	val editHospitalisationHasErrors = exec(http("EditHospitalisationHasErrors")
			.get("/owners/2/pets/2/hospitalisations/5/edit")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("EditHospitalisationHasErrors")
			.post("/owners/1/pets/1/hospitalisations/5/edit")
			.headers(headers_2)
			.formParam("treatment", "treatment11")
			.formParam("diagnosis", "diagnosis11")
			.formParam("totalPrice", "-100")
			.formParam("hospitalisationStatus.name", "HOSPITALISED")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
}
	val positiveScn = scenario("EditHospitalisationForPetDiagnosis").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner1.showOwner1, EditHospitalisationForPetDiagnosis.editHospitalisationForPetDiagnosis)
	val negativeScn = scenario("EditHospitalisationHasErrors").exec(Home.home, Login.login, FindOwners.findOwners, FindOwnersList.findOwnersList, ShowOwner2.showOwner2, EditHospitalisationHasErrors.editHospitalisationHasErrors)

	setUp(
		negativeScn.inject(rampUsers(2000) during (200 seconds)), 
		positiveScn.inject(rampUsers(2000) during (200 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}