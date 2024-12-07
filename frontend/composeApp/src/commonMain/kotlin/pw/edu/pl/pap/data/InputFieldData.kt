package pw.edu.pl.pap.data

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState
import kotlinx.serialization.Serializable

data class InputFieldData(
    val title: String,
    var parameter: MutableState<String>,
    val onChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions? = null,  // Optional keyboard options, used for number only keyboard
)