package com.ddm

import com.ddm.HolidayTransform.HolidayTransformDto
import zio.{ZIO, ZLayer}
import zio.prelude.Validation

import java.time.{Duration, LocalDate}
import java.util.UUID

trait HolidayTransformRepo {
  def fetch(marketUuid: UUID): ZIO[Any, Throwable, HolidayTransformDto]
  def upsert(holidayTransform: HolidayTransformDto): ZIO[Any, Throwable, HolidayTransformDto]
  def validate(holidayTransformDto: HolidayTransformDto): ZIO[Any, Throwable, HolidayTransformDto]
}

object HolidayTransformRepo {
  def fetch(marketUuid: UUID): ZIO[HolidayTransformRepo, Throwable, HolidayTransformDto] =
    ZIO.serviceWithZIO(_.fetch(marketUuid))
  def upsert(holidayTransform: HolidayTransformDto): ZIO[HolidayTransformRepo, Throwable, HolidayTransformDto] =
    ZIO.serviceWithZIO(_.upsert(holidayTransform))
}

case class HolidayTransformRepoLive() extends HolidayTransformRepo {

  override def fetch(marketUuid: UUID): ZIO[Any, Throwable, HolidayTransformDto] =
    ZIO.succeed(HolidayTransformDto(UUID.randomUUID(), LocalDate.now(), LocalDate.now()))

  override def upsert(dto: HolidayTransformDto): ZIO[Any, Throwable, HolidayTransformDto] =
    for {
      _ <- validate(dto)
      _ <- ZIO.logInfo(s"Saved transform!")
    } yield dto

  override def validate(dto: HolidayTransformDto): ZIO[Any, Throwable, HolidayTransformDto] =
    Validation
      .fromPredicateWith(
        s"Difference between holiday=${dto.holidayDate} and working=${dto.workingDate} date should not be more than a year"
      )(
        dto
      )(dto =>
        Math.abs(
          Duration
            .between(
              dto.holidayDate.atTime(0, 0),
              dto.workingDate.atTime(0, 0)
            )
            .toDays
        ) <= 366
      )
      .toZIO
      .mapError(new Exception(_))

}

object HolidayTransformRepoLive {
  def layer: ZLayer[Any, Throwable, HolidayTransformRepo] =
    ZLayer.succeed(HolidayTransformRepoLive())
}
