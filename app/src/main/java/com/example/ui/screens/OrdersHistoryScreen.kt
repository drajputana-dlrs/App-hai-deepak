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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.StoreViewModel
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurple
import com.example.ui.theme.BrandPurpleDark
import com.example.ui.theme.BrandPurpleLight

@Composable
fun OrdersHistoryScreen(
  viewModel: StoreViewModel,
  onNavigateToShop: () -> Unit,
  modifier: Modifier = Modifier
) {
  val orders by viewModel.ordersHistory.collectAsState()
  val isLoading by viewModel.isLoadingOrders.collectAsState()
  val currentPhone by viewModel.customerPhone.collectAsState()
  var searchPhone by remember(currentPhone) { mutableStateOf(currentPhone) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header matching orders.html (📦 मेरे पुराने ऑर्डर्स | 🔄 Refresh)
    Surface(
      color = BrandPurple,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "📦 मेरे पुराने ऑर्डर्स",
          color = Color.White,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color.White.copy(alpha = 0.2f),
          modifier = Modifier.clickable {
            viewModel.refreshOrdersHistory(searchPhone)
          }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            if (isLoading) {
              CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(14.dp),
                strokeWidth = 2.dp
              )
            } else {
              Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Refresh",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    // Phone search filter
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 1.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = searchPhone,
          onValueChange = { searchPhone = it },
          placeholder = { Text("Enter Mobile to check orders", fontSize = 12.sp) },
          leadingIcon = {
            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = BrandPurple)
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          singleLine = true,
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandPurple,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
          onClick = { viewModel.refreshOrdersHistory(searchPhone) },
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
          modifier = Modifier.height(52.dp)
        ) {
          Text("Find", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
      }
    }

    // Orders List or Empty State
    if (orders.isEmpty()) {
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
              .size(80.dp)
              .clip(CircleShape)
              .background(BrandPurpleLight),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.ShoppingBag,
              contentDescription = null,
              tint = BrandPurple,
              modifier = Modifier.size(42.dp)
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "कोई पुराना आर्डर नहीं मिला! 🛍️",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurple
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = if (searchPhone.isBlank())
              "आपने अभी तक इस डिवाइस से कोई आर्डर नहीं किया है।"
            else
              "मोबाइल नंबर $searchPhone से कोई पुराना आर्डर रिकॉर्ड नहीं मिला।",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = onNavigateToShop,
            colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("नया आर्डर करें / Shopping करें ➔", fontWeight = FontWeight.Bold)
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(orders, key = { it.orderId }) { order ->
          Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(modifier = Modifier.fillMaxWidth()) {
              // Left Accent Line matching orders.html (border-left: 5px solid #9c27b0)
              Box(
                modifier = Modifier
                  .width(5.dp)
                  .fillMaxSize()
                  .background(BrandPurple)
              )

              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp)
              ) {
                // Date & ID Row
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "📅 तारीख: ${order.date}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFFF3E5F5)
                  ) {
                    Text(
                      text = "ID: ${order.orderId}",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = BrandPurple,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Items list
                Text(
                  text = "🛒 सामान:",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = order.items,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface,
                  lineHeight = 18.sp,
                  modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Row: Total & Status Pill
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "💰 कुल बिल: ₹${order.total}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandGreen
                  )

                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BrandPurple
                  ) {
                    Text(
                      text = order.status,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color.White,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
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
}
