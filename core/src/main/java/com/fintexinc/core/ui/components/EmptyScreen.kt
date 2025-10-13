package com.fintexinc.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun EmptyScreen(
    modifier: Modifier,
    title: Int = R.string.text_empty_transactions,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(title),
            color = Colors.Text,
            style = FontStyles.TitleSmall,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.text_try_again),
            color = Colors.TextSubdued,
            style = FontStyles.BodyLarge,
        )
        Spacer(modifier = Modifier.height(28.dp))

        Image(
            painter = painterResource(R.drawable.ic_empty),
            contentDescription = stringResource(R.string.description_empty_result)
        )
    }
}