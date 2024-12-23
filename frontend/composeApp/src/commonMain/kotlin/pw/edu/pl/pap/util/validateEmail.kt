package pw.edu.pl.pap.util

fun validateEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+.[A-Za-z0-9.-]+$".toRegex()
    return email.matches(emailRegex)
}