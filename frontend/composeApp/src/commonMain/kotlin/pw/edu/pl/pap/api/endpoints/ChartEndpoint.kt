package pw.edu.pl.pap.api.endpoints

import kotlinx.datetime.LocalDate

sealed class ChartEndpoint(relativePath: String) : BaseEndpoint("/chart/my", relativePath) {
    data class GroupYearData(val year: Number) : ChartEndpoint("/group/$year")
    data class GroupCategoriesPeriod(val begin: LocalDate, val end: LocalDate) :
        ChartEndpoint("/group/categories/$begin/$end")

    data class UserYearData(val year: Number) : ChartEndpoint("/expenses/$year")
    data class UserCategoriesPeriod(val begin: LocalDate, val end: LocalDate) :
        ChartEndpoint("/expenses/categories/$begin/$end")
}