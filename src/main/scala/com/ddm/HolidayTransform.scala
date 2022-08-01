package com.ddm

import sttp.tapir.{Schema, ValidationResult, Validator}
import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.{Duration, LocalDate}
import java.util.UUID

object HolidayTransform {
  case class HolidayTransformDto(marketUuid: UUID, holidayDate: LocalDate, workingDate: LocalDate)
  implicit val jsonEncoder: JsonEncoder[HolidayTransformDto] = DeriveJsonEncoder.gen[HolidayTransformDto]
  implicit val jsonDecoder: JsonDecoder[HolidayTransformDto] = DeriveJsonDecoder.gen[HolidayTransformDto]
  implicit val schema: Schema[HolidayTransformDto] =
    Schema
      .derived[HolidayTransformDto]
//      .validate(
//        Validator.custom(
//          dto =>
//            ValidationResult
//              .validWhen(
//                Math.abs(Duration.between(dto.holidayDate.atTime(0, 0), dto.workingDate.atTime(0, 0)).toDays) <= 366
//              ),
//          Some("Difference between holiday and working date cannot be more than a year")
//        )
//      )
}
