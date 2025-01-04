package pw.edu.pl.pap.util.dateFunctions

import kotlinx.datetime.LocalDate

fun dateToMillis(date: LocalDate?): Long? {
    return date?.toEpochDays()?.toLong()?.let { it * 24 * 60 * 60 * 1000 }
}