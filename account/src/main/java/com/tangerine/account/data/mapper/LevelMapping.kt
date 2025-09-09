package com.tangerine.account.data.mapper

fun String.levelToUIString() {
    this.split("_")
        .joinToString(" ") {
            it.replaceFirstChar { char -> char.uppercase() }
        }
}