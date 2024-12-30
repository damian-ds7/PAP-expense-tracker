package pw.edu.pl.pap.data.uiSetup.inputFields

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

data class ButtonData (
    val title: String,
    val isColored: Boolean = false,
    val customColor: Color = Color.Red.copy(alpha = 0.2f),
    val onClick: () -> Unit,
)