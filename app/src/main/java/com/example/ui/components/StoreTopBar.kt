package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandCoral
import com.example.ui.theme.BrandPurple

@Composable
fun StoreTopBar(
  title: String = "✨ Deepak Store",
  subtitle: String = "दीपक स्टोर • Kirana & Essentials",
  cartItemCount: Int = 0,
  onCartClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = title,
          fontSize = 20.sp,
          fontWeight = FontWeight.ExtraBold,
          color = BrandPurple
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      IconButton(onClick = onCartClick) {
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
            tint = BrandPurple,
            modifier = Modifier.size(26.dp)
          )
        }
      }
    }
  }
}
