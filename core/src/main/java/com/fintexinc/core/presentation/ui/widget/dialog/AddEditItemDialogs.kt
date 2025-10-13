package com.fintexinc.core.presentation.ui.widget.dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.fintexinc.core.R
import com.fintexinc.core.presentation.ui.widget.button.SecondaryButton
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun UpdatePopup(
    @DrawableRes imageRes: Int,
    imageContentDescription: String,
    title: String,
    text: String,
    onClick: () -> Unit
) {
    Popup(
        alignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .shadow(8.dp, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp)
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(start = 32.dp, top = 40.dp, end = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(imageRes),
                contentDescription = imageContentDescription
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = FontStyles.TitleMedium,
                color = Colors.Text,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Colors.BackgroundSecondary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        onClick()
                    }
                    .padding(vertical = 12.dp),
                text = stringResource(R.string.text_back),
                style = FontStyles.HeadingLarge,
                color = Colors.TextInverse,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DeletePopup(
    @DrawableRes imageRes: Int,
    imageContentDescription: String,
    title: String,
    text: String,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    isSecondButtonVisible: Boolean = true,
    approvedButtonText: Int = R.string.text_zero_out_today,
) {
    Popup(
        alignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .shadow(8.dp, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp)
                .background(
                    color = Colors.Background,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(start = 32.dp, top = 40.dp, end = 32.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(imageRes),
                contentDescription = imageContentDescription
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = FontStyles.TitleMedium,
                color = Colors.Text,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Colors.BackgroundSecondary,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        onDeleteClick()
                    }
                    .padding(vertical = 12.dp),
                text = stringResource(approvedButtonText),
                style = FontStyles.HeadingLarge,
                color = Colors.TextInverse,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isSecondButtonVisible) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Colors.BackgroundSecondary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onDeleteClick()
                        }
                        .padding(vertical = 12.dp),
                    text = stringResource(R.string.text_delete_with_history),
                    style = FontStyles.HeadingLarge,
                    color = Colors.TextInverse,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            SecondaryButton(
                text = stringResource(R.string.text_cancel),
                onClick = {
                    onCancelClick()
                }
            )
        }
    }
}
