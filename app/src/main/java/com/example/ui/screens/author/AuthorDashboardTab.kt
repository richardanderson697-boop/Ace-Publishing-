package com.example.ui.screens.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.OperationalOrderCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel
import com.example.ui.viewmodel.AuthorTab

@Composable
fun AuthorDashboardTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val ws = uiState.authorWorkspace
  val analytics = viewModel.getAnalyticsSummary()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_dashboard_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Hero Banner with Studio Name
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = AceDarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(listOf(AceDarkCard, AceIndigoDark.copy(alpha = 0.5f)))
            )
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = ws?.storeName ?: "Richard Anderson Studio",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
              Text(
                text = "${ws?.handle ?: "@richard_anderson"} • Isolated Author Workspace",
                color = AceGold,
                fontSize = 12.sp
              )
            }

            Button(
              onClick = { viewModel.openImportFromWriteSound(true) },
              modifier = Modifier.testTag("dashboard_import_button"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceGold)
            ) {
              Icon(Icons.Default.CloudDownload, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Import from Write-Sound", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
        }
      }
    }

    // Quick Stats Grid: Month Sales, Creator Net (85%), Followers, Pending Orders
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "This Month Gross",
          value = "$${"%.2f".format(analytics.currentPeriod.grossSales)}",
          subValue = "↑ ${analytics.priorPeriodComparison.revenueGrowthPercent.toInt()}% vs last mo",
          subColor = AceEmerald,
          icon = Icons.Default.MonetizationOn,
          iconTint = AceGold,
          modifier = Modifier.weight(1f)
        )

        StatCard(
          title = "Creator Share (85%)",
          value = "$${"%.2f".format(analytics.currentPeriod.creatorShare85)}",
          subValue = "${analytics.currentPeriod.unitsSold} copies sold",
          subColor = AceEmerald,
          icon = Icons.Default.AccountBalanceWallet,
          iconTint = AceEmerald,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Followers / Fans",
          value = "${ws?.followerCount ?: 312}",
          subValue = "${ws?.activeFollowersThisWeek ?: 48} active this week",
          subColor = AceBlue,
          icon = Icons.Default.People,
          iconTint = AceBlue,
          modifier = Modifier.weight(1f)
        )

        StatCard(
          title = "Upcoming Anticipation",
          value = "${uiState.scheduledReleases.size} Releases",
          subValue = "Chapter 6 next",
          subColor = AceIndigoLight,
          icon = Icons.Default.Event,
          iconTint = AceIndigoLight,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Write-Sound Studio Intake Quick Bar
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = AceIndigoDark.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.4f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Sync, contentDescription = null, tint = AceIndigoLight, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Write-Sound Bridge Synced",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Text(
                text = "${uiState.writeSoundProjects.size} projects ready for publishing import",
                color = AceIndigoLight,
                fontSize = 11.sp
              )
            }
          }

          Button(
            onClick = { viewModel.openImportFromWriteSound(true) },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceIndigoLight)
          ) {
            Text("Review Drafts", color = AceObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Recent Operational Orders Section
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "RECENT OPERATIONAL ORDERS",
          color = AceTextSecondary,
          fontWeight = FontWeight.Bold,
          fontSize = 11.sp,
          letterSpacing = 1.sp
        )
        TextButton(onClick = { viewModel.setAuthorTab(AuthorTab.ORDERS) }) {
          Text("View All (${uiState.operationalOrders.size})", color = AceGold, fontSize = 11.sp)
        }
      }
    }

    items(uiState.operationalOrders.take(3)) { order ->
      OperationalOrderCard(
        order = order,
        onUpdateStatus = { newStatus -> viewModel.updateOrderStatus(order.orderId, newStatus) }
      )
    }
  }
}

@Composable
private fun StatCard(
  title: String,
  value: String,
  subValue: String,
  subColor: androidx.compose.ui.graphics.Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = AceDarkCard,
    shape = RoundedCornerShape(14.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(title, color = AceTextSecondary, fontSize = 11.sp)
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(value, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(subValue, color = subColor, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
  }
}
