package com.fintexinc.dashboard.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.presentation.ui.widget.ColumnWithShadow
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R

@Composable
internal fun Banner(
    closeBannerClick: () -> Unit,
) {
    ColumnWithShadow(
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .wrapContentHeight(),
                text = stringResource(R.string.text_you_saved),
                style = FontStyles.BodyLarge,
            )

            IconButton(
                onClick = closeBannerClick
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.description_icon_close),
                    tint = Colors.Text,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 40.dp),
            text = stringResource(R.string.text_money_stays),
            style = FontStyles.BodyMedium,
            color = Colors.TextSubdued,
        )
        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                painter = painterResource(R.drawable.ic_saved_background),
                contentDescription = stringResource(R.string.description_image_background_),
                contentScale = ContentScale.FillWidth
            )
            Image(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.TopCenter),
                painter = painterResource(R.drawable.ic_banner_image),
                contentDescription = stringResource(R.string.description_icon_folder)
            )
        }
    }
}