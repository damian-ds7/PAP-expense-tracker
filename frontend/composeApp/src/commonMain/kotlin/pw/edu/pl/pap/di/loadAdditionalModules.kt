package pw.edu.pl.pap.di

import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module

fun loadAdditionalModules(vararg modules: Module) {
    loadKoinModules(modules.asList())
}