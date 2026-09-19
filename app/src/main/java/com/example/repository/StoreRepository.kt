package com.example.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.CartItem
import com.example.data.CustomerProfile
import com.example.data.OrderItemRecord
import com.example.data.Product
import com.example.network.DeepakStoreApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StoreRepository(private val context: Context) {
  private val prefs: SharedPreferences =
    context.getSharedPreferences("deepak_store_prefs", Context.MODE_PRIVATE)

  private val _cart = MutableStateFlow<Map<String, CartItem>>(emptyMap())
  val cart: StateFlow<Map<String, CartItem>> = _cart.asStateFlow()

  private val _userProfile = MutableStateFlow(loadProfile())
  val userProfile: StateFlow<CustomerProfile> = _userProfile.asStateFlow()

  private val _localOrders = MutableStateFlow(loadLocalOrders())
  val localOrders: StateFlow<List<OrderItemRecord>> = _localOrders.asStateFlow()

  init {
    loadSavedCart()
  }

  fun updateCart(product: Product, change: Int) {
    val current = _cart.value.toMutableMap()
    val existing = current[product.name]
    val newQty = (existing?.quantity ?: 0) + change

    if (newQty <= 0) {
      current.remove(product.name)
    } else {
      current[product.name] = CartItem(product = product, quantity = newQty)
    }
    _cart.value = current
    saveCart(current)
  }

  fun clearCart() {
    _cart.value = emptyMap()
    prefs.edit().remove("saved_cart_json").apply()
  }

  fun saveProfile(name: String, phone: String, address: String) {
    val profile = CustomerProfile(name = name.trim(), phone = phone.trim(), address = address.trim())
    _userProfile.value = profile
    prefs.edit()
      .putString("cust_name", profile.name)
      .putString("cust_phone", profile.phone)
      .putString("cust_address", profile.address)
      .apply()
  }

  private fun loadProfile(): CustomerProfile {
    val name = prefs.getString("cust_name", "") ?: ""
    val phone = prefs.getString("cust_phone", "") ?: ""
    val address = prefs.getString("cust_address", "") ?: ""
    return CustomerProfile(name, phone, address)
  }

  private fun saveCart(cartMap: Map<String, CartItem>) {
    try {
      val jsonArray = JSONArray()
      cartMap.values.forEach { item ->
        val obj = JSONObject().apply {
          put("name", item.product.name)
          put("price", item.product.price)
          put("category", item.product.category)
          put("qty", item.quantity)
          put("imageUrl", item.product.imageUrl)
        }
        jsonArray.put(obj)
      }
      prefs.edit().putString("saved_cart_json", jsonArray.toString()).apply()
    } catch (_: Exception) {}
  }

  private fun loadSavedCart() {
    try {
      val jsonStr = prefs.getString("saved_cart_json", null) ?: return
      val array = JSONArray(jsonStr)
      val map = mutableMapOf<String, CartItem>()
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        val name = obj.getString("name")
        val price = obj.getInt("price")
        val category = obj.optString("category", "General")
        val qty = obj.getInt("qty")
        val img = obj.optString("imageUrl", "")
        val prod = Product(id = "P_$i", name = name, price = price, category = category, imageUrl = img)
        map[name] = CartItem(product = prod, quantity = qty)
      }
      _cart.value = map
    } catch (_: Exception) {}
  }

  fun addLocalOrder(order: OrderItemRecord) {
    val current = _localOrders.value.toMutableList()
    current.add(0, order)
    _localOrders.value = current
    saveLocalOrders(current)
  }

  private fun saveLocalOrders(list: List<OrderItemRecord>) {
    try {
      val array = JSONArray()
      list.take(50).forEach { order ->
        val obj = JSONObject().apply {
          put("orderId", order.orderId)
          put("date", order.date)
          put("items", order.items)
          put("total", order.total)
          put("status", order.status)
          put("paymentMethod", order.paymentMethod)
          put("customerName", order.customerName)
          put("customerPhone", order.customerPhone)
          put("address", order.address)
        }
        array.put(obj)
      }
      prefs.edit().putString("saved_orders_json", array.toString()).apply()
    } catch (_: Exception) {}
  }

  private fun loadLocalOrders(): List<OrderItemRecord> {
    val list = mutableListOf<OrderItemRecord>()
    try {
      val jsonStr = prefs.getString("saved_orders_json", null) ?: return emptyList()
      val array = JSONArray(jsonStr)
      for (i in 0 until array.length()) {
        val obj = array.getJSONObject(i)
        list.add(
          OrderItemRecord(
            orderId = obj.optString("orderId", "DS-1001"),
            date = obj.optString("date", "Recent"),
            items = obj.optString("items", ""),
            total = obj.optInt("total", 0),
            status = obj.optString("status", "Processing"),
            paymentMethod = obj.optString("paymentMethod", "COD"),
            customerName = obj.optString("customerName", ""),
            customerPhone = obj.optString("customerPhone", ""),
            address = obj.optString("address", "")
          )
        )
      }
    } catch (_: Exception) {}
    return list
  }
}
