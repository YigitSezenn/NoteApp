package com.example.mynoteapp.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.R

import com.example.mynoteapp.ViewModel.LoginViewModel
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//val googleSignInLauncher =
//    registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            lifecycleScope.launch {
//                val signInResult = googleAuthHelper.getSignInResultFromIntent(result.data!!)
//                if (signInResult.data != null) {
//                    Toast.makeText(this@MainActivity, "Giriş Başarılı: ${signInResult.data.username}", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@MainActivity, "Hata: ${signInResult.error}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,

    ) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val auth = viewModel.auth


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background) // Açık gri arka plan
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Hoşgeldin GIF (İnternet veya local resource kullanabilirsin)

        GlideImage(
            imageModel = { "https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExajczbWt5d2pzdjZ2a2RmcjllNmZleTNndDl1ZnB1bGRvM2M5OXU3MSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/kLNTfS8SoJ1z6IT9uC/giphy.gif" },

            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome NoteApp",
            fontSize = 24.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

            }
        ) { }


        Spacer(modifier = Modifier.height(16.dp))
        Text("Text", textAlign = TextAlign.Center, color = Color.White)
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            maxLines = 1,
            label = { Text("Eposta Adresi") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            maxLines = 1,
            label = { Text("Parola") },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Password"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Lütfen Tüm Alanları Doldurun", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.LoginControllerModel(navController, email, password) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = (AppColors.ButtonColor)), // Yeşil buton
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Giriş Yap", fontSize = 18.sp, color = AppColors.TextColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button (Kayıt Ekranına Götür)
        TextButton(onClick = { navController.navigate(NavigationItem.RegisterScreen.route) }) {
            Text(text = "Hesabın Yok mu? Hemen Kayıt Ol", color = (AppColors.SecondaryText))
        }
    }
}
