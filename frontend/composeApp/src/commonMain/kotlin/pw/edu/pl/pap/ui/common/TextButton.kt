package pw.edu.pl.pap.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    text: String,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    onUpdate: () -> Unit = {},
    width: Float = 0.3f,
    height: Float = 0.1f,
    sizeOfText: Float = 0.05f
) {
    val windowSize = getWindowSize()

    val buttonWidth: Dp = windowSize.width * width
    val buttonHeight: Dp = windowSize.height * height

    val fontSize = with(LocalDensity.current) { (windowSize.width * sizeOfText).toSp() }

    Button(
        shape = RoundedCornerShape(20),
        modifier = modifier.width(buttonWidth).height(buttonHeight),
        colors = colors,
        contentPadding = PaddingValues(0.dp),
        onClick = { onUpdate() },
        enabled = isEnabled
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
expect fun getWindowSize(): DpSize

//@Composable
//fun rememberWindowSize(): DpSize {
//    return if (isDesktop()) {
//        val density = LocalDensity.current
//        val size = try {
//            java.awt.Toolkit.getDefaultToolkit().screenSize
//        } catch (e: Exception) {
//            java.awt.Dimension(1920, 1080)
//        }
//        with(density) {
//            DpSize(size.width.toDp(), size.height.toDp())
//        }
//    } else {
//        val configuration = androidx.compose.ui.platform.LocalConfiguration.current
//        DpSize(
//            width = configuration.screenWidthDp.dp,
//            height = configuration.screenHeightDp.dp
//        )
//    }
//}
//
//fun isDesktop(): Boolean = System.getProperty("os.name")?.contains("Windows") == true ||
//        System.getProperty("os.name")?.contains("Mac") == true ||
//        System.getProperty("os.name")?.contains("Linux") == true
