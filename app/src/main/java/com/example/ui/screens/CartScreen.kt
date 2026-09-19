package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.CheckoutStep
import com.example.ui.StoreViewModel
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandPurpleLight

@Composable
fun CartScreen(
  viewModel: StoreViewModel,
  onBack: () -> Unit,
  onContinueToAddress: () -> Unit,
  modifier: Modifier = Modifier
) {
  val cartMap by viewModel.cart.collectAsState()
  val cartTotal by viewModel.cartTotalPrice.collectAsState()
  val cartItems = cartMap.values.toList()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar matching user's cart.html (< CART | STEP 1/3)
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.clickable { onBack() }
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "CART",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFEEF2FF)
        ) {
          Text(
            text = "STEP 1/3",
            color = Color(0xFF5C6BC0),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }
    }

    if (cartItems.isEmpty()) {
      // Empty Cart View
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(32.dp)
        ) {
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(BrandPurpleLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = BrandPurple,
              modifier = Modifier.size(48.dp)
            )
          }
          Spacer(modifier = Modifier.height(18.dp))
          Text(
            text = "Your Cart is Empty",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Add delicious snacks, cold drinks and fresh groceries from Deepak Store!",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Start Shopping ➔", fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      // Cart Items & Bill Details
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .padding(horizontal = 16.dp, vertical = 10.dp)
      ) {
        item {
          Text(
            text = "Selected Items (${cartItems.size})",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
          )
        }

        // Cart items list
        items(cartItems, key = { it.product.name }) { item ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Product Image
              Box(
                modifier = Modifier
                  .size(60.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFFAFAFA)),
                contentAlignment = Alignment.Center
              ) {
                AsyncImage(
                  model = ImageRequest.Builder(LocalContext.current)
                    .data(item.product.displayImageUrl)
                    .crossfade(true)
                    .build(),
                  contentDescription = item.product.name,
                  contentScale = ContentScale.Fit,
                  modifier = Modifier.size(54.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.product.name,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "₹${item.product.price} each",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Total: ₹${item.totalPrice}",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = BrandGreen
                )
              }

              // Stepper
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = BrandPurpleLight,
                border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.4f))
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                  IconButton(
                    onClick = { viewModel.updateCart(item.product, -1) },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Remove,
                      contentDescription = "Decrease",
                      tint = BrandPurple,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                  Text(
                    text = "${item.quantity}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPurpleDark,
                    modifier = Modifier.padding(horizontal = 6.dp)
                  )
                  IconButton(
                    onClick = { viewModel.updateCart(item.product, 1) },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Add,
                      contentDescription = "Increase",
                      tint = BrandPurple,
                      modifier = Modifier.size(16.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // Bill Details Card
        item {
          Spacer(modifier = Modifier.height(14.dp))
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                text = "Bill Details",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Item Total", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("₹$cartTotal", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Delivery Fee", fontSize = 14.sp, color = BrandGreen)
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFE8F5E9)
                ) {
                  Text(
                    text = "FREE",
                    color = BrandGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(12.dp))
              HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
              Spacer(modifier = Modifier.height(12.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Grand Total",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "₹$cartTotal",
                  fontSize = 18.sp,
                  fontWeight = FontWeight.Black,
                  color = BrandGreen
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(20.dp))
        }
      }

      // Sticky Bottom Bar matching user's cart.html
      Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
        modifier = Modifier
          .fillMaxWidth()
          .navigationBarsPadding()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "₹$cartTotal",
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "TOTAL PAYABLE",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = BrandPurple
            )
          }

          Button(
            onClick = onContinueToAddress,
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(start = 16.dp)
          ) {
            Text(
              text = "Continue ➔",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}
