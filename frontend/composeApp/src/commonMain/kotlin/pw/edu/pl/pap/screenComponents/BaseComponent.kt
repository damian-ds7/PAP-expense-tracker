package pw.edu.pl.pap.screenComponents

import kotlinx.coroutines.CoroutineScope

interface BaseComponent {
    val coroutineScope: CoroutineScope
}