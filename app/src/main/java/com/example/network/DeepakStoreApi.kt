package com.example.network

import android.util.Log
import com.example.data.OrderItemRecord
import com.example.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DeepakStoreApi {
  private const val TAG = "DeepakStoreApi"
  private const val GET_PRODUCTS_URL =
    "https://script.google.com/macros/s/AKfycbxuozzP9B1a2M66ce2mU0A8KWJheJnACVawprMmtjCYm-tkipsuqLwvbzrpyc34gB1_ww/exec"
  private const val ORDERS_URL =
    "https://script.google.com/macros/s/AKfycbz-NmUsGxRG-0aAKkMiVE4svU-QRu2v21vn-4f2A0bKrLJ0vF6_Wgr3AnQEAt1JEb-bKA/exec"

  private val client: OkHttpClient by lazy {
    OkHttpClient.Builder()
      .connectTimeout(15, TimeUnit.SECONDS)
      .readTimeout(20, TimeUnit.SECONDS)
      .followRedirects(true)
      .followSslRedirects(true)
      .build()
  }

  suspend fun fetchProducts(): List<Product> = withContext(Dispatchers.IO) {
    try {
      val request = Request.Builder()
        .url(GET_PRODUCTS_URL)
        .header("Accept", "application/json")
        .build()

      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        val bodyString = response.body?.string() ?: ""
        if (bodyString.isNotBlank()) {
          val json = JSONObject(bodyString)
          val dataArray = json.optJSONArray("data") ?: JSONArray()
          val products = mutableListOf<Product>()
          for (i in 0 until dataArray.length()) {
            val item = dataArray.getJSONObject(i)
            val name = item.optString("Item_Name", "").trim()
            val priceStr = item.optString("Price", "0").replace(Regex("[^0-9]"), "")
            val price = priceStr.toIntOrNull() ?: 0
            val category = item.optString("Category", "General").trim()
            if (name.isNotBlank() && price > 0) {
              products.add(
                Product(
                  id = "P_${i + 1}",
                  name = name,
                  price = price,
                  category = cleanCategoryName(category)
                )
              )
            }
          }
          if (products.isNotEmpty()) {
            return@withContext products
          }
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error fetching products from API, using fallback store catalog", e)
    }
    return@withContext getCuratedDeepakStoreProducts()
  }

  suspend fun submitOrder(
    orderId: String,
    customerName: String,
    customerPhone: String,
    address: String,
    orderItems: String,
    totalBill: Int,
    paymentMethod: String
  ): Boolean = withContext(Dispatchers.IO) {
    try {
      val jsonBody = JSONObject().apply {
        put("orderId", orderId)
        put("customerName", customerName)
        put("customerPhone", customerPhone)
        put("address", address)
        put("orderItems", orderItems)
        put("totalBill", totalBill)
        put("paymentMethod", paymentMethod)
      }

      val mediaType = "application/json; charset=utf-8".toMediaType()
      val body = jsonBody.toString().toRequestBody(mediaType)
      val request = Request.Builder()
        .url(ORDERS_URL)
        .post(body)
        .build()

      val response = client.newCall(request).execute()
      Log.d(TAG, "Order submit response code: ${response.code}")
      return@withContext response.isSuccessful || response.code == 302
    } catch (e: Exception) {
      Log.e(TAG, "Failed to submit order to Apps Script", e)
      return@withContext false
    }
  }

  suspend fun fetchOrderHistory(phone: String): List<OrderItemRecord> = withContext(Dispatchers.IO) {
    if (phone.isBlank()) return@withContext emptyList()
    try {
      val url = "$ORDERS_URL?action=history&mobile=$phone"
      val request = Request.Builder().url(url).build()
      val response = client.newCall(request).execute()
      if (response.isSuccessful) {
        val body = response.body?.string() ?: ""
        if (body.isNotBlank() && body.startsWith("[")) {
          val array = JSONArray(body)
          val list = mutableListOf<OrderItemRecord>()
          for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
              OrderItemRecord(
                orderId = obj.optString("orderId", "DS-${100000 + i}"),
                date = obj.optString("date", "Today"),
                items = obj.optString("items", "Items summary"),
                total = obj.optString("total", "0").replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0,
                status = obj.optString("status", "Processing"),
                customerPhone = phone
              )
            )
          }
          return@withContext list
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error fetching order history", e)
    }
    return@withContext emptyList()
  }

  private fun cleanCategoryName(raw: String): String {
    val cleaned = raw.replace(Regex("[🔥🥤🛒🍟🚬💧📦✨]"), "").trim()
    return if (cleaned.isBlank()) "General" else cleaned
  }

  fun getCuratedDeepakStoreProducts(): List<Product> = listOf(
    Product("1", "Kurkure Masala Munch (85g)", 20, "Snacks & Namkeen"),
    Product("2", "Lay's India's Magic Masala", 20, "Snacks & Namkeen"),
    Product("3", "Thums Up Soft Drink (750ml)", 45, "Cold Drinks & Beverages"),
    Product("4", "Sprite Bottle (750ml)", 45, "Cold Drinks & Beverages"),
    Product("5", "Coca Cola Can (300ml)", 40, "Cold Drinks & Beverages"),
    Product("6", "Maggi 2-Minute Noodles (4 Pack)", 56, "Instant Food & Noodles"),
    Product("7", "Yippee Magic Masala Noodles", 14, "Instant Food & Noodles"),
    Product("8", "Britannia Good Day Butter (200g)", 35, "Biscuits & Cookies"),
    Product("9", "Parle-G Gold Glucose Biscuits", 25, "Biscuits & Cookies"),
    Product("10", "Haldiram's Bhujia Sev (200g)", 55, "Snacks & Namkeen"),
    Product("11", "Dettol Original Soap (3x125g)", 140, "Personal Care & Soaps"),
    Product("12", "Lifebuoy Total Soap (4 Pack)", 110, "Personal Care & Soaps"),
    Product("13", "Surf Excel Easy Wash Detergent (1kg)", 135, "Household & Cleaning"),
    Product("14", "Rin Detergent Bar (250g)", 20, "Household & Cleaning"),
    Product("15", "Tata Salt Iodized (1kg)", 28, "Kirana & Staples"),
    Product("16", "Fortune Sunlite Refined Oil (1L)", 145, "Kirana & Staples"),
    Product("17", "Aashirvaad Shudh Chakki Atta (5kg)", 240, "Kirana & Staples"),
    Product("18", "Madhur Pure & Hygienic Sugar (1kg)", 52, "Kirana & Staples"),
    Product("19", "Taj Mahal Tea (250g)", 165, "Tea & Coffee"),
    Product("20", "Nescafe Classic Instant Coffee (50g)", 175, "Tea & Coffee"),
    Product("21", "Amul Butter Pasteurized (100g)", 58, "Dairy & Breakfast"),
    Product("22", "Kissan Fresh Tomato Ketchup (500g)", 105, "Sauces & Spices"),
    Product("23", "Everest Garam Masala (100g)", 92, "Sauces & Spices"),
    Product("24", "Catch Red Chilli Powder (200g)", 80, "Sauces & Spices"),
    Product("25", "Cadbury Dairy Milk Silk (60g)", 85, "Chocolates & Sweets"),
    Product("26", "Colgate Strong Teeth Toothpaste (200g)", 125, "Personal Care & Soaps")
  )
}
