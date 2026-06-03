package com.ccs.thaparbitesshop.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────
// Extended colors that live outside the M3 color scheme —
// shop status, order chips, FAB, top bar.
//
// Access via:  MaterialTheme.extendedColors.shopOpen
// ─────────────────────────────────────────────────────────────

@Immutable
data class ExtendedColors(
    // Shop open/closed/busy toggle
    val shopOpen           : Color,
    val shopOpenContainer  : Color,
    val shopClosed         : Color,
    val shopClosedContainer: Color,
    val shopBusy           : Color,
    val shopBusyContainer  : Color,

    // Order status chips
    val orderPending           : Color,
    val orderPendingContainer  : Color,
    val orderAccepted          : Color,
    val orderAcceptedContainer : Color,
    val orderReady             : Color,
    val orderReadyContainer    : Color,
    val orderCancelled         : Color,
    val orderCancelledContainer: Color,

    // Top bar background (always crimson)
    val topBarBackground: Color,
    val topBarContent   : Color,

    // FAB — "Add menu item"
    val fabBackground: Color,
    val fabContent   : Color,
)

val LightExtendedColors = ExtendedColors(
    shopOpen            = ShopOpenGreen,
    shopOpenContainer   = ShopOpenGreenContainer,
    shopClosed          = ShopClosedRed,
    shopClosedContainer = ShopClosedRedContainer,
    shopBusy            = ShopBusyAmber,
    shopBusyContainer   = ShopBusyAmberContainer,

    orderPending            = OrderPendingColor,
    orderPendingContainer   = OrderPendingContainer,
    orderAccepted           = OrderAcceptedColor,
    orderAcceptedContainer  = OrderAcceptedContainer,
    orderReady              = OrderReadyColor,
    orderReadyContainer     = OrderReadyContainer,
    orderCancelled          = OrderCancelledColor,
    orderCancelledContainer = OrderCancelledContainer,

    topBarBackground = Crimson500,
    topBarContent    = White,
    fabBackground    = Crimson500,
    fabContent       = White,
)

val DarkExtendedColors = ExtendedColors(
    shopOpen            = ShopOpenGreenDark,
    shopOpenContainer   = ShopOpenGreenContainerDark,
    shopClosed          = ShopClosedRedDark,
    shopClosedContainer = ShopClosedRedContainerDark,
    shopBusy            = ShopBusyAmberDark,
    shopBusyContainer   = ShopBusyAmberContainerDark,

    orderPending            = OrderPendingColorDark,
    orderPendingContainer   = OrderPendingContainerDark,
    orderAccepted           = OrderAcceptedColorDark,
    orderAcceptedContainer  = OrderAcceptedContainerDark,
    orderReady              = OrderReadyColorDark,
    orderReadyContainer     = OrderReadyContainerDark,
    orderCancelled          = OrderCancelledColorDark,
    orderCancelledContainer = OrderCancelledContainerDark,

    topBarBackground = Crimson700,   // slightly darker in dark mode
    topBarContent    = White,
    fabBackground    = Crimson300,
    fabContent       = Crimson900,
)

val LocalExtendedColors = compositionLocalOf { LightExtendedColors }