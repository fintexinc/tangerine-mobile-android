package com.fintexinc.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import kotlinx.coroutines.delay

@Composable
fun CustomToast(
    message: String,
    actionText: String,
    onActionClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 30.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = Color.Black.copy(alpha = 0.30f),
                ambientColor = Color.Black.copy(alpha = 0.30f)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Colors.BackgroundSecondary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                style = FontStyles.BodyLarge,
                color = Colors.TextInverse,
            )

            Text(
                text = actionText,
                style = FontStyles.BodyMedium,
                color = Colors.BackgroundSecondaryDisabled,
                modifier = Modifier
                    .clickable { onActionClick() }
                    .padding(start = 16.dp)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(4000)
        onDismiss()
    }
}