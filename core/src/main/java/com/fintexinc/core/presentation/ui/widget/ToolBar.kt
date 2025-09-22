package com.fintexinc.core.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun ToolBar(
    text: String,
    textColor: Color = Colors.BrandBlack,
    backgroundColor: Color = Colors.Background,
    leftIcon: (@Composable () -> Unit)? = null,
    rightIcon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Spacer(modifier = Modifier.width(30.dp))

        leftIcon?.invoke()

        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 22.dp),
            style = FontStyles.TitleSmallRegular,
            color = textColor,
            textAlign = TextAlign.Center
        )

        rightIcon?.invoke()

        Spacer(modifier = Modifier.width(30.dp))

    }
}

@Preview(name = "No Icons", showBackground = true)
@Composable
fun ToolBarPreview_NoIcons() {
    ToolBar(text = "Home")
}

@Preview(name = "Left Icon Only", showBackground = true)
@Composable
fun ToolBarPreview_LeftIconOnly() {
    ToolBar(
        text = "Settings",
        leftIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Preview(name = "Right Icon Only", showBackground = true)
@Composable
fun ToolBarPreview_RightIconOnly() {
    ToolBar(
        text = "Profile",
        rightIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = "Menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Preview(name = "Both Icons", showBackground = true)
@Composable
fun ToolBarPreview_BothIcons() {
    ToolBar(
        text = "Account Summary",
        leftIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        rightIcon = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "More",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}
