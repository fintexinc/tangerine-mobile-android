package com.fintexinc.core.presentation.ui.widget.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.modifier.clickableShape
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun PrimaryButton(
    text: String,
    onClick : () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Colors.BackgroundPrimary,
                    shape = RoundedCornerShape(40.dp)
                )
                .clickableShape(RoundedCornerShape(40.dp)) {
                    onClick()
                }
                .padding(12.dp)
                .align(Alignment.Center),
            text = text,
            textAlign = TextAlign.Center,
            style = FontStyles.HeadingLarge,
            color = Colors.TextInverse
        )
    }
}