package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class EditDiscountForProductDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.9,es-ES;q=0.8,es;q=0.7,en-US;q=0.6,ru;q=0.5")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

object Home {
	val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
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

object ShowShop {
	val showShop = exec(http("ShowShop")
			.get("/shops/1")
			.headers(headers_0))
		.pause(15)
}

object ShowProduct5 {
	val showProduct5 = exec(http("ShowProduct5")
			.get("/shops/1/products/5")
			.headers(headers_0))
		.pause(15)
}

object ShowProduct3 {
	val showProduct3 = exec(http("ShowProduct3")
			.get("/shops/1/products/3")
			.headers(headers_0))
		.pause(15)
}

object EditDiscountForProduct5 {
	val editDiscountForProduct5 = exec(http("EditDiscountForProduct5Form")
			.get("/shops/1/products/5/discounts/3/edit")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("EditDiscountForProduct5Success")
			.post("/shops/1/products/5/discounts/3/edit")
			.headers(headers_2)
			.formParam("percentage", "50")
			.formParam("startDate", "2020/07/13")
			.formParam("finishDate", "2020/09/19")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
}

object EditDiscountForProduct3 {
	val editDiscountForProduct3 = exec(http("EditDiscountForProduct3Form")
			.get("/shops/1/products/3/discounts/2/edit")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("EditDiscountForProduct3HasErrors")
			.post("/shops/1/products/3/discounts/2/edit")
			.headers(headers_2)
			.formParam("percentage", "20")
			.formParam("startDate", "2020/07/13")
			.formParam("finishDate", "")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
}

	val positiveScn = scenario("EditDiscountForProductSuccess").exec(Home.home, Login.login, ShowShop.showShop, ShowProduct5.showProduct5, EditDiscountForProduct5.editDiscountForProduct5)
	val negativeScn = scenario("EditDiscountForProductHasErrors").exec(Home.home, Login.login, ShowShop.showShop, ShowProduct3.showProduct3, EditDiscountForProduct3.editDiscountForProduct3)

	setUp(
		negativeScn.inject(rampUsers(5000) during (100 seconds)), 
		positiveScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))

}