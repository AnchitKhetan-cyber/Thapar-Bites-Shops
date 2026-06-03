package com.ccs.thaparbitesshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ccs.thaparbitesshop.navigation.ShopNavGraph
import com.ccs.thaparbitesshop.ui.theme.ThaparBitesTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ThaparBitesTheme {

                val navController = rememberNavController()

                ShopNavGraph(
                    navController = navController
                )
            }
        }
    }
}

