package com.example.data

import java.net.URLEncoder

data class Product(
  val id: String = "",
  val name: String,
  val price: Int,
  val category: String,
  val imageUrl: String = "",
  val unit: String = "1 Unit"
) {
  val displayImageUrl: String
    get() = if (imageUrl.isNotBlank()) imageUrl
    else "https://tse2.mm.bing.net/th?q=${URLEncoder.encode("$name fmcg product india", "UTF-8")}&w=260&h=260&c=7"
}

data class CartItem(
  val product: Product,
  val quantity: Int = 1
) {
  val totalPrice: Int get() = product.price * quantity
}

data class OrderItemRecord(
  val orderId: String,
  val date: String,
  val items: String,
  val total: Int,
  val status: String,
  val paymentMethod: String = "COD",
  val customerName: String = "",
  val customerPhone: String = "",
  val address: String = ""
)

data class CustomerProfile(
  val name: String = "",
  val phone: String = "",
  val address: String = ""
)
