package com.xq.domain

import java.time.LocalDate
import java.util.UUID

case class CashPoolQuarterEnd(
  uuid: UUID,
  inQuarterCashPoolName: String,
  inQuarterCashPoolUuid: UUID,
  quarterEndDate: LocalDate,
  outQuarterCashPoolName: String,
  outQuarterCashPoolUuid: UUID
)
