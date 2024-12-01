package pw.edu.pl.pap.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String, inPattern: String, OutPattern: String): String {
    val formatterInput = DateTimeFormatter.ofPattern(inPattern)
    val date = LocalDate.parse(dateString, formatterInput)

    val formatter = DateTimeFormatter.ofPattern(OutPattern)
    return date.format(formatter)
}