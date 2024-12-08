package pw.edu.pl.pap.util

fun sanitizePriceInput(newParameter: String): String? {
    val sanitizedInput = newParameter.replace(" ", "").replace(",", ".")

    return if (sanitizedInput.all { it.isDigit() || it == '.' } &&
        sanitizedInput.count { it == '.' } <= 1
    ) {
        sanitizedInput
    } else {
        null
    }
}