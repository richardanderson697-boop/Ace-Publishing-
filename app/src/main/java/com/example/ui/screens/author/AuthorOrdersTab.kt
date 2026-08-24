package com.example.ui.screens.author

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.OrderStatus
import com.example.ui.components.OperationalOrderCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun AuthorOrdersTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  var selectedFilter by remember { mutableStateOf<OrderStatus?>(uiState.selectedOrderFilter) }

  val filteredOrders = remember(uiState.operationalOrders, selectedFilter) {
    if (selectedFilter == null) {
      uiState.operationalOrders
    } else {
      uiState.operationalOrders.filter { it.status == selectedFilter }
    }
  }

  val totalGross = remember(uiState.operationalOrders) {
    uiState.operationalOrders.sumOf { it.grossAmount }
  }
  val totalCreatorNet = remember(uiState.operationalOrders) {
    uiState.operationalOrders.sumOf { it.authorCut85 }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_orders_tab"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header with Operational Summary
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "OPERATIONAL ORDERS BOARD",
                color = AceGold,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp
              )
              Text(
                text = "Fulfillment & Delivery Pipeline",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(AceEmeraldBg)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("85% Creator Cut", color = AceEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Total Pipeline: $${"%.2f".format(totalGross)} Gross",
              color = AceTextSecondary,
              fontSize = 12.sp
            )
            Text(
              text = "Your Net: $${"%.2f".format(totalCreatorNet)}",
              color = AceEmerald,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // Filter Chips Row
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        FilterChip(
          selected = selectedFilter == null,
          onClick = { selectedFilter = null },
          label = { Text("All (${uiState.operationalOrders.size})", fontSize = 11.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AceGold,
            selectedLabelColor = AceObsidian,
            labelColor = AceTextSecondary
          )
        )

        OrderStatus.values().forEach { status ->
          val count = uiState.operationalOrders.count { it.status == status }
          FilterChip(
            selected = selectedFilter == status,
            onClick = { selectedFilter = if (selectedFilter == status) null else status },
            label = { Text("${status.displayName} ($count)", fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AceGold,
              selectedLabelColor = AceObsidian,
              labelColor = AceTextSecondary
            )
          )
        }
      }
    }

    if (filteredOrders.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("No orders in this status category.", color = AceTextSecondary, fontSize = 13.sp)
        }
      }
    } else {
      items(filteredOrders, key = { it.orderId }) { order ->
        OperationalOrderCard(
          order = order,
          onUpdateStatus = { newStatus -> viewModel.updateOrderStatus(order.orderId, newStatus) }
        )
      }
    }
  }
}
