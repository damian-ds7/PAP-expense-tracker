package pw.edu.pl.pap.data

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String,
)
