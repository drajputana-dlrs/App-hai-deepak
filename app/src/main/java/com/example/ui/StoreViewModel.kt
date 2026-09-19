package com.example.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CartItem
import com.example.data.CustomerProfile
import com.example.data.OrderItemRecord
import com.example.data.Product
import com.example.network.DeepakStoreApi
import com.example.repository.StoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed interface CheckoutStep {
  data object Catalog : CheckoutStep
  data object Cart : CheckoutStep
  data object Address : CheckoutStep
  data object Payment : CheckoutStep
  data class Success(val orderId: String, val total: Int, val paymentMethod: String, val itemsCount: Int) : CheckoutStep
}

enum class NavigationTab {
  HOME,
  CATEGORIES,
  CART,
  ORDERS
}

class StoreViewModel(private val repository: StoreRepository) : ViewModel() {
  companion object {
    const val MERCHANT_UPI = "6398499395@ybl"
    const val MERCHANT_PHONE = "6398499395"
    const val STORE_NAME = "Deepak Store"
  }

  // Navigation & Screen Flow
  private val _currentTab = MutableStateFlow(NavigationTab.HOME)
  val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

  private val _checkoutStep = MutableStateFlow<CheckoutStep>(CheckoutStep.Catalog)
  val checkoutStep: StateFlow<CheckoutStep> = _checkoutStep.asStateFlow()

  // Catalog
  private val _rawProducts = MutableStateFlow<List<Product>>(emptyList())
  val rawProducts: StateFlow<List<Product>> = _rawProducts.asStateFlow()

  private val _isLoadingProducts = MutableStateFlow(true)
  val isLoadingProducts: StateFlow<Boolean> = _isLoadingProducts.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  val categories: StateFlow<List<String>> = _rawProducts.combine(_rawProducts) { prods, _ ->
    val cats = prods.map { it.category }.filter { it.isNotBlank() }.distinct()
    listOf("All") + cats
  }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf("All"))

  val filteredProducts: StateFlow<List<Product>> = combine(
    _rawProducts,
    _selectedCategory,
    _searchQuery
  ) { prods, cat, query ->
    prods.filter { product ->
      val matchesCat = cat == "All" || product.category.equals(cat, ignoreCase = true)
      val matchesQuery = query.isBlank() ||
          product.name.contains(query, ignoreCase = true) ||
          product.category.contains(query, ignoreCase = true)
      matchesCat && matchesQuery
    }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  // Cart
  val cart: StateFlow<Map<String, CartItem>> = repository.cart

  val cartItemCount: StateFlow<Int> = cart.combine(cart) { map, _ ->
    map.values.sumOf { it.quantity }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

  val cartTotalPrice: StateFlow<Int> = cart.combine(cart) { map, _ ->
    map.values.sumOf { it.totalPrice }
  }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

  // Address & Customer Info
  private val _customerName = MutableStateFlow("")
  val customerName: StateFlow<String> = _customerName.asStateFlow()

  private val _customerPhone = MutableStateFlow("")
  val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

  private val _customerAddress = MutableStateFlow("")
  val customerAddress: StateFlow<String> = _customerAddress.asStateFlow()

  private val _isFetchingGps = MutableStateFlow(false)
  val isFetchingGps: StateFlow<Boolean> = _isFetchingGps.asStateFlow()

  // Payment
  private val _selectedPaymentMethod = MutableStateFlow("COD") // "COD", "UPI", "QR"
  val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod.asStateFlow()

  private val _isSubmittingOrder = MutableStateFlow(false)
  val isSubmittingOrder: StateFlow<Boolean> = _isSubmittingOrder.asStateFlow()

  // Order History
  private val _ordersHistory = MutableStateFlow<List<OrderItemRecord>>(emptyList())
  val ordersHistory: StateFlow<List<OrderItemRecord>> = _ordersHistory.asStateFlow()

  private val _isLoadingOrders = MutableStateFlow(false)
  val isLoadingOrders: StateFlow<Boolean> = _isLoadingOrders.asStateFlow()

  init {
    // Pre-populate customer profile from local storage
    val profile = repository.userProfile.value
    _customerName.value = profile.name
    _customerPhone.value = profile.phone
    _customerAddress.value = profile.address

    // Pre-populate local orders
    _ordersHistory.value = repository.localOrders.value

    // Fetch initial products
    loadProducts()

    // If phone exists, fetch cloud order history
    if (profile.phone.isNotBlank()) {
      refreshOrdersHistory(profile.phone)
    }
  }

  fun setTab(tab: NavigationTab) {
    _currentTab.value = tab
    if (tab == NavigationTab.CART) {
      _checkoutStep.value = CheckoutStep.Cart
    } else if (tab == NavigationTab.HOME || tab == NavigationTab.CATEGORIES) {
      if (_checkoutStep.value is CheckoutStep.Success) {
        _checkoutStep.value = CheckoutStep.Catalog
      }
    } else if (tab == NavigationTab.ORDERS) {
      refreshOrdersHistory(_customerPhone.value)
    }
  }

  fun setCheckoutStep(step: CheckoutStep) {
    _checkoutStep.value = step
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectCategory(category: String) {
    _selectedCategory.value = category
  }

  fun updateCart(product: Product, change: Int) {
    repository.updateCart(product, change)
  }

  fun updateCustomerName(name: String) {
    _customerName.value = name
  }

  fun updateCustomerPhone(phone: String) {
    _customerPhone.value = phone
  }

  fun updateCustomerAddress(address: String) {
    _customerAddress.value = address
  }

  fun selectPaymentMethod(method: String) {
    _selectedPaymentMethod.value = method
  }

  fun loadProducts() {
    viewModelScope.launch {
      _isLoadingProducts.value = true
      val list = DeepakStoreApi.fetchProducts()
      _rawProducts.value = list
      _isLoadingProducts.value = false
    }
  }

  fun getUpiUri(totalAmount: Int): String {
    return "upi://pay?pa=$MERCHANT_UPI&pn=DeepakStore&am=$totalAmount&cu=INR"
  }

  fun getQrCodeUrl(totalAmount: Int): String {
    val upiLink = getUpiUri(totalAmount)
    return "https://api.qrserver.com/v1/create-qr-code/?size=350x350&data=${URLEncoder.encode(upiLink, "UTF-8")}"
  }

  @SuppressLint("MissingPermission")
  fun fetchGpsCoordinates(context: Context) {
    viewModelScope.launch {
      _isFetchingGps.value = true
      try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        var bestLocation: Location? = null
        if (locationManager != null) {
          val providers = locationManager.getProviders(true)
          for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
              bestLocation = l
            }
          }
        }

        if (bestLocation != null) {
          val lat = String.format(Locale.US, "%.5f", bestLocation.latitude)
          val lng = String.format(Locale.US, "%.5f", bestLocation.longitude)
          val gpsMapLink = "http://maps.google.com/?q=$lat,$lng"
          val existing = _customerAddress.value.trim()
          val currentText = if (existing.contains("GPS:")) {
            existing.replace(Regex("GPS:\\s*http://maps\\.google\\.com/\\?q=[0-9.,-]+"), "GPS: $gpsMapLink")
          } else if (existing.isNotBlank()) {
            "$existing\nGPS: $gpsMapLink"
          } else {
            "GPS: $gpsMapLink\nLandmark: "
          }
          _customerAddress.value = currentText
        } else {
          val existing = _customerAddress.value.trim()
          if (!existing.contains("GPS:")) {
            _customerAddress.value = "$existing\nGPS: (Location fetched via device)"
          }
        }
      } catch (e: Exception) {
        Log.e("StoreViewModel", "Error fetching GPS", e)
      } finally {
        _isFetchingGps.value = false
      }
    }
  }

  fun saveCustomerProfile() {
    repository.saveProfile(
      name = _customerName.value,
      phone = _customerPhone.value,
      address = _customerAddress.value
    )
  }

  fun placeOrder(onUpiLaunch: ((Intent) -> Unit)? = null) {
    viewModelScope.launch {
      val name = _customerName.value.trim()
      val phone = _customerPhone.value.trim()
      val address = _customerAddress.value.trim()
      val total = cartTotalPrice.value
      val itemsList = cart.value.values.toList()
      val method = _selectedPaymentMethod.value

      if (itemsList.isEmpty() || total <= 0) return@launch

      // Save profile for auto-fill
      saveCustomerProfile()

      // If user selected UPI, launch native UPI payment app
      if (method == "UPI" && onUpiLaunch != null) {
        try {
          val upiUri = Uri.parse(getUpiUri(total))
          val intent = Intent(Intent.ACTION_VIEW, upiUri)
          onUpiLaunch(intent)
        } catch (e: Exception) {
          Log.e("StoreViewModel", "Cannot launch UPI intent", e)
        }
      }

      _isSubmittingOrder.value = true

      val orderId = "DS-${(100000..999999).random()}"
      val itemsString = itemsList.joinToString("\n") { "${it.quantity}x ${it.product.name} (₹${it.totalPrice})" }
      val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
      val currentDateStr = sdf.format(Date())

      val newOrderRecord = OrderItemRecord(
        orderId = orderId,
        date = currentDateStr,
        items = itemsString,
        total = total,
        status = "Processing",
        paymentMethod = method,
        customerName = name,
        customerPhone = phone,
        address = address
      )

      // Save locally first for instant zero-latency experience
      repository.addLocalOrder(newOrderRecord)
      val updatedList = mutableListOf(newOrderRecord)
      updatedList.addAll(_ordersHistory.value)
      _ordersHistory.value = updatedList.distinctBy { it.orderId }

      // Submit in background to Google Apps Script
      launch {
        DeepakStoreApi.submitOrder(
          orderId = orderId,
          customerName = name,
          customerPhone = phone,
          address = address,
          orderItems = itemsString,
          totalBill = total,
          paymentMethod = method
        )
      }

      // Clear Cart
      repository.clearCart()

      _isSubmittingOrder.value = false
      _checkoutStep.value = CheckoutStep.Success(
        orderId = orderId,
        total = total,
        paymentMethod = method,
        itemsCount = itemsList.sumOf { it.quantity }
      )
    }
  }

  fun refreshOrdersHistory(phone: String = _customerPhone.value) {
    if (phone.isBlank()) {
      _ordersHistory.value = repository.localOrders.value
      return
    }
    viewModelScope.launch {
      _isLoadingOrders.value = true
      val remoteList = DeepakStoreApi.fetchOrderHistory(phone.trim())
      val localList = repository.localOrders.value

      // Merge local and remote
      val combined = (localList + remoteList).distinctBy { it.orderId }
      _ordersHistory.value = combined
      _isLoadingOrders.value = false
    }
  }
}

class StoreViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val repository = StoreRepository(context.applicationContext)
    return StoreViewModel(repository) as T
  }
}
