package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CreateProductDiagnosis extends Simulation {

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

object CreateProductSuccess {
	val createProductSuccess = exec(http("CreateProductForm")
			.get("/shops/1/products/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(31)
		.exec(http("CreatedProductSuccess")
			.post("/shops/1/products/new")
			.headers(headers_1)
			.formParam("name", "productTest")
			.formParam("price", "10")
			.formParam("stock", "10")
			.formParam("_csrf", "${stoken}"))
		.pause(28)
}

object CreateProductDuplicatedName {
	val createProductDuplicatedName = exec(http("CreateProductForm")
			.get("/shops/1/products/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(31)
		.exec(http("CreatedProductDuplicatedName")
			.post("/shops/1/products/new")
			.headers(headers_1)
			.formParam("name", "product1")
			.formParam("price", "10")
			.formParam("stock", "10")
			.formParam("_csrf", "${stoken}"))
		.pause(28)
}

	val positiveScn = scenario("CreateProductSuccess").exec(Home.home, Login.login, ShowShop.showShop, CreateProductSuccess.createProductSuccess)
	val negativeScn = scenario("CreateProductDuplicatedName").exec(Home.home, Login.login, ShowShop.showShop, CreateProductDuplicatedName.createProductDuplicatedName)

	setUp(
		negativeScn.inject(rampUsers(5000) during (100 seconds)), 
		positiveScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}