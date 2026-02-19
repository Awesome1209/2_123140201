package com.tugas2.newsfeed

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tugas2.newsfeed.ui.NewsFeedScreen

// Brown/Sepia lebih kuat (kertas koran jadul)
private val NewspaperColorScheme = lightColorScheme(
    primary = Color(0xFF3A2A1F),          // tinta coklat tua
    onPrimary = Color(0xFFF3E6CF),

    secondary = Color(0xFF6B4F3B),
    onSecondary = Color(0xFFF3E6CF),

    // BACKGROUND & SURFACE dibuat brown (bukan putih)
    background = Color(0xFFE7D3B1),       // kertas coklat muda
    onBackground = Color(0xFF221A14),

    surface = Color(0xFFE0C9A3),          // card/lembar lebih gelap sedikit
    onSurface = Color(0xFF221A14),

    surfaceVariant = Color(0xFFD7BF97),
    onSurfaceVariant = Color(0xFF2D2118),

    outline = Color(0xFF9B7F60)           // garis/outline coklat
)

// Typography serif “koran”
private val NewspaperTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    )
)

@Composable
fun App() {
    MaterialTheme(
        colorScheme = NewspaperColorScheme,
        typography = NewspaperTypography
    ) {
        NewsFeedScreen()
    }
}
