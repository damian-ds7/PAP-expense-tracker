package pw.edu.pl.pap.util

import androidx.compose.runtime.MutableState

suspend fun updatePrice(newPrice: String, price: MutableState<String>) {
    try {
        price.value = newPrice
        println("Updated record " + price.value)
    } catch (e: Exception) {
        println("Incorrect")
    }
}