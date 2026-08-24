package com.example.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.CartItem
import com.example.ui.theme.*

@Composable
fun CustomerCartSheet(
  cartItems: List<CartItem>,
  onDismiss: () -> Unit,
  onRemoveItem: (String) -> Unit,
  onCheckout: () -> Unit
) {
  val subtotal = cartItems.sumOf { it.product.price * it.quantity }
  val creatorShare = subtotal * 0.85

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.85f)
        .testTag("cart_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AceGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Shopping Cart (${cartItems.size})",
              color = AceTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = AceTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(10.dp))

        if (cartItems.isEmpty()) {
          Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(Icons.Default.RemoveShoppingCart, contentDescription = null, tint = AceTextMuted, modifier = Modifier.size(40.dp))
              Spacer(modifier = Modifier.height(8.dp))
              Text("Your cart is empty.", color = AceTextSecondary, fontSize = 13.sp)
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(cartItems) { item ->
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = AceDarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
              ) {
                Row(
                  modifier = Modifier.padding(10.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Image(
                    painter = painterResource(id = item.product.coverDrawableRes),
                    contentDescription = item.product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .size(50.dp)
                      .clip(RoundedCornerShape(6.dp))
                  )

                  Spacer(modifier = Modifier.width(10.dp))

                  Column(modifier = Modifier.weight(1f)) {
                    Text(item.product.title, color = AceTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("By ${item.product.authorName}", color = AceGold, fontSize = 11.sp)
                    Text("$${"%.2f".format(item.product.price)} • ${item.product.format.displayName}", color = AceTextSecondary, fontSize = 10.sp)
                  }

                  IconButton(onClick = { onRemoveItem(item.product.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = AceRose)
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(10.dp))

        // Price & Creator Royalties Note
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          color = AceEmeraldBg.copy(alpha = 0.35f)
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Creator Guarantee: $${"%.2f".format(creatorShare)} (85%) goes directly to authors",
                color = AceEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Total Amount", color = AceTextSecondary, fontSize = 11.sp)
            Text("$${"%.2f".format(subtotal)}", color = AceTextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
          }

          Button(
            onClick = onCheckout,
            enabled = cartItems.isNotEmpty(),
            modifier = Modifier.height(46.dp).testTag("checkout_confirm_btn"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceGold)
          ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Complete Purchase", color = AceObsidian, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
