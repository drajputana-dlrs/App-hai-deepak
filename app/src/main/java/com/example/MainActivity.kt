package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.CheckoutStep
import com.example.ui.NavigationTab
import com.example.ui.StoreViewModel
import com.example.ui.StoreViewModelFactory
import com.example.ui.components.StoreBottomNav
import com.example.ui.components.StoreTopBar
import com.example.ui.screens.AddressScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderSuccessScreen
import com.example.ui.screens.OrdersHistoryScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.theme.DeepakStoreTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val viewModel: StoreViewModel by viewModels {
      StoreViewModelFactory(this)
    }

    setContent {
      DeepakStoreTheme {
        DeepakStoreApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun DeepakStoreApp(viewModel: StoreViewModel) {
  val currentTab by viewModel.currentTab.collectAsState()
  val checkoutStep by viewModel.checkoutStep.collectAsState()
  val cartItemCount by viewModel.cartItemCount.collectAsState()

  // Back Navigation Handling
  BackHandler(enabled = checkoutStep !is CheckoutStep.Catalog || currentTab != NavigationTab.HOME) {
    when (checkoutStep) {
      is CheckoutStep.Payment -> viewModel.setCheckoutStep(CheckoutStep.Address)
      is CheckoutStep.Address -> viewModel.setCheckoutStep(CheckoutStep.Cart)
      is CheckoutStep.Cart -> {
        viewModel.setCheckoutStep(CheckoutStep.Catalog)
        viewModel.setTab(NavigationTab.HOME)
      }
      is CheckoutStep.Success -> {
        viewModel.setCheckoutStep(CheckoutStep.Catalog)
        viewModel.setTab(NavigationTab.HOME)
      }
      is CheckoutStep.Catalog -> {
        if (currentTab != NavigationTab.HOME) {
          viewModel.setTab(NavigationTab.HOME)
        }
      }
    }
  }

  val isBrowsing = checkoutStep is CheckoutStep.Catalog

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      if (isBrowsing && currentTab != NavigationTab.ORDERS) {
        StoreTopBar(
          title = "✨ Deepak Store",
          subtitle = "दीपक स्टोर • Kirana & Essentials",
          cartItemCount = cartItemCount,
          onCartClick = {
            viewModel.setTab(NavigationTab.CART)
          }
        )
      }
    },
    bottomBar = {
      if (isBrowsing) {
        StoreBottomNav(
          currentTab = currentTab,
          cartItemCount = cartItemCount,
          onTabSelected = { tab ->
            viewModel.setTab(tab)
          },
          modifier = Modifier.navigationBarsPadding()
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = Pair(checkoutStep, currentTab),
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
      ) { (step, tab) ->
        when (step) {
          is CheckoutStep.Cart -> {
            CartScreen(
              viewModel = viewModel,
              onBack = {
                viewModel.setCheckoutStep(CheckoutStep.Catalog)
                viewModel.setTab(NavigationTab.HOME)
              },
              onContinueToAddress = {
                viewModel.setCheckoutStep(CheckoutStep.Address)
              }
            )
          }

          is CheckoutStep.Address -> {
            AddressScreen(
              viewModel = viewModel,
              onBack = {
                viewModel.setCheckoutStep(CheckoutStep.Cart)
              },
              onContinueToPayment = {
                viewModel.setCheckoutStep(CheckoutStep.Payment)
              }
            )
          }

          is CheckoutStep.Payment -> {
            PaymentScreen(
              viewModel = viewModel,
              onBack = {
                viewModel.setCheckoutStep(CheckoutStep.Address)
              }
            )
          }

          is CheckoutStep.Success -> {
            OrderSuccessScreen(
              orderId = step.orderId,
              total = step.total,
              paymentMethod = step.paymentMethod,
              itemsCount = step.itemsCount,
              onContinueShopping = {
                viewModel.setCheckoutStep(CheckoutStep.Catalog)
                viewModel.setTab(NavigationTab.HOME)
              },
              onViewOrders = {
                viewModel.setCheckoutStep(CheckoutStep.Catalog)
                viewModel.setTab(NavigationTab.ORDERS)
              }
            )
          }

          is CheckoutStep.Catalog -> {
            when (tab) {
              NavigationTab.HOME -> {
                HomeScreen(
                  viewModel = viewModel,
                  onNavigateToCart = {
                    viewModel.setTab(NavigationTab.CART)
                  }
                )
              }

              NavigationTab.CATEGORIES -> {
                CategoriesScreen(viewModel = viewModel)
              }

              NavigationTab.CART -> {
                CartScreen(
                  viewModel = viewModel,
                  onBack = {
                    viewModel.setTab(NavigationTab.HOME)
                  },
                  onContinueToAddress = {
                    viewModel.setCheckoutStep(CheckoutStep.Address)
                  }
                )
              }

              NavigationTab.ORDERS -> {
                OrdersHistoryScreen(
                  viewModel = viewModel,
                  onNavigateToShop = {
                    viewModel.setTab(NavigationTab.HOME)
                  }
                )
              }
            }
          }
        }
      }
    }
  }
}
