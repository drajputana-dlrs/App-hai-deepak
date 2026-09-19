package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.ui.StoreViewModel
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurple

@Composable
fun PaymentScreen(
  viewModel: StoreViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val cartTotal by viewModel.cartTotalPrice.collectAsState()
  val selectedMethod by viewModel.selectedPaymentMethod.collectAsState()
  val isSubmitting by viewModel.isSubmittingOrder.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar matching payment.html (< PAYMENT METHOD | STEP 3/3)
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
            text = "PAYMENT METHOD",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFEEF2FF)
        ) {
          Text(
            text = "STEP 3/3",
            color = Color(0xFF5C6BC0),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }
    }

    // Payment Options Body
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      Text(
        text = "💳 Select Payment Method",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      // Option 1: COD (Recommended)
      PaymentOptionCard(
        title = "Cash on Delivery",
        subtitle = "Pay when you receive the order",
        badge = "✓ Recommended",
        badgeColor = BrandGreen,
        icon = Icons.Default.LocalAtm,
        isSelected = selectedMethod == "COD",
        onSelect = { viewModel.selectPaymentMethod("COD") }
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Option 2: Pay Online (UPI)
      PaymentOptionCard(
        title = "Pay Online (UPI)",
        subtitle = "Google Pay, PhonePe, Paytm, BHIM",
        badge = "Fast & Secure",
        badgeColor = BrandPurple,
        icon = Icons.Default.AccountBalanceWallet,
        isSelected = selectedMethod == "UPI",
        onSelect = { viewModel.selectPaymentMethod("UPI") }
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Option 3: Pay for Friend / Scan QR
      PaymentOptionCard(
        title = "Pay for Friend / Scan QR",
        subtitle = "Generate dynamic QR code to scan or share",
        badge = "QR Code",
        badgeColor = Color(0xFF3B82F6),
        icon = Icons.Default.QrCode2,
        isSelected = selectedMethod == "QR",
        onSelect = { viewModel.selectPaymentMethod("QR") }
      )

      // QR Code Box matching user's payment.html (#qr-box)
      AnimatedVisibility(visible = selectedMethod == "QR") {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
        ) {
          Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Scan or Share this UPI QR Code",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Take a screenshot or scan with any UPI app",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic QR Image from api.qrserver.com
            Box(
              modifier = Modifier
                .size(190.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(2.dp, BrandPurple.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              AsyncImage(
                model = ImageRequest.Builder(context)
                  .data(viewModel.getQrCodeUrl(cartTotal))
                  .crossfade(true)
                  .build(),
                contentDescription = "Dynamic UPI QR Code",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Merchant UPI details
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF3E8FF),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text("Merchant UPI ID", fontSize = 10.sp, color = BrandPurple)
                  Text(StoreViewModel.MERCHANT_UPI, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandPurple)
                }
                IconButton(
                  onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("UPI ID", StoreViewModel.MERCHANT_UPI))
                    Toast.makeText(context, "UPI ID copied!", Toast.LENGTH_SHORT).show()
                  }
                ) {
                  Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = BrandPurple)
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }

    // Sticky Bottom Bar matching user's payment.html
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
            text = "ORDER TOTAL",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurple
          )
        }

        Button(
          onClick = {
            viewModel.placeOrder(
              onUpiLaunch = { intent ->
                try {
                  context.startActivity(intent)
                } catch (_: Exception) {
                  Toast.makeText(context, "Opening payment...", Toast.LENGTH_SHORT).show()
                }
              }
            )
          },
          enabled = !isSubmitting,
          colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.padding(start = 16.dp)
        ) {
          if (isSubmitting) {
            CircularProgressIndicator(
              color = Color.White,
              modifier = Modifier.size(18.dp),
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Processing...")
          } else {
            val buttonText = when (selectedMethod) {
              "UPI" -> "Pay & Place Order"
              "QR" -> "Place Order"
              else -> "Place Order"
            }
            Text(
              text = buttonText,
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

@Composable
private fun PaymentOptionCard(
  title: String,
  subtitle: String,
  badge: String? = null,
  badgeColor: Color = BrandPurple,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) Color(0xFFFAF5FB) else MaterialTheme.colorScheme.surface
    ),
    border = androidx.compose.foundation.BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) BrandPurple else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelect() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (isSelected) BrandPurple else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = title,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            if (badge != null) {
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = badgeColor.copy(alpha = 0.15f)
              ) {
                Text(
                  text = badge,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = badgeColor,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = subtitle,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      RadioButton(
        selected = isSelected,
        onClick = onSelect,
        colors = RadioButtonDefaults.colors(
          selectedColor = BrandPurple,
          unselectedColor = MaterialTheme.colorScheme.outline
        )
      )
    }
  }
}
