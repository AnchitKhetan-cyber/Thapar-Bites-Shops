package com.ccs.thaparbitesshop.ui.theme

import com.ccs.thaparbitesshop.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// ─────────────────────────────────────────────────────────────
// FONT — Poppins (same as customer app)
//
// Place in res/font/:
//   poppins.ttf          (Regular 400)
//   poppins_medium.ttf   (Medium  500)
//   poppins_semibold.ttf (SemiBold 600)
//   poppins_bold.ttf     (Bold    700)
//
// Download: https://fonts.google.com/specimen/Poppins
// ─────────────────────────────────────────────────────────────

val Poppins = FontFamily(
    Font(R.font.poppins,          FontWeight.Normal),
    Font(R.font.poppins_medium,   FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold,     FontWeight.Bold),
)

// ─────────────────────────────────────────────────────────────
// TYPE SCALE
// Identical scale to customer app — same brand feel.
// Shop-specific usage notes added in comments.
// ─────────────────────────────────────────────────────────────

val ThaparBitesShopTypography = Typography(

    // Dashboard welcome heading  "Good morning, Nescafe ☀️"
    displayLarge = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Bold,
        fontSize      = 57.sp,
        lineHeight    = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Bold,
        fontSize      = 45.sp,
        lineHeight    = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 36.sp,
        lineHeight    = 44.sp,
        letterSpacing = 0.sp,
    ),

    // Screen titles e.g. "Manage Menu", "Incoming Orders"
    headlineLarge = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 32.sp,
        lineHeight    = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 28.sp,
        lineHeight    = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 24.sp,
        lineHeight    = 32.sp,
        letterSpacing = 0.sp,
    ),

    // TopAppBar title, order card header, dialog heading
    titleLarge = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.SemiBold,
        fontSize      = 22.sp,
        lineHeight    = 28.sp,
        letterSpacing = 0.sp,
    ),
    // Menu item names, order item names
    titleMedium = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Medium,
        fontSize      = 16.sp,
        lineHeight    = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    // Modifier / add-on sub-headings, section labels
    titleSmall = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Medium,
        fontSize      = 14.sp,
        lineHeight    = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    // Order detail text, item descriptions
    bodyLarge = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Normal,
        fontSize      = 16.sp,
        lineHeight    = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    // Customer name, item count, secondary info
    bodyMedium = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Normal,
        fontSize      = 14.sp,
        lineHeight    = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    // Timestamps, order ID, fine print
    bodySmall = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Normal,
        fontSize      = 12.sp,
        lineHeight    = 16.sp,
        letterSpacing = 0.4.sp,
    ),

    // Accept / Reject / Ready buttons
    labelLarge = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Medium,
        fontSize      = 14.sp,
        lineHeight    = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    // Status chips, filter tags, price tags
    labelMedium = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Medium,
        fontSize      = 12.sp,
        lineHeight    = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    // Tiny badges, ETA, item count bubble
    labelSmall = TextStyle(
        fontFamily    = Poppins,
        fontWeight    = FontWeight.Medium,
        fontSize      = 11.sp,
        lineHeight    = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)