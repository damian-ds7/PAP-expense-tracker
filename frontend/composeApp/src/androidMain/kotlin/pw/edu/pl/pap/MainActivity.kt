package pw.edu.pl.pap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import pw.edu.pl.pap.screenComponents.RootComponent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = retainedComponent { componentContext ->
            RootComponent(
                componentContext = componentContext
            )
        }

        setContent {
            App(rootComponent, BuildConfig.BASE_URL)
        }
    }
}
