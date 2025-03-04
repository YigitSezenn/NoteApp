package com.example.mynoteapp.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    @SuppressLint("NotConstructor")
        private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String,navController: NavController, callback: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback("Kayıt başarılı. Lütfen giriş yapın.")

                    navController.navigate(NavigationItem.LoginScreen.route)
                } else {
                    val exception = task.exception
                    when (exception) {
                        is FirebaseAuthInvalidUserException -> {
                            callback("Bu Mail Adresi Zaten Kullanımda.")
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            callback("Geçersiz Mail Adresi veya Şifre.")
                        }
                        else -> {
                            callback("Hata: ${exception?.message}")
                        }
                    }
                }
            }
    }
}