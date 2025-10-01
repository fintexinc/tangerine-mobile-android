package com.tangerine.account.presentation.ui.bottom_tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.tangerine.account.R
import com.tangerine.account.presentation.ui.components.DataSectionRow

@Composable
internal fun DetailsUi(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val accountInfoItems = listOf(
            Triple(stringResource(R.string.text_account_number), "4000102187", false),
            Triple(stringResource(R.string.text_account_name), "Non-Registered Investment", false),
            Triple(stringResource(R.string.text_inception_date), "JAN 1, 2024", false),
            Triple(stringResource(R.string.text_beneficiaries), "TBC", false),
        )

        val profileInfo = listOf(
            Triple(stringResource(R.string.text_portfolio), "Balanced Income", false),
            Triple(stringResource(R.string.text_distributions), "Annually, December", false),
            Triple(stringResource(R.string.text_management_expense_ratio), "1.06%", true),
        )

        Text(
            text = stringResource(R.string.text_account_info),
            color = Colors.Text,
            style = FontStyles.TitleSmall,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        accountInfoItems.forEachIndexed { index, (label, value, hasInfo) ->

            DataSectionRow(
                label = label,
                value = value,
                valueColor = Colors.Text,
                valueNameColor = Colors.BorderInformation,
                hasInfoIcon = hasInfo,
                hasTrailingIcon = false,
                valueStyle = FontStyles.BodyLarge,
                labelStyle = FontStyles.BodyMedium,
                paddingValues = PaddingValues(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Colors.BorderSubdued,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = stringResource(R.string.text_portfolio_info),
            color = Colors.Text,
            style = FontStyles.TitleSmall,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))

        profileInfo.forEachIndexed { index, (label, value, hasInfo) ->
            DataSectionRow(
                label = label,
                value = value,
                valueColor = Colors.Text,
                valueNameColor = Colors.BorderInformation,
                hasInfoIcon = hasInfo,
                hasTrailingIcon = false,
                valueStyle = FontStyles.BodyLarge,
                labelStyle = FontStyles.BodyMedium,
                paddingValues = PaddingValues(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Colors.BorderSubdued,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}