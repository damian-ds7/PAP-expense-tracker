package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.*

@Composable
fun ConfirmButton(text: String, onUpdate: () -> Unit): Unit {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            shape = RoundedCornerShape(20),
            modifier = Modifier
                .width(100.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter)
                .offset(x= 0.dp, y= (-30).dp),
            colors = ButtonColors(Color.DarkGray, Color.Cyan, Color.DarkGray, Color.Cyan),
            contentPadding = PaddingValues(0.dp),
            onClick = {onUpdate()}
        ) {
            Text(
                text = text,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }

}