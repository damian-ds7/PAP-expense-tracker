package pw.edu.pl.pap.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*

@Composable
fun Header(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.TopCenter)
        )
    }
}

