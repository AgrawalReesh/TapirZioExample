package com.xq.app

import com.xq.domain.CashPoolQuarterEnd
import com.xq.services.{CrossQuarterCashPoolService, CrossQuarterCashPoolServiceLive}
import zhttp.http._
import zhttp.service.Server
import zio.{ZIO, ZIOAppDefault}
import zio.json._

object HttpMain extends ZIOAppDefault {

  // Create HTTP route
  val app: HttpApp[Any, Nothing] = Http.collect[Request] {
    case Method.GET -> !! / "text" => Response.text("Hello World!")
    case Method.GET -> !! / "json" => Response.json("""{"greetings": "Hello World!"}""")
  }

  implicit val quarterEndJsonEncoder: JsonEncoder[CashPoolQuarterEnd] =
    DeriveJsonEncoder.gen[CashPoolQuarterEnd]

  implicit val quarterEndJsonDecoder: JsonDecoder[CashPoolQuarterEnd] =
    DeriveJsonDecoder.gen[CashPoolQuarterEnd]

  val cashPoolQuarterEndApp: Http[CrossQuarterCashPoolService, Throwable, Request, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "cash-pool-quarter-ends" =>
        CrossQuarterCashPoolService.getAllCashPoolQuarterEnds
          .map(data => Response.json(data.toJson))
      case req @ Method.POST -> !! / "create" =>
        for {
          request <- req.bodyAsString
          entries <- ZIO.fromEither(request.fromJson[List[CashPoolQuarterEnd]]).mapError(new Exception(_))
          _       <- CrossQuarterCashPoolService.createCashPoolQuarterEnd(entries)
        } yield Response.ok
    }

  // Run it like any simple app
  override val run =
    Server.start(8080, cashPoolQuarterEndApp.provideLayer(CrossQuarterCashPoolServiceLive.layer))
}
