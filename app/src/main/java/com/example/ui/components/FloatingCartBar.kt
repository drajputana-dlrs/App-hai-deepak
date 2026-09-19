package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandGreen

@Composable
fun FloatingCartBar(
  itemCount: Int,
  totalPrice: Int,
  onViewCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = itemCount > 0,
    enter = slideInVertically(initialOffsetY = { it }),
    exit = slideOutVertically(targetOffsetY = { it }),
    modifier = modifier
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clickable { onViewCart() },
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = BrandGreen),
      elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "$itemCount ${if (itemCount == 1) "Item" else "Items"}",
              color = Color(0xFFD1FAE5),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "₹$totalPrice",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "View Cart",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
