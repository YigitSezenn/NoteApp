package com.example.mynoteapp.AppSettings


import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object AppColors {
    val Background = Color(0xFF778899) // Maviye dönük duman grisi
    val ButtonColor = Color(0xFFE27D42) // Sıcak turuncu
    val SecondaryButton = Color(0xFFF4A261) // Pastel turuncu
    val DangerButton = Color(0xFFD95D39) // Koyu kırmızı-turuncu
    val TextColor = Color(0xFFFFFFFF) // Beyaz
    val SecondaryText = Color(0xFFE0E6ED) // Açık gri-mavi
}


object RandomColor {

    fun getRandomColor(): Color {
        val red = 0.5f + Random.nextFloat() * 0.3f  // 0.5 - 0.8
        val green = 0.5f + Random.nextFloat() * 0.3f
        val blue = 0.5f + Random.nextFloat() * 0.3f

        // Beyaz ile karıştır (pastelleştirme)
        val pastelRed = (red + 1.0f) / 2f
        val pastelGreen = (green + 1.0f) / 2f
        val pastelBlue = (blue + 1.0f) / 2f

        return Color(
            red = pastelRed,
            green = pastelGreen,
            blue = pastelBlue,
            alpha = 1f
        )
    }
}

