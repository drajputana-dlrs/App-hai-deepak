package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.StoreViewModel
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleLight

@Composable
fun CategoriesScreen(
  viewModel: StoreViewModel,
  modifier: Modifier = Modifier
) {
  val rawProducts by viewModel.rawProducts.collectAsState()
  val categories by viewModel.categories.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = "All Categories",
      fontSize = 20.sp,
      fontWeight = FontWeight.ExtraBold,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Browse through Deepak Store's wide range of groceries",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(bottom = 80.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(categories) { cat ->
        val count = if (cat == "All") rawProducts.size else rawProducts.count { it.category.equals(cat, true) }
        val iconEmoji = when {
          cat.contains("Drink", true) || cat.contains("Beverage", true) -> "🥤"
          cat.contains("Snack", true) || cat.contains("Namkeen", true) -> "🍟"
          cat.contains("Noodle", true) || cat.contains("Instant", true) -> "🍜"
          cat.contains("Biscuit", true) || cat.contains("Cookie", true) -> "🍪"
          cat.contains("Soap", true) || cat.contains("Personal", true) -> "🧼"
          cat.contains("Household", true) || cat.contains("Detergent", true) -> "🧺"
          cat.contains("Kirana", true) || cat.contains("Staples", true) -> "🌾"
          cat.contains("Tea", true) || cat.contains("Coffee", true) -> "☕"
          cat.contains("Dairy", true) -> "🧈"
          cat.contains("Spice", true) || cat.contains("Sauce", true) -> "🌶️"
          cat.contains("Sweet", true) || cat.contains("Chocolate", true) -> "🍫"
          else -> "🛒"
        }

        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              viewModel.selectCategory(cat)
              viewModel.setTab(NavigationTab.HOME)
            }
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(BrandPurpleLight),
                contentAlignment = Alignment.Center
              ) {
                Text(text = iconEmoji, fontSize = 22.sp)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = cat,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "$count items",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
              contentDescription = null,
              tint = BrandPurple,
              modifier = Modifier.size(12.dp)
            )
          }
        }
      }
    }
  }
}
