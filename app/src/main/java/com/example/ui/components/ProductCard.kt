package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.Product
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandPurpleLight

@Composable
fun ProductCard(
  product: Product,
  quantityInCart: Int,
  onAddToCart: () -> Unit,
  onRemoveFromCart: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(4.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      // Product Image Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(115.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFFFAFAFA)),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(product.displayImageUrl)
            .crossfade(true)
            .build(),
          contentDescription = product.name,
          contentScale = ContentScale.Fit,
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(4.dp)
        )

        // Category Tag Top-Left
        Box(
          modifier = Modifier
            .align(Alignment.TopStart)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(BrandPurpleLight.copy(alpha = 0.9f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = product.category,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurpleDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Product Title
      Text(
        text = product.name,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          lineHeight = 17.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.height(36.dp)
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Price and Add/Quantity Stepper Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Price
        Column {
          Text(
            text = "₹${product.price}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BrandGreen
          )
        }

        // Stepper or ADD button
        AnimatedContent(
          targetState = quantityInCart,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "CartQuantityButton"
        ) { qty ->
          if (qty == 0) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandPurpleLight,
              border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.5f)),
              modifier = Modifier.clickable { onAddToCart() }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "ADD",
                  color = BrandPurple,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.size(2.dp))
                Icon(
                  imageVector = Icons.Default.Add,
                  contentDescription = "Add",
                  tint = BrandPurple,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          } else {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = BrandPurple,
              shadowElevation = 2.dp
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clickable { onRemoveFromCart() },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                }

                Text(
                  text = "$qty",
                  color = Color.White,
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp)
                )

                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clickable { onAddToCart() },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
