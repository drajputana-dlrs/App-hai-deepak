package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight

@Composable
fun StoreBottomNav(
  currentTab: NavigationTab,
  cartItemCount: Int,
  onTabSelected: (NavigationTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  ) {
    // 1. Home
    NavigationBarItem(
      selected = currentTab == NavigationTab.HOME,
      onClick = { onTabSelected(NavigationTab.HOME) },
      icon = {
        Icon(
          imageVector = Icons.Default.Home,
          contentDescription = "Home",
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "Home",
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavigationTab.HOME) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BrandPurple,
        selectedTextColor = BrandPurple,
        indicatorColor = BrandPurpleLight,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )

    // 2. Categories
    NavigationBarItem(
      selected = currentTab == NavigationTab.CATEGORIES,
      onClick = { onTabSelected(NavigationTab.CATEGORIES) },
      icon = {
        Icon(
          imageVector = Icons.Default.Category,
          contentDescription = "Categories",
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "Categories",
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavigationTab.CATEGORIES) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BrandPurple,
        selectedTextColor = BrandPurple,
        indicatorColor = BrandPurpleLight,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )

    // 3. Cart
    NavigationBarItem(
      selected = currentTab == NavigationTab.CART,
      onClick = { onTabSelected(NavigationTab.CART) },
      icon = {
        BadgedBox(
          badge = {
            if (cartItemCount > 0) {
              Badge(
                containerColor = BrandCoral,
                contentColor = Color.White
              ) {
                Text(
                  text = "$cartItemCount",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            modifier = Modifier.size(22.dp)
          )
        }
      },
      label = {
        Text(
          text = "Cart",
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavigationTab.CART) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BrandPurple,
        selectedTextColor = BrandPurple,
        indicatorColor = BrandPurpleLight,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )

    // 4. Orders
    NavigationBarItem(
      selected = currentTab == NavigationTab.ORDERS,
      onClick = { onTabSelected(NavigationTab.ORDERS) },
      icon = {
        Icon(
          imageVector = Icons.Default.ReceiptLong,
          contentDescription = "Orders",
          modifier = Modifier.size(22.dp)
        )
      },
      label = {
        Text(
          text = "Orders",
          fontSize = 11.sp,
          fontWeight = if (currentTab == NavigationTab.ORDERS) FontWeight.Bold else FontWeight.Normal
        )
      },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BrandPurple,
        selectedTextColor = BrandPurple,
        indicatorColor = BrandPurpleLight,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
  }
}
