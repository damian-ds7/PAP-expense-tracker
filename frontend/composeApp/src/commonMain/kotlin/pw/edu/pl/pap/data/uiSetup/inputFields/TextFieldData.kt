package pw.edu.pl.pap.data.uiSetup.inputFields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.MutableState

data class TextFieldData (
    var parameter: MutableState<String>,
    val onChange: (String) -> Unit,
    val keyboardOptions: KeyboardOptions? = null,  // Optional keyboard options, used for number only keyboard
    val password: Boolean = false,
)