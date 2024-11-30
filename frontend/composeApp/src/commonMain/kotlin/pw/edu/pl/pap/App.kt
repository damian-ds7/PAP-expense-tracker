package pw.edu.pl.pap

import androidx.compose.material3.*
import androidx.compose.runtime.*
import pw.edu.pl.pap.apiclient.ApiClient
import pw.edu.pl.pap.ui.home.HomeScreen
import pw.edu.pl.pap.viewmodel.HomeViewModel

@Composable
fun App() {
    val apiClient = remember { ApiClient() }
    val viewModel = remember(apiClient) { HomeViewModel(apiClient) }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold { HomeScreen(viewModel) }
    }
}