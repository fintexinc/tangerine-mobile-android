package com.fintexinc.tangerine.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import kotlinx.coroutines.delay

private const val SPLASH_SCREEN_DELAY = 3000L

@Composable
fun SplashScreenUI(onSplashComplete: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Colors.BackgroundPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo"
        )
    }
    LaunchedEffect(true) {
        delay(SPLASH_SCREEN_DELAY)
        onSplashComplete()
    }
}