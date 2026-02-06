package com.everybirr.core

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.money(): BigDecimal = setScale(2, RoundingMode.HALF_UP)
