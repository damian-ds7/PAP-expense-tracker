package pw.edu.pl.pap.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

fun formatDate(date: LocalDate, pattern: String = "dd MMMM yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.toJavaLocalDate().format(formatter)}