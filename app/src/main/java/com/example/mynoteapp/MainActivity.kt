package com.NoteNest.mynoteapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.NoteNest.mynoteapp.R
import com.example.mynoteapp.AppNavHost.AppNavHost
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.GoogleAuth.GoogleAuthUiClient
import com.example.mynoteapp.ViewModel.LoginViewModel
import com.example.mynoteapp.theme.MyNoteAPPTheme

import com.google.android.gms.auth.api.identity.Identity

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyNoteAPPTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar =
                { MyBottom(navController) })


                { innerPadding ->


                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)

                    )
                }

            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MyBottom(navController: NavHostController) {
    val noteViewModel = LoginViewModel()
    val visibility = remember { mutableStateOf(false) }
    val selected = remember { mutableStateOf(Icons.Default.Home) }
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    visibility.value = currentRoute !in listOf(
        NavigationItem.LoginScreen.route,
        NavigationItem.RegisterScreen.route,
        NavigationItem.LoadingScreen.route
    )
    Box()
    {
        if (!visibility.value) return
        BottomAppBar(
            contentColor = Color.Black,
            containerColor = AppColors.ButtonColor

        ) {
            // Home button
            IconButton(
                onClick = {
                    selected.value = Icons.Default.Home
                    navController.navigate(NavigationItem.NoteScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.note),
                    contentDescription = "Notlarım",
                    modifier = Modifier.size(24.dp),
                )
            }

            // Tasks button
            IconButton(
                onClick = {
                    selected.value = Icons.Default.Home
                    navController.navigate(NavigationItem.TaskScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Tasklar",
                    tint = if (selected.value == Icons.Default.Home) Color.Black else Color.Gray
                )
            }

            // Logout button
            IconButton(
                onClick = {
                    // Handle logout logic
                    selected.value = Icons.Default.Close
                    noteViewModel.SignOut(
                        navController = navController
                    )

                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Çıkış Yap",
                    tint = if (selected.value == Icons.Default.Close) Color.Black else Color.Gray
                )
            }


        }
    }

}







