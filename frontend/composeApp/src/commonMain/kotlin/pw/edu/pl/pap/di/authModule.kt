package pw.edu.pl.pap.di

import com.russhwolf.settings.Settings
import org.koin.dsl.module
import pw.edu.pl.pap.api.auth.AuthServiceCreator
import pw.edu.pl.pap.repositories.auth.LoginRepository
import pw.edu.pl.pap.repositories.auth.SignupRepository
import pw.edu.pl.pap.repositories.auth.TokenRepository
import pw.edu.pl.pap.storage.TokenStorage

val authModule = module {
    single { AuthServiceCreator(getProperty("baseUrl")) }

    single { TokenStorage(Settings()) }

    single { TokenRepository(get(), get<AuthServiceCreator>().createRefreshTokenApi()) }

    single { LoginRepository(get<AuthServiceCreator>().createLoginApi(), get()) }

    single { SignupRepository(get<AuthServiceCreator>().createSignupApi(), get()) }
}