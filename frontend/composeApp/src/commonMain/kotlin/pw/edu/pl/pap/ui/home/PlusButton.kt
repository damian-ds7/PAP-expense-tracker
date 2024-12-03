package pw.edu.pl.pap.ui.home

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
fun PlusButton(buttonClicked: Boolean, onUpdate: (Boolean) -> Unit): Unit {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            shape = RoundedCornerShape(100),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .align(Alignment.BottomEnd)
                .offset(x= (-20).dp, y= (-20).dp),
            colors = ButtonColors(Color.DarkGray, Color.Cyan, Color.DarkGray, Color.Cyan),
            contentPadding = PaddingValues(0.dp),
            onClick = {onUpdate(buttonClicked)}
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ){
                Text(
                    text = "+",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 0.dp, y = (-10).dp)
                )
            }
        }
    }
}
