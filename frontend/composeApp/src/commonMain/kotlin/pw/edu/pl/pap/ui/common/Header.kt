package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import pw.edu.pl.pap.util.constants.padding

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

@Composable
fun ClickableHeader(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable { onClick() },
        )
    }
}

@Composable
fun TwoChoiceClickableHeader(
    text: String,
    onClick: () -> Unit,
    text2: String,
    onClick2: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Text(
            text = text,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onClick() },
        )
        Text(
            text = text2,
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { onClick2() },
        )
    }
}

