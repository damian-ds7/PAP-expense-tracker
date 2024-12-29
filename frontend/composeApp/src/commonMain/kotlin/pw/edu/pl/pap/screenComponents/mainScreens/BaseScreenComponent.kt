package pw.edu.pl.pap.screenComponents.mainScreens

import com.arkivanov.decompose.ComponentContext
import pw.edu.pl.pap.api.ApiService
import pw.edu.pl.pap.screenComponents.BaseComponent

interface BaseScreenComponent : ComponentContext, BaseComponent {
    val apiService: ApiService
}