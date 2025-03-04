@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.mynoteapp.AppNavHost


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mynoteapp.Screens.LoadingScreen
import com.example.mynoteapp.Screens.LoginScreen

import com.example.mynoteapp.Screens.NoteDetails


import com.example.mynoteapp.Screens.NoteScreen
import com.example.mynoteapp.Screens.RegisterScreen
import com.example.mynoteapp.ViewModel.LoginViewModel
import com.example.mynoteapp.ViewModel.NoteViewModel
import com.example.mynoteapp.ViewModel.RegisterViewModel
import com.google.android.gms.auth.api.identity.Identity


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewModelConstructorInComposable", "ComposableDestinationInComposeScope")
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.LoginScreen.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier

    )
    {
        composable(route = NavigationItem.LoginScreen.route) {
            LoginScreen(navController, LoginViewModel())
        }
        composable(route = NavigationItem.RegisterScreen.route) {
            RegisterScreen(navController, RegisterViewModel())
        }
        composable(route = NavigationItem.LoadingScreen.route) {
            LoadingScreen(navController)
        }
        composable(route = NavigationItem.NoteScreen.route) {
            NoteScreen(navController, NoteViewModel())

        }
        composable(route = "${NavigationItem.NoteDetails.route}/{NoteID}") {
            NoteDetails(navController, NoteViewModel(), it.arguments?.getString("NoteID") ?: "")

        }

    }
}


