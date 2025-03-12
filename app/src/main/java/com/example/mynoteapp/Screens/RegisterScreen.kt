package com.example.mynoteapp.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.ViewModel.RegisterViewModel
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)

    var FirstNameAndSurname by remember { mutableStateOf("") }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background((AppColors.Background)) // Açık gri arka plan
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GlideImage(
            imageModel = { "https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExajczbWt5d2pzdjZ2a2RmcjllNmZleTNndDl1ZnB1bGRvM2M5OXU3MSZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/kLNTfS8SoJ1z6IT9uC/giphy.gif" },

            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Text(
            text = " NoteNest  ile Hesabını Oluştur",
            fontSize = 20.sp,
            color = (AppColors.SecondaryText)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // First Name & Surname Input
        OutlinedTextField(
            value = FirstNameAndSurname,
            onValueChange = { FirstNameAndSurname = it },
            label = { Text("AdSoyad")



                    },
            maxLines = 1,
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = "User") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Eposta Adresi") },
            maxLines = 1,
            leadingIcon = { Icon(imageVector = Icons.Filled.Email, contentDescription = "Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Parola") },
            maxLines = 1,
            leadingIcon = { Icon(imageVector = Icons.Filled.Lock, contentDescription = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && FirstNameAndSurname.isNotEmpty()) {
                    viewModel.registerUser(email, password,navController) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (message == "Register successful.Navigating to login screen") {
                            sharedPreferences.edit().putString("Firstname", FirstNameAndSurname).apply()


                            navController.navigate(NavigationItem.LoginScreen.route)
                        }
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.ButtonColor), // Yeşil buton
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign Up", fontSize = 18.sp, color = AppColors.TextColor)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Already have an account? Login
        TextButton(onClick = { navController.navigate(NavigationItem.LoginScreen.route) }) {
            Text(text = "Hesabın var mı? Hemen Giriş Yap", color = AppColors.SecondaryText)
        }
    }
}
