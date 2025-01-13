package pw.edu.pl.pap.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatNumber(number: Float): String {

    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        decimalSeparator = ','   // Use ',' for decimal separator
        groupingSeparator = ' ' // Use space for grouping separator
    }

    val pattern = "#,###.##"
    val decimalFormat = DecimalFormat(pattern, symbols)

    return decimalFormat.format(number)
}