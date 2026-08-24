package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AttributionSource
import com.example.data.models.OperationalOrder
import com.example.data.models.OrderStatus
import com.example.ui.theme.*

@Composable
fun OperationalOrderCard(
  order: OperationalOrder,
  onUpdateStatus: (OrderStatus) -> Unit,
  modifier: Modifier = Modifier
) {
  val (statusColor, statusBg) = when (order.status) {
    OrderStatus.PAYMENT_RECEIVED -> AceBlue to AceBlue.copy(alpha = 0.15f)
    OrderStatus.PROCESSING -> AceAmber to AceAmber.copy(alpha = 0.15f)
    OrderStatus.DIGITAL_DELIVERY_READY -> AceIndigoLight to AceIndigo.copy(alpha = 0.2f)
    OrderStatus.COMPLETED -> AceEmerald to AceEmeraldBg
    OrderStatus.PAYMENT_FAILED -> AceRose to AceRoseBg
    OrderStatus.REFUND_REQUESTED -> AceRose to AceRoseBg
  }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("order_card_${order.orderId}"),
    color = AceDarkCard,
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Header: Order ID & Timestamp & Status Badge
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "#${order.orderId}",
              color = AceGold,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "• ${order.formattedTime}",
              color = AceTextSecondary,
              fontSize = 11.sp
            )
          }
          Text(
            text = "Customer: ${order.customerName}",
            color = AceTextSecondary,
            fontSize = 11.sp
          )
        }

        // Status Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(statusBg)
            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = order.status.displayName,
            color = statusColor,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Divider(color = AceDarkCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(10.dp))

      // Product Title & Format
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = order.productTitle,
            color = AceTextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
          )
          Text(
            text = order.format.displayName,
            color = AceIndigoLight,
            fontSize = 11.sp
          )
        }

        // Financials: Gross & 85% Split
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "$${"%.2f".format(order.grossAmount)} Gross",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
          Text(
            text = "Author 85%: $${"%.2f".format(order.authorCut85)}",
            color = AceEmerald,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
          )
          Text(
            text = "ACE Fee 15%: $${"%.2f".format(order.platformFee15)}",
            color = AceTextMuted,
            fontSize = 10.sp
          )
        }
      }

      // Fan Attribution Note
      if (order.attributionSource != null || order.attributionDetail != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(AceDarkSurface)
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = when (order.attributionSource) {
              AttributionSource.FAN_POST -> Icons.Default.Campaign
              AttributionSource.AUDIO_PREVIEW -> Icons.Default.Headphones
              AttributionSource.STOREFRONT_SEARCH -> Icons.Default.Search
              else -> Icons.Default.Link
            },
            contentDescription = null,
            tint = AceGold,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Attribution: ${order.attributionDetail ?: order.attributionSource?.displayName ?: ""}",
            color = AceGoldLight,
            fontSize = 10.sp,
            maxLines = 1
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // State Transition Operations Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        when (order.status) {
          OrderStatus.PAYMENT_RECEIVED -> {
            Button(
              onClick = { onUpdateStatus(OrderStatus.PROCESSING) },
              modifier = Modifier.weight(1f).height(34.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceIndigo)
            ) {
              Text("Process Master Audio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
          OrderStatus.PROCESSING -> {
            Button(
              onClick = { onUpdateStatus(OrderStatus.DIGITAL_DELIVERY_READY) },
              modifier = Modifier.weight(1f).height(34.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceBlue)
            ) {
              Text("Set Digital Delivery Ready", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
          OrderStatus.DIGITAL_DELIVERY_READY -> {
            Button(
              onClick = { onUpdateStatus(OrderStatus.COMPLETED) },
              modifier = Modifier.weight(1f).height(34.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceEmerald)
            ) {
              Text("Deliver Audio & EPUB", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
          OrderStatus.COMPLETED -> {
            OutlinedButton(
              onClick = { onUpdateStatus(OrderStatus.REFUND_REQUESTED) },
              modifier = Modifier.weight(1f).height(32.dp),
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
            ) {
              Text("Request Refund/Support", fontSize = 10.sp, color = AceTextSecondary)
            }
          }
          OrderStatus.PAYMENT_FAILED -> {
            Button(
              onClick = { onUpdateStatus(OrderStatus.PAYMENT_RECEIVED) },
              modifier = Modifier.weight(1f).height(34.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceAmber)
            ) {
              Text("Retry Payment Gateway", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
          OrderStatus.REFUND_REQUESTED -> {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              Button(
                onClick = { onUpdateStatus(OrderStatus.COMPLETED) },
                modifier = Modifier.weight(1f).height(34.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AceEmerald)
              ) {
                Text("Decline & Keep Active", fontSize = 10.sp)
              }
              Button(
                onClick = { onUpdateStatus(OrderStatus.PAYMENT_FAILED) },
                modifier = Modifier.weight(1f).height(34.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AceRose)
              ) {
                Text("Approve Refund ($${"%.2f".format(order.grossAmount)})", fontSize = 10.sp)
              }
            }
          }
        }
      }
    }
  }
}
