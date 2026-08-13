package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HomeScreen
import com.example.ui.ServerListScreen
import com.example.ui.ServerViewModel
import com.example.ui.theme.MyApplicationTheme

import com.example.ui.AboutScreen
import androidx.compose.material.icons.filled.Info

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var selectedTab by remember { mutableStateOf(0) }
        val viewModel: ServerViewModel = viewModel()

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          Scaffold(
            bottomBar = {
              NavigationBar(
                containerColor = Color.White
              ) {
                NavigationBarItem(
                  selected = selectedTab == 0,
                  onClick = { selectedTab = 0 },
                  icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                  label = { Text("Home") },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1E2438),
                    selectedTextColor = Color(0xFF1E2438),
                    indicatorColor = Color(0xFFE8ECF8)
                  ),
                  modifier = Modifier.testTag("tab_home")
                )
                NavigationBarItem(
                  selected = selectedTab == 1,
                  onClick = { selectedTab = 1 },
                  icon = { Icon(Icons.Default.Dns, contentDescription = "Servers") },
                  label = { Text("Servers") },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1E2438),
                    selectedTextColor = Color(0xFF1E2438),
                    indicatorColor = Color(0xFFE8ECF8)
                  ),
                  modifier = Modifier.testTag("tab_servers")
                )
                NavigationBarItem(
                  selected = selectedTab == 2,
                  onClick = { selectedTab = 2 },
                  icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                  label = { Text("About") },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1E2438),
                    selectedTextColor = Color(0xFF1E2438),
                    indicatorColor = Color(0xFFE8ECF8)
                  ),
                  modifier = Modifier.testTag("tab_about")
                )
              }
            }
          ) { paddingValues ->
            if (selectedTab == 0) {
              HomeScreen(
                viewModel = viewModel,
                onNavigateToServers = { selectedTab = 1 },
                modifier = Modifier.padding(paddingValues)
              )
            } else if (selectedTab == 1) {
              ServerListScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(paddingValues)
              )
            } else {
              AboutScreen(
                modifier = Modifier.padding(paddingValues)
              )
            }
          }
        }
      }
    }
  }
}


