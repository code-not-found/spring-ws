package com.codenotfound.order

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

    val httpConf = http
        .baseURL("http://localhost:9090")
        .contentTypeHeader("text/xml")

    val scn = scenario("Call Order Web Service").during(1 minutes) {
        exec(http("createOrder")
            .post("/codenotfound/ws/order")
            .body(RawFileBody("order-request.xml")))
    }

    setUp(
        scn.inject(rampUsers(10) over(1 minutes))
            .protocols(httpConf))
            .maxDuration(2 minutes)
}