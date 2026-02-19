package com.tugas2.newsfeed

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "NewsFeedSimulator_Tugas2",
    ) {
        App()
    }
}