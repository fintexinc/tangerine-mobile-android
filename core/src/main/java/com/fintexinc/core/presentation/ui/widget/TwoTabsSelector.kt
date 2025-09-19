package com.fintexinc.core.presentation.ui.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TabsSelector(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>
) {
    val selectedIndex = remember { mutableIntStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Colors.BackgroundSupplementary,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        tabs.forEachIndexed { index, tab ->
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f / tabs.size)
                    .padding(2.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        selectedIndex.value = index
                        tabs[index].onTabSelected?.invoke()
                    }
                    .then(
                        if (selectedIndex.value == index) {
                            Modifier
                                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                                .background(
                                    color = Colors.Background,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 0.5.dp,
                                    color = Colors.BorderSubdued,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        } else {
                            Modifier
                        }.padding(vertical = 6.dp)
                    ),
                text = tab.title,
                textAlign = TextAlign.Center,
                style = if (selectedIndex.value == index) FontStyles.BodyMediumBold else FontStyles.BodyMedium
            )
        }
    }

    AnimatedContent(
        targetState = selectedIndex.value,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)).togetherWith(fadeOut(animationSpec = tween(150)))
        },
        label = "tab_content"
    ) { index ->
        tabs[index].content()
    }
}

data class TabItem(
    val title: String,
    val content: @Composable () -> Unit,
    val onTabSelected: (() -> Unit)? = null,
)