package pw.edu.pl.pap.util.listFunctions

import kotlin.reflect.KProperty1

fun <T : Any> getIndicesFromItems(
    dataList: List<T>, items: List<String>?, field: KProperty1<T, *>
): List<Int>? {
    return items?.let {
        dataList.mapIndexedNotNull { index, item ->
            val fieldValue = field.get(item) as? String
            if (fieldValue in items) index else null
        }
    }
}