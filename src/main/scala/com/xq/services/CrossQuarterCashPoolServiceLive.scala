package com.xq.services

import com.xq.domain.CashPoolQuarterEnd
import zio.{Random, ZIO, ZLayer}

import java.time.LocalDate
import java.util.UUID
import scala.collection.mutable

case class CrossQuarterCashPoolServiceLive() extends CrossQuarterCashPoolService {

  // its okay, this is for experimentation :P
  val current = mutable.Map.empty[UUID, CashPoolQuarterEnd]
  current.addOne(
    UUID.randomUUID(),
    CashPoolQuarterEnd(UUID.randomUUID(), "Costco", UUID.randomUUID(), LocalDate.now(), "HSBC", UUID.randomUUID())
  )

  override def getAllCashPoolQuarterEnds(): ZIO[Any, Throwable, List[CashPoolQuarterEnd]] =
    ZIO.attempt(current.values.toList)

  override def createCashPoolQuarterEnd(args: List[CashPoolQuarterEnd]): ZIO[Any, Throwable, Unit] =
    ZIO
      .foreach(args) { arg =>
        for {
          freshUuid <- Random.nextUUID
          _         <- ZIO.attempt(current.addOne((arg.inQuarterCashPoolUuid, arg.copy(uuid = freshUuid))))
        } yield ()
      }
      .unit
}

object CrossQuarterCashPoolServiceLive {
  def layer: ZLayer[Any, Throwable, CrossQuarterCashPoolService] =
    ZLayer.succeed(CrossQuarterCashPoolServiceLive())

}
