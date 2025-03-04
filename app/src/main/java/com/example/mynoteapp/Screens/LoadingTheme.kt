package com.example.mynoteapp.Screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.example.mynoteapp.AppSettings.AppColors
import kotlinx.coroutines.delay

private const val PADDING_PERCENTAGE_OUTER_CIRCLE = 0.15f
private const val PADDING_PERCENTAGE_INNER_CIRCLE = 0.3f
private const val POSITION_START_OFFSET_OUTER_CIRCLE = 90f
private const val POSITION_START_OFFSET_INNER_CIRCLE = 135f

@Composable
fun LoadingScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }

    // 3 saniye sonra login ekranına yönlendir
    LaunchedEffect(Unit) {
        delay(2000) // 3 saniye bekle
        isLoading = false
        navController.navigate(NavigationItem.LoginScreen.route) // Login ekranına yönlendir
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background),
        verticalArrangement = Arrangement.Center,

        ) {
        LoadingTheme(modifier = Modifier.fillMaxSize())
    }

}

@Composable
fun LoadingTheme(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, // Başlangıç açısı
        targetValue = 360f, // Tam tur dönüş
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing) // 1 saniyede dönüş
        ),
        label = "rotation animation"
    )

    var width by remember { mutableStateOf(40) } // Varsayılan değer



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background), // Arka plan rengi
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Orbit Animation
            Box(
                modifier = Modifier
                    .size(100.dp) // Daha büyük görünmesi için
                    .onSizeChanged {
                        width = it.width
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .graphicsLayer { rotationZ = rotation }
                )

                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(60.dp)
                        .graphicsLayer { rotationZ = -rotation }
                )

                CircularProgressIndicator(
                    strokeWidth = 3.dp,
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer { rotationZ = rotation }
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Animasyon ile yazı arasında boşluk

            // Altında "Uygulama Açılıyor..." yazısı
            Text(
                text = "Note App'e Hoşgeldiniz",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingThemePreview() {
    LoadingTheme(modifier = Modifier.fillMaxSize())

}