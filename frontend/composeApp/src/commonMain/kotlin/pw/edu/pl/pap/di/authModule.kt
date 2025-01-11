package pw.edu.pl.pap.di

import org.koin.dsl.module
import pw.edu.pl.pap.api.auth.AuthServiceCreator
import pw.edu.pl.pap.repositories.auth.LoginRepository
import pw.edu.pl.pap.repositories.auth.TokenRepository

val authModule = module {
    single { TokenRepository() }

    single { AuthServiceCreator(getProperty("baseUrl")) }

    single { LoginRepository(get<AuthServiceCreator>().createLoginApi(), get()) }
}