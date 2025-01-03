package pw.edu.pl.pap.util.api

import pw.edu.pl.pap.data.databaseAssociatedData.FilterParams

fun generateParamRequest(filterParams: FilterParams): String {
    var query = "?"
    val propertyMap = filterParams.toMap()
    propertyMap.forEach { (key, value) ->
        if (value == "") return@forEach
        query += "$key=$value&"
    }
    return query
}