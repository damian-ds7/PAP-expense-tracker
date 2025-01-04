package pw.edu.pl.pap.screenComponents

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.api.ApiService

interface BaseScreenComponent : ComponentContext, BaseComponent {
    val apiService: ApiService
}