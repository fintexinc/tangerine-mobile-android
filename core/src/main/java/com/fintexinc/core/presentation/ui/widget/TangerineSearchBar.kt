package com.fintexinc.core.presentation.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintexinc.core.R
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@Composable
fun TangerineSearchBar(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    isShowFilter: Boolean = true,
    horizontalPadding: Dp = 16.dp,
) {
    val searchText = remember {
        mutableStateOf("")
    }
    Row(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight()
                .background(color = Colors.LightGray, shape = RoundedCornerShape(12.dp))
                .padding(8.dp),
            value = searchText.value,
            onValueChange = {
                searchText.value = it
            },
            cursorBrush = SolidColor(Colors.BrandBlack), // Set cursor color
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(R.string.description_icon_search),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchText.value.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                text = stringResource(R.string.text_search),
                                style = FontStyles.BodyLarge,
                                color = Colors.TextSubdued
                            )
                        }
                        innerTextField()
                    }
                }
            },
            textStyle = FontStyles.BodyLarge.copy(color = Colors.BrandBlack)
        )
        if (isShowFilter){
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                modifier = Modifier.wrapContentSize(),
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = stringResource(R.string.description_icon_filter),
                tint = Colors.BrandBlack
            )
        }
    }
}