package com.example.mynoteapp.AppNavHost

enum class AppNavigation {
    LoginScreen,
    RegisterScreen,
    NoteScreen,
    NoteDetails,
    LoadingScreen
}

sealed class NavigationItem(val route: String) {
    object LoginScreen : NavigationItem(AppNavigation.LoginScreen.name)
    object RegisterScreen : NavigationItem(AppNavigation.RegisterScreen.name)
    object NoteScreen : NavigationItem(AppNavigation.NoteScreen.name)
    object NoteDetails : NavigationItem(AppNavigation.NoteDetails.name)
    object LoadingScreen : NavigationItem(AppNavigation.LoadingScreen.name)
}