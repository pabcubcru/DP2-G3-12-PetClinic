package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateDiscountForProductDiagnosis extends Simulation {

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

object ShowProduct2 {
	val showProduct2 = exec(http("ShowProduct2")
			.get("/shops/1/products/2")
			.headers(headers_0))
		.pause(15)
}

object ShowProduct4 {
	val showProduct4 = exec(http("ShowProduct4")
			.get("/shops/1/products/4")
			.headers(headers_0))
		.pause(15)
}

object CreateDiscountForProduct2 {
	val createDiscountForProduct2 = exec(http("CreateDiscountForProduct2Form")
			.get("/shops/1/products/2/discounts/new")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("CreateDiscountForProduct2Success")
			.post("/shops/1/products/2/discounts/new")
			.headers(headers_2)
			.formParam("percentage", "30")
			.formParam("startDate", "2020/05/13")
			.formParam("finishDate", "2020/06/19")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
}

object CreateDiscountForProduct4 {
	val createDiscountForProduct4 = exec(http("CreateDiscountForProduct4Form")
			.get("/shops/1/products/4/discounts/new")
			.headers(headers_0)
			check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("CreateDiscountForProduct4HasErrors")
			.post("/shops/1/products/4/discounts/new")
			.headers(headers_2)
			.formParam("percentage", "30")
			.formParam("startDate", "2020/05/13")
			.formParam("finishDate", "")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
}

	val positiveScn = scenario("CreateDiscountForProductSuccess").exec(Home.home, Login.login, ShowShop.showShop, ShowProduct2.showProduct2, CreateDiscountForProduct2.createDiscountForProduct2)
	val negativeScn = scenario("CreateDiscountForProductHasErrors").exec(Home.home, Login.login, ShowShop.showShop, ShowProduct4.showProduct4, CreateDiscountForProduct4.createDiscountForProduct4)

	setUp(
		negativeScn.inject(rampUsers(5000) during (100 seconds)), 
		positiveScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}