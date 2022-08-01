package com.xq.services

import com.xq.domain.CashPoolQuarterEnd
import zio.ZIO

trait CrossQuarterCashPoolService {
  def getAllCashPoolQuarterEnds(): ZIO[Any, Throwable, List[CashPoolQuarterEnd]]
  def createCashPoolQuarterEnd(args: List[CashPoolQuarterEnd]): ZIO[Any, Throwable, Unit]
}

object CrossQuarterCashPoolService {

  def getAllCashPoolQuarterEnds: ZIO[CrossQuarterCashPoolService, Throwable, List[CashPoolQuarterEnd]] =
    ZIO.serviceWithZIO(_.getAllCashPoolQuarterEnds())

  def createCashPoolQuarterEnd(args: List[CashPoolQuarterEnd]): ZIO[CrossQuarterCashPoolService, Throwable, Unit] =
    ZIO.serviceWithZIO(_.createCashPoolQuarterEnd(args))
}
