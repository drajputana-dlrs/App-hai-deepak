package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandPurple

@Composable
fun OrderSuccessScreen(
  orderId: String,
  total: Int,
  paymentMethod: String,
  itemsCount: Int,
  onContinueShopping: () -> Unit,
  onViewOrders: () -> Unit,
  modifier: Modifier = Modifier
) {
  var animatedIn by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (animatedIn) 1f else 0.4f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
    label = "SuccessCheckScale"
  )

  LaunchedEffect(Unit) {
    animatedIn = true
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Big Animated Green Checkmark
    Box(
      modifier = Modifier
        .size(96.dp)
        .scale(scale)
        .clip(CircleShape)
        .background(Color(0xFFE8F5E9)),
      contentAlignment = Alignment.Center
    ) {
      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(BrandGreen),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "Success",
          tint = Color.White,
          modifier = Modifier.size(44.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "Order Successful! 🎉",
      fontSize = 24.sp,
      fontWeight = FontWeight.ExtraBold,
      color = BrandGreen
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "आपका आर्डर रिसीव हो गया है और दीपक स्टोर तथा Telegram पर डिटेल भेज दी गई है।",
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
      lineHeight = 18.sp,
      modifier = Modifier.padding(horizontal = 12.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Order Details Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Order ID",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFF3E5F5)
          ) {
            Text(
              text = orderId,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = BrandPurple,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Total Paid / Payable",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "₹$total",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BrandGreen
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Payment Mode",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = paymentMethod,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Delivery info
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEFF6FF),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocalShipping,
              contentDescription = null,
              tint = Color(0xFF2563EB),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Estimated Delivery: 15-30 Minutes",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1E40AF)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))

    // Actions
    Button(
      onClick = onContinueShopping,
      colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
      Text(
        text = "Continue Shopping",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedButton(
      onClick = onViewOrders,
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPurple),
      border = androidx.compose.foundation.BorderStroke(1.dp, BrandPurple),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
    ) {
      Text(
        text = "📦 View My Orders",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
