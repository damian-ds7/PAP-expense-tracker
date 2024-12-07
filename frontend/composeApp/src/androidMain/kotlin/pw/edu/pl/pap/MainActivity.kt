package pw.edu.pl.pap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponent = retainedComponent { componentContext ->
            RootComponent(
                componentContext = componentContext,
                baseUrl = BuildConfig.BASE_URL
            )
        }

        setContent {
            App(rootComponent)
        }
    }
}
