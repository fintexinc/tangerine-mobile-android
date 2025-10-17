package com.fintexinc.core.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun TangerineSnackbar(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Colors.BackgroundSecondary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            text = message,
            color = Colors.TextInverse,
            style = FontStyles.BodyLarge
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .clickable {
                    onAction?.invoke()
                },
            text = actionLabel ?: "",
            color = Colors.TextSupplementary,
            style = FontStyles.BodyMedium,
        )
    }
}