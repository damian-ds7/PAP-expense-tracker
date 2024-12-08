package pw.edu.pl.pap.ui.common


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BackButton(onUpdate: () -> Unit): Unit {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            shape = RoundedCornerShape(20),
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
                .align(Alignment.BottomStart)
                .offset(x = 30.dp, y = (-30).dp),
            colors = ButtonColors(Color.DarkGray, Color.Cyan, Color.DarkGray, Color.Cyan),
            contentPadding = PaddingValues(0.dp),
            onClick = { onUpdate() }
        ) {
            Text(
                text = "BACK",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}