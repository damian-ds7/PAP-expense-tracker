package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextButton(
    text: String,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onUpdate: () -> Unit = {}
) {
    Button(
        shape = RoundedCornerShape(20),
        modifier = modifier
            .width(100.dp)
            .height(60.dp),
        colors = colors,
        contentPadding = PaddingValues(0.dp),
        onClick = { onUpdate() },
        enabled = isEnabled
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}