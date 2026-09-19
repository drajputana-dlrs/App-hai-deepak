package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.StoreViewModel

@Composable
fun StoreHeroBanner(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF240046))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              Color(0xFF3B0764),
              Color(0xFF1E0038),
              Color(0xFF10002B)
            )
          )
        )
        .padding(16.dp)
    ) {
      Column {
        // Shop LED Sign Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF8E24AA)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = "Store Icon",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "DEEPAK STORE",
                color = Color(0xFFFFD166),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
              )
              Text(
                text = "दीपक स्टोर (GENERAL & KIRANA ITEMS)",
                color = Color(0xFFF3E5F5),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          // Call button
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF059669),
            modifier = Modifier.clickable {
              val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+91${StoreViewModel.MERCHANT_PHONE}"))
              context.startActivity(intent)
            }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call Store",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Call Shop",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Feature Highlights
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Open Badge
          Row(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0x33059669))
              .border(1.dp, Color(0xFF059669), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = Color(0xFF34D399),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "STORE OPEN",
                color = Color(0xFF34D399),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Instant Kirana",
                color = Color(0xFFE2E8F0),
                fontSize = 9.sp
              )
            }
          }

          // Fast Delivery Badge
          Row(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0x33F59E0B))
              .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp))
              .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.ElectricBolt,
              contentDescription = null,
              tint = Color(0xFFFBBF24),
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "FREE DELIVERY",
                color = Color(0xFFFBBF24),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "15-30 Mins",
                color = Color(0xFFE2E8F0),
                fontSize = 9.sp
              )
            }
          }
        }
      }
    }
  }
}
