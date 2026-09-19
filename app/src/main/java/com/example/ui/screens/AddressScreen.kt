package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.StoreViewModel
import com.example.ui.theme.BrandPurple

@Composable
fun AddressScreen(
  viewModel: StoreViewModel,
  onBack: () -> Unit,
  onContinueToPayment: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val customerName by viewModel.customerName.collectAsState()
  val customerPhone by viewModel.customerPhone.collectAsState()
  val customerAddress by viewModel.customerAddress.collectAsState()
  val isFetchingGps by viewModel.isFetchingGps.collectAsState()
  val cartTotal by viewModel.cartTotalPrice.collectAsState()

  var errorMessage by remember { mutableStateOf<String?>(null) }

  // Location Permission Launcher
  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      viewModel.fetchGpsCoordinates(context)
      Toast.makeText(context, "GPS Location fetched!", Toast.LENGTH_SHORT).show()
    } else {
      Toast.makeText(context, "Please grant location permission to fetch GPS.", Toast.LENGTH_LONG).show()
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar matching address.html (< ADDRESS | STEP 2/3)
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
            text = "ADDRESS",
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
            text = "STEP 2/3",
            color = Color(0xFF5C6BC0),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }
    }

    // Form Body
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Delivery Information",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Saved automatically for quick future deliveries",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Full Name
          OutlinedTextField(
            value = customerName,
            onValueChange = {
              viewModel.updateCustomerName(it)
              errorMessage = null
            },
            label = { Text("Full Name") },
            placeholder = { Text("Enter your full name") },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = BrandPurple)
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BrandPurple,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Mobile Number
          OutlinedTextField(
            value = customerPhone,
            onValueChange = {
              viewModel.updateCustomerPhone(it)
              errorMessage = null
            },
            label = { Text("Mobile Number") },
            placeholder = { Text("10-digit mobile number") },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = BrandPurple)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BrandPurple,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(14.dp))

          // GPS Location Button matching user's address.html (.btn-loc)
          OutlinedButton(
            onClick = {
              val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
              ) == PackageManager.PERMISSION_GRANTED

              if (hasPermission) {
                viewModel.fetchGpsCoordinates(context)
                Toast.makeText(context, "Fetching GPS location...", Toast.LENGTH_SHORT).show()
              } else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
              }
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = Color(0xFFEEF2FF),
              contentColor = Color(0xFF5C6BC0)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF5C6BC0)),
            modifier = Modifier.fillMaxWidth()
          ) {
            if (isFetchingGps) {
              CircularProgressIndicator(
                color = Color(0xFF5C6BC0),
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Fetching GPS Location... ⏳",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            } else {
              Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "GPS",
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "🎯 Fetch GPS Location",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Address Textarea
          OutlinedTextField(
            value = customerAddress,
            onValueChange = {
              viewModel.updateCustomerAddress(it)
              errorMessage = null
            },
            label = { Text("Complete Address & Landmark") },
            placeholder = { Text("Enter House/Flat No, Street, Landmark...") },
            leadingIcon = {
              Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = BrandPurple)
            },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = BrandPurple,
              unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier.fillMaxWidth()
          )

          // Error Message Display
          if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFFEE2E2),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF87171)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = errorMessage ?: "",
                color = Color(0xFFB91C1C),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(10.dp)
              )
            }
          }
        }
      }
    }

    // Sticky Bottom Bar matching user's address.html
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
          onClick = {
            if (customerName.isBlank() || customerPhone.isBlank() || customerAddress.isBlank()) {
              errorMessage = "⚠️ कृपया अपना नाम, मोबाइल नंबर और एड्रेस सही से भरें ताकि हम डिलीवरी कर सकें!"
              Toast.makeText(context, "Please fill Name, Mobile & Address", Toast.LENGTH_SHORT).show()
            } else {
              viewModel.saveCustomerProfile()
              onContinueToPayment()
            }
          },
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
