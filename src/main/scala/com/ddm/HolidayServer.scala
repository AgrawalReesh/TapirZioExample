package com.ddm

import com.ddm.HolidayTransform._
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.zio._
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import sttp.tapir.ztapir._
import zhttp.http.{Http, Request, Response}
import zhttp.service.Server
import zio.{Scope, Task, ZIO, ZIOAppArgs, ZIOAppDefault}

import java.util.UUID

object HolidayServer extends ZIOAppDefault {

  val holidaysUpsertEndpoint: PublicEndpoint[(UUID, HolidayTransformDto), String, HolidayTransformDto, Any] =
    endpoint.post
      .in("holidays" / "market" / path[UUID]("marketUuid"))
      .in(jsonBody[HolidayTransformDto])
      .errorOut(stringBody)
      .out(jsonBody[HolidayTransformDto])

  val zioHolidaysUpsert: ZServerEndpoint[Any, Any] =
    holidaysUpsertEndpoint.zServerLogic { case (_, dto) =>
      (for {
        saved <- HolidayTransformRepo.upsert(dto)
      } yield saved)
        .provide(HolidayTransformRepoLive.layer)
        .mapError(_.getMessage)
    }

  val holidaysFetchEndpoint: PublicEndpoint[UUID, String, HolidayTransformDto, Any] =
    endpoint.get
      .in("holidays" / "market" / path[UUID]("marketUuid"))
      .errorOut(stringBody)
      .out(jsonBody[HolidayTransformDto])

  val zioHolidaysFetch: ZServerEndpoint[Any, Any] =
    holidaysFetchEndpoint.zServerLogic { uuid =>
      (for {
        result <- HolidayTransformRepo.fetch(uuid)
      } yield result)
        .provide(HolidayTransformRepoLive.layer)
        .mapError(_.getMessage)
    }

  val swaggerEndpoint: List[ZServerEndpoint[Any, Any]] =
    SwaggerInterpreter()
      .fromEndpoints[Task](List(holidaysUpsertEndpoint, holidaysFetchEndpoint), "Holiday manager", "1.0")

  val routes: Http[Any, Throwable, Request, Response] =
    ZioHttpInterpreter().toHttp(List(zioHolidaysUpsert, zioHolidaysFetch) ++ swaggerEndpoint)

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    Server.start(8080, routes)

}
