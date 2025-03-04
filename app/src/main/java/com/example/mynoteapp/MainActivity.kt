package com.example.mynoteapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mynoteapp.AppNavHost.AppNavHost
import com.example.mynoteapp.GoogleAuth.GoogleAuthUiClient
import com.example.mynoteapp.Screens.LoginScreen
import com.example.mynoteapp.ViewModel.LoginViewModel
import com.example.mynoteapp.ui.theme.MyNoteAPPTheme
import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyNoteAPPTheme {
                val navController = rememberNavController()


                Scaffold(modifier = Modifier.fillMaxSize()) {  innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)

                    )
                }
            }
        }
    }
}