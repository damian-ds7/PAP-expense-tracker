package pw.edu.pl.pap.util.listFunctions

import kotlin.reflect.KProperty1

fun <T> getIndexByField(dataList: List<T>, fieldValue: Any?, field: KProperty1<T, *>): Int? {
    return dataList.indexOfFirst { field.get(it) == fieldValue }.takeIf { it >= 0 }
}