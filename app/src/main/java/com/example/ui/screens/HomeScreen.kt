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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import com.example.ui.StoreViewModel
import com.example.ui.components.FloatingCartBar
import com.example.ui.components.ProductCard
import com.example.ui.components.StoreHeroBanner
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandPurpleLight

@Composable
fun HomeScreen(
  viewModel: StoreViewModel,
  onNavigateToCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  val products by viewModel.filteredProducts.collectAsState()
  val rawProducts by viewModel.rawProducts.collectAsState()
  val categories by viewModel.categories.collectAsState()
  val selectedCat by viewModel.selectedCategory.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val isLoading by viewModel.isLoadingProducts.collectAsState()
  val cart by viewModel.cart.collectAsState()
  val cartCount by viewModel.cartItemCount.collectAsState()
  val cartTotal by viewModel.cartTotalPrice.collectAsState()

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(bottom = 100.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      // 1. Storefront Hero Banner
      item(span = { GridItemSpan(2) }) {
        StoreHeroBanner()
      }

      // 2. Search Box
      item(span = { GridItemSpan(2) }) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { viewModel.setSearchQuery(it) },
          placeholder = {
            Text(
              text = "Search by item or brand (e.g. Maggi, Chips...)",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Search",
              tint = BrandPurple
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.setSearchQuery("") }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Clear",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandPurple,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
        )
      }

      // 3. Category Filter Chips
      item(span = { GridItemSpan(2) }) {
        LazyRow(
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(categories) { cat ->
            val isSelected = cat.equals(selectedCat, ignoreCase = true)
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
              else -> "🛍️"
            }

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) BrandPurple else MaterialTheme.colorScheme.surface,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) BrandPurple else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
              ),
              shadowElevation = if (isSelected) 2.dp else 0.dp,
              modifier = Modifier.clickable { viewModel.selectCategory(cat) }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = iconEmoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = cat,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // 4. Section Title & Item count
      item(span = { GridItemSpan(2) }) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (selectedCat == "All") "Popular Kirana & Grocery" else selectedCat,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${products.size} items",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // 5. Loading State
      if (isLoading && products.isEmpty()) {
        item(span = { GridItemSpan(2) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              CircularProgressIndicator(color = BrandPurple)
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Loading Deepak Store catalog...",
                fontSize = 13.sp,
                color = BrandPurpleDark,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      // 6. Empty State
      if (!isLoading && products.isEmpty()) {
        item(span = { GridItemSpan(2) }) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(220.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(24.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = BrandPurple.copy(alpha = 0.5f),
                modifier = Modifier.size(54.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "No products found",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Try searching with different keywords or select another category.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }
      }

      // 7. Products Grid Items
      items(products, key = { it.name }) { prod ->
        val qtyInCart = cart[prod.name]?.quantity ?: 0
        ProductCard(
          product = prod,
          quantityInCart = qtyInCart,
          onAddToCart = { viewModel.updateCart(prod, 1) },
          onRemoveFromCart = { viewModel.updateCart(prod, -1) },
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
      }
    }

    // Floating Cart at the bottom
    FloatingCartBar(
      itemCount = cartCount,
      totalPrice = cartTotal,
      onViewCart = onNavigateToCart,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 12.dp)
    )
  }
}
