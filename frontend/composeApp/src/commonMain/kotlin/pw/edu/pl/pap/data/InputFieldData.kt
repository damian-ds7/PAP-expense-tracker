package pw.edu.pl.pap.data

import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

@Serializable
data class InputFieldData(
    val title: String,
    var parameter: MutableState<String>,
    val onChange: (String) -> Unit,
)