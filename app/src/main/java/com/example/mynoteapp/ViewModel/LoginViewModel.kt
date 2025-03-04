package com.example.mynoteapp.ViewModel


import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.credentials.GetCredentialRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException


class LoginViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun LoginControllerModel(
        navController: NavController,
        Email: String,
        Password: String,
        callback: (String) -> Unit
    ) {
        // viewModelScope.launch{} ile coroutine başlatıyoruz
        if (Email.isBlank() || Password.isBlank()) {
            callback("Please fill in all fields!")
            return
        }
        auth.signInWithEmailAndPassword(Email, Password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback("Giriş Başarılı")
                    navController.navigate(NavigationItem.NoteScreen.route)
                } else {
                    val exception = task.exception
                    when (exception) {
                        is FirebaseAuthInvalidUserException -> {
                            callback("Bu e-posta adresi kayıtlı değil. Lütfen tekrar deneyin.")
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            callback("Eposta veya Şifre Geçersiz  .")
                        }

                        else -> {
                            callback("Hata: ${exception?.message}")
                        }
                    }
                }
            }
    }
}

//
//    class GoogleAuthHelper(private val context: Context) {
//        private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//        private val oneTapClient: SignInClient = Identity.getSignInClient(context) // Google One Tap istemcisi oluşturuldu
//
//        /**
//         * Google ile giriş işlemini başlatır ve IntentSender döndürür.
//         */
//        suspend fun signInWithGoogle(): IntentSender? {
//            return try {
//                val signInRequest = BeginSignInRequest.builder()
//                    .setGoogleIdTokenRequestOptions(
//                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                            .setSupported(true) // Google ID Token kullanımını destekle
//                            .setServerClientId("YOUR_WEB_CLIENT_ID") // Firebase Web Client ID (Firebase Console'dan alınmalı)
//                            .setFilterByAuthorizedAccounts(false) // Kullanıcıların hesap seçmesini sağla
//                            .build()
//                    )
//                    .setAutoSelectEnabled(true) // Kullanıcının tek bir hesabı varsa otomatik seçim yap
//                    .build()
//
//                val result = oneTapClient.beginSignIn(signInRequest).await()
//                result.pendingIntent.intentSender
//            } catch (e: Exception) {
//                e.printStackTrace()
//                null
//            }
//        }
//
//        /**
//         * Google girişinden dönen Intent verisini işler ve Firebase Authentication ile giriş yapar.
//         */
//        @RequiresApi(Build.VERSION_CODES.O)
//        suspend fun getSignInResultFromIntent(intent: Intent): SignInResult {
//            return try {
//                val credential = oneTapClient.getSignInCredentialFromIntent(intent) // Google kimlik bilgilerini al
//                val googleIdToken = credential.googleIdToken ?: throw Exception("Google ID Token is null")
//                // Google ID Token alınamazsa hata fırlat
//
//                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
//                // Firebase için Google kimlik bilgilerini oluştur
//
//                val user = auth.signInWithCredential(googleCredentials).await().user
//                // Firebase ile giriş yap ve kullanıcı bilgisini al
//
//                if (user != null) {
//                    SignInResult(
//                        data = UserData(
//                            userId = user.uid, // Kullanıcının Firebase UID'sini al
//                            username = user.displayName, // Kullanıcının Google adını al
//                            profilePictureUrl = user.photoUrl?.toString() // Kullanıcının profil fotoğrafı (URL)
//                        ),
//                        error = null // Hata yok
//                    )
//                } else {
//                    SignInResult(
//                        null,
//                        "Google ile giriş başarısız."
//                    ) // Kullanıcı bilgisi null ise hata döndür
//                }
//            } catch (e: Exception) {
//                e.printStackTrace() // Hata oluşursa log'a yaz
//                if (e is CancellationException) throw e // Coroutine iptal edildiyse tekrar fırlat
//                SignInResult(
//                    null,
//                    e.message ?: "Bir hata oluştu."
//                ) // Başarısız olursa hata mesajı döndür
//            }
//        }
//    }
//
//    /**
//     * Kullanıcı giriş sonucunu temsil eden veri sınıfı.
//     */
//    data class SignInResult(
//        val data: UserData?, // Kullanıcı bilgileri (başarılıysa)
//        val error: String? = null // Hata mesajı (varsa)
//    )
//
//    /**
//     * Giriş yapan kullanıcıyı temsil eden veri sınıfı.
//     */
//    data class UserData(
//        val userId: String, // Kullanıcının Firebase UID'si
//        val username: String?, // Kullanıcının adı (Google profili)
//        val profilePictureUrl: String? // Kullanıcının Google profil fotoğrafı URL'si
//    )
//
//
//}





