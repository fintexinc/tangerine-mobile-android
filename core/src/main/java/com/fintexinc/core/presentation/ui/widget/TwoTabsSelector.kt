package com.fintexinc.core.presentation.ui.widget

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors
import com.fintexinc.core.ui.font.FontStyles

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TabsSelector(
    modifier: Modifier = Modifier,
    tabs: List<TabItem>,
    contentMaxHeight: Dp = Dp.Unspecified,
    shadowDivider: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .shadow(2.dp)
        )
    },
) {
    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Colors.Background,)
    ) {
        tabs.forEachIndexed { index, tab ->
            Column(
                modifier = Modifier
                    .weight(1f / tabs.size)
                    .wrapContentHeight()
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .clickable {
                                selectedIndex.intValue = index
                                tabs[index].onTabSelected?.invoke()
                            }
                            .padding(horizontal = 2.dp, vertical = 12.dp),
                        text = tab.title,
                        textAlign = TextAlign.Center,
                        style = if (selectedIndex.intValue == index) FontStyles.BodyMediumSemiBold else FontStyles.BodyMedium
                    )
                    if (selectedIndex.intValue == index) {
                        Box(
                            modifier = Modifier
                                .align(androidx.compose.ui.Alignment.BottomCenter)
                                .fillMaxWidth()
                                .height(3.dp)
                                .background(Colors.BackgroundSecondary, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
        }
    }

    shadowDivider()
    AnimatedContent(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (contentMaxHeight != Dp.Unspecified) {
                    Modifier.heightIn(max = contentMaxHeight)
                } else {
                    Modifier.wrapContentHeight()
                }
            )
            .background(color = Colors.BackgroundSubdued),
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