package com.fintexinc.dashboard.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fintexinc.core.data.utils.currency.formatCurrency
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles
import com.fintexinc.dashboard.R

@Composable
internal fun TransactionItemUI(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_transaction),
            tint = Color.Unspecified,
            contentDescription = stringResource(R.string.description_icon_transaction),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.transactionDescription,
                style = FontStyles.BodyLarge,
                color = Colors.BrandBlack,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = transaction.investmentDetails.fundName,
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = transaction.transactionAmount.formatCurrency(),
                style = FontStyles.BodyLargeBold,
                color = Colors.BrandBlack,
            )

            Text(
                text = transaction.grossAmount.formatCurrency(),
                style = FontStyles.BodyMedium,
                color = Colors.TextSubdued,
            )
        }

        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(24.dp)
                .padding(start = 8.dp),
            painter = painterResource(com.fintexinc.core.R.drawable.ic_arrow_right),
            tint = Colors.BrandGray,
            contentDescription = stringResource(R.string.description_icon_open),
        )
    }
}