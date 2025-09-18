package com.fintexinc.core.ui.font

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fintexinc.core.R

object FontStyles {
    private val tangerineCircularBlack = FontFamily(
        Font(R.font.tangerine_circular_black, FontWeight.Black)
    )
    private val tangerineCircularBold = FontFamily(
        Font(R.font.tangerine_circular_bold, FontWeight.Bold)
    )
    private val tangerineCircularBook = FontFamily(
        Font(R.font.tangerine_circular_book, FontWeight.W400)
    )
    private val tangerineCircularMedium = FontFamily(
        Font(R.font.tangerine_circular_medium, FontWeight.Medium)
    )
    private val tangerineCircularBlackItalic = FontFamily(
        Font(R.font.tangerine_circular_black_italic, FontWeight.Black, FontStyle.Italic)
    )
    private val tangerineCircularBoldItalic = FontFamily(
        Font(R.font.tangerine_circular_bold_italic, FontWeight.Bold, FontStyle.Italic)
    )
    private val tangerineCircularBookItalic = FontFamily(
        Font(R.font.tangerine_circular_book_italic, FontWeight.W400, FontStyle.Italic)
    )
    private val tangerineCircularMediumItalic = FontFamily(
        Font(R.font.tangerine_circular_medium_italic, FontWeight.Medium, FontStyle.Italic)
    )

    val TitleSmall = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 14.sp,
        lineHeight = 19.sp
    )
    val TitleLarge = TextStyle(
        fontFamily = tangerineCircularMedium,
        fontSize = 25.sp,
        lineHeight = 30.sp
    )
    val TitleMediumBold = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    val TitleMediumMedium = TextStyle(
        fontFamily = tangerineCircularMedium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    )
    val BodyLargeBold = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val BodyLarge = TextStyle(
        fontFamily = tangerineCircularBook,
        fontSize = 16.sp,
        lineHeight = 21.sp
    )
    val BodyMedium = TextStyle(
        fontFamily = tangerineCircularBook,
        fontSize = 14.sp,
        lineHeight = 19.sp
    )
    val BodyMediumBold = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 14.sp,
        lineHeight = 19.sp
    )

    val BodySmall = TextStyle(
        fontFamily = tangerineCircularBook,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )

    val BodyExtraSmall = TextStyle(
        fontFamily = tangerineCircularBook,
        fontSize = 10.sp,
        lineHeight = 12.sp
    )

    val BodySmallBold = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
    val DisplaySmall = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 32.sp,
        lineHeight = 39.sp
    )
    val HeadingLarge = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
    val HeadingRegular = TextStyle(
        fontFamily = tangerineCircularMedium,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
    val HeadingMedium = TextStyle(
        fontFamily = tangerineCircularBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )

    val HeadingMediumRegular = TextStyle(
        fontFamily = tangerineCircularMedium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
}