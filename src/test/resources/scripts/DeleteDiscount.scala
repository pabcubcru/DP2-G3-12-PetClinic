package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteDiscount extends Simulation {

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

object Shop {
	val shop = exec(http("Shop")
			.get("/shops/1")
			.headers(headers_0))
		.pause(4)
}

object Product {
	val product = exec(http("Product")
			.get("/shops/1/products/1")
			.headers(headers_0))
		.pause(2)
}

object DeleteDiscountSuccess {
	val deleteDiscountSuccess = exec(http("DeleteDiscountSuccess")
			.get("/shops/1/products/1/discounts/1/delete")
			.headers(headers_0))
		.pause(2)
}

object DeleteDiscountError {
	val deleteDiscountError = exec(http("DeleteDiscountError")
			.get("/shops/1/products/1/discounts/2/delete")
			.headers(headers_0))
		.pause(2)
}

	val positiveScn = scenario("DeleteDiscountSuccess").exec(Home.home, Login.login, Shop.shop, Product.product, DeleteDiscountSuccess.deleteDiscountSuccess)
    val negativeScn = scenario("DeleteDiscountError").exec(Home.home, Login.login, Shop.shop, Product.product, DeleteDiscountError.deleteDiscountError)

    setUp(
        negativeScn.inject(rampUsers(7500) during (100 seconds)), 
        positiveScn.inject(rampUsers(7500) during (100 seconds))
    ).protocols(httpProtocol)
    .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95))
}

	