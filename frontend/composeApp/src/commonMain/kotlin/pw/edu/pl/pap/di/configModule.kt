package pw.edu.pl.pap.di

import org.koin.dsl.module
import pw.edu.pl.pap.api.data.ConfigServiceCreator
import pw.edu.pl.pap.repositories.data.ConfigRepository

val configModule = module {
    single { ConfigServiceCreator(getProperty("baseUrl")) }

    single { get<ConfigServiceCreator>().createConfigApi() }

    single { ConfigRepository(get()) }
}