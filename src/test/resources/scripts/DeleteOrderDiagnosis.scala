package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class DeleteOrderDiagnosis extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8,es;q=0.7")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map("Proxy-Connection" -> "keep-alive")



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
			.headers(headers_0)
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

object ShowOrder1{
	val showOrder1 = exec(http("ShowOrder1")
			.get("/shops/1/orders/1")
			.headers(headers_0))
		.pause(13)
}

object Order1Deleted{
	val order1Deleted = exec(http("Order1Deleted")
			.get("/shops/1/orders/1/delete")
			.headers(headers_0))
		.pause(17)
}

object ShowOrder2{
	val showOrder2 = exec(http("ShowOrder2")
			.get("/shops/1/orders/2")
			.headers(headers_0))
		.pause(27)
}

object Order2DeletedError{
	val order2DeletedError = exec(http("Order2DeletedError")
			.get("/shops/1/orders/2/delete")
			.headers(headers_0))
		.pause(13)
}
	

val positiveScn = scenario("DeleteOrderSuccess")
						.exec(Home.home, Login.login, ShowShop.showShop,
						ShowOrder1.showOrder1, Order1Deleted.order1Deleted)
val negativeScn = scenario("DeleteOrderHasErrors")
						.exec(Home.home, Login.login, ShowShop.showShop,
						ShowOrder2.showOrder2, Order2DeletedError.order2DeletedError)
	setUp(
		negativeScn.inject(rampUsers(3000) during (100 seconds)), 
		positiveScn.inject(rampUsers(3000) during (100 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95))
}