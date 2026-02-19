package com.tugas2.newsfeed

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform