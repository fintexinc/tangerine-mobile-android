package com.fintexinc.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    textModifier:Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    color: Color = Colors.BrandBlack,
    textColor: Color = Colors.Background,
    textStyle: TextStyle = FontStyles.BodyLargeBold
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = color
        ),
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
            modifier = textModifier,
        )
    }
}