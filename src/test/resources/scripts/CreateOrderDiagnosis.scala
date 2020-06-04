package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateOrderDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.png""", """.*.js""", """.*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.9,es-ES;q=0.8,es;q=0.7,en-US;q=0.6,ru;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_2 = Map(
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
			.headers(headers_2)
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

object CreateOrderSuccess {
	val createOrderSuccess = exec(http("CreateOrderForm")
			.get("/shops/1/orders/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(43)
		.exec(http("OrderCreated")
			.post("/shops/1/orders/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("name", "orderTest")
			.formParam("supplier", "orderTest")
			.formParam("productNumber", "10")
			.formParam("product.name", "product1")
			.formParam("_csrf", "${stoken}"))
		.pause(29)
}

object CreateOrderHasErrors {
	val createOrderHasErrors = exec(http("ErrorCreatingOrder")
			.get("/shops/1/orders/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(43)
		.exec(http("OrderHasErrors")
			.post("/shops/1/orders/new")
			.headers(headers_2)
			.formParam("name", "orderTest")
			.formParam("supplier", "orderTest")
			.formParam("productNumber", "0")
			.formParam("product.name", "product1")
			.formParam("_csrf", "${stoken}"))
		.pause(29)
}

	val positiveScn = scenario("CreateOrderSuccess").exec(Home.home, Login.login, ShowShop.showShop, CreateOrderSuccess.createOrderSuccess)
	val negativeScn = scenario("CreateOrderHasErrors").exec(Home.home, Login.login, ShowShop.showShop, CreateOrderHasErrors.createOrderHasErrors)

	setUp(
		negativeScn.inject(rampUsers(5000) during (100 seconds)), 
		positiveScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}