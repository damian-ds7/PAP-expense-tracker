package pw.edu.pl.pap.util.listFunctions

import kotlin.reflect.KProperty1

fun <T : Any> getItemsFromIndices(
    dataList: List<T>, indices: List<Int>?, field: KProperty1<T, *>
): List<String>? {
    return indices?.let {
        dataList.mapIndexedNotNull { index, item ->
            if (index in indices) field.get(item) as? String else null
        }
    }
}