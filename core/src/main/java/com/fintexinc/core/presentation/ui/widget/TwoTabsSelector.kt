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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fintexinc.core.ui.color.Colors

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TwoTabsSelector(
    modifier: Modifier = Modifier,
    firstTabTitle: String,
    secondTabTitle: String,
    onFirstTabSelected: @Composable () -> Unit,
    onSecondTabSelected: @Composable () -> Unit,
) {
    val mode = remember {
        mutableStateOf<Mode>(Mode.FirstTab)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Colors.BackgroundSupplementary,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(0.5f)
                .padding(2.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    mode.value = Mode.FirstTab
                }
                .then(
                    if (mode.value is Mode.FirstTab) {
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
            text = firstTabTitle,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .weight(0.5f)
                .padding(2.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    mode.value = Mode.SecondTab
                }
                .then(
                    if (mode.value is Mode.SecondTab) {
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
            text = secondTabTitle,
            textAlign = TextAlign.Center
        )
    }

    AnimatedContent(
        targetState = mode.value,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)).togetherWith(fadeOut(animationSpec = tween(150)))
        },
        label = "tab_content"
    ) { currentMode ->
        when (currentMode) {
            is Mode.FirstTab -> onFirstTabSelected()
            is Mode.SecondTab -> onSecondTabSelected()
        }
    }
}

sealed class Mode {
    object FirstTab : Mode()
    object SecondTab : Mode()
}