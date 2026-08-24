package com.example.ui.screens.author

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun AuthorAnalyticsTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val analytics = viewModel.getAnalyticsSummary()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_analytics_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Current Period Overview
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "THIS MONTH'S PERFORMANCE",
            color = AceGold,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${analytics.currentPeriod.unitsSold} Total Copies Sold",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            MetricPill(
              title = "Gross Marketplace Sales",
              amount = "$${"%.2f".format(analytics.currentPeriod.grossSales)}",
              growthText = "↑ ${analytics.priorPeriodComparison.revenueGrowthPercent.toInt()}% vs prior month",
              color = AceGold,
              modifier = Modifier.weight(1f)
            )

            MetricPill(
              title = "Creator Share (85%)",
              amount = "$${"%.2f".format(analytics.currentPeriod.creatorShare85)}",
              growthText = "Direct Payout Ready",
              color = AceEmerald,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Sub metrics row: Audiobook vs Ebook vs Followers
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SubMetricTile(
              title = "Audiobooks",
              count = "${analytics.currentPeriod.audiobookSales} units",
              icon = Icons.Default.Headphones,
              modifier = Modifier.weight(1f)
            )
            SubMetricTile(
              title = "E-Books",
              count = "${analytics.currentPeriod.ebookSales} units",
              icon = Icons.Default.Book,
              modifier = Modifier.weight(1f)
            )
            SubMetricTile(
              title = "New Followers",
              count = "+${analytics.currentPeriod.newFollowers}",
              icon = Icons.Default.PersonAdd,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    // 2. Compared with Last Month Growth Badges
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "PERIOD COMPARISON (MoM GROWTH)",
            color = AceIndigoLight,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            GrowthCard(
              label = "Sales Velocity",
              percent = "+${analytics.priorPeriodComparison.salesGrowthPercent.toInt()}%",
              icon = Icons.Default.TrendingUp,
              modifier = Modifier.weight(1f)
            )
            GrowthCard(
              label = "Fan Acquisition",
              percent = "+${analytics.priorPeriodComparison.followerGrowthPercent.toInt()}%",
              icon = Icons.Default.Groups,
              modifier = Modifier.weight(1f)
            )
            GrowthCard(
              label = "Net Revenue",
              percent = "+${analytics.priorPeriodComparison.revenueGrowthPercent.toInt()}%",
              icon = Icons.Default.ShowChart,
              modifier = Modifier.weight(1f)
            )
          }
        }
      }
    }

    // 3. Forecasting Section (Explicitly labeled as Estimate!)
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceIndigoDark.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(RoundedCornerShape(8.dp))
                .background(Brush.linearGradient(listOf(AceIndigo, AceIndigoDark))),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.AutoGraph, contentDescription = null, tint = AceTextPrimary, modifier = Modifier.size(18.dp))
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "REVENUE FORECASTING",
                  color = AceIndigoLight,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  letterSpacing = 1.sp
                )
                Text(
                  text = "Projected September Revenue",
                  color = AceTextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AceDarkSurface)
                .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
              Text("ESTIMATE", color = AceGold, fontSize = 9.sp, fontWeight = FontWeight.Black)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "$${"%.0f".format(analytics.forecastingEstimate.projectedRangeMin)} – $${"%.0f".format(analytics.forecastingEstimate.projectedRangeMax)}",
            color = AceGold,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = AceDarkSurface
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Info, contentDescription = null, tint = AceIndigoLight, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = analytics.forecastingEstimate.confidenceNote,
                color = AceTextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
              )
            }
          }
        }
      }
    }

    // 4. Royalty Transparency 85/15 Explainer
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "TRANSPARENT ROYALTY MODEL",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(24.dp)
              .clip(RoundedCornerShape(6.dp))
          ) {
            Box(
              modifier = Modifier
                .weight(0.85f)
                .fillMaxHeight()
                .background(AceEmerald),
              contentAlignment = Alignment.Center
            ) {
              Text("85% Creator Share", color = AceObsidian, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Box(
              modifier = Modifier
                .weight(0.15f)
                .fillMaxHeight()
                .background(AceIndigo),
              contentAlignment = Alignment.Center
            ) {
              Text("15%", color = AceTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "ACE charges a transparent 15% flat platform fee for master audio CDN delivery, payment gateway processing, and mobile app discovery. Creators keep 85% with automated weekly payouts.",
            color = AceTextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }
    }
  }
}

@Composable
private fun MetricPill(
  title: String,
  amount: String,
  growthText: String,
  color: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = AceDarkSurface,
    shape = RoundedCornerShape(12.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f))
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(title, color = AceTextSecondary, fontSize = 10.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(amount, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(growthText, color = AceEmerald, fontSize = 9.sp, fontWeight = FontWeight.Medium)
    }
  }
}

@Composable
private fun SubMetricTile(
  title: String,
  count: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = AceDarkSurface,
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Icon(icon, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(count, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
      Text(title, color = AceTextSecondary, fontSize = 9.sp)
    }
  }
}

@Composable
private fun GrowthCard(
  label: String,
  percent: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = AceDarkSurface,
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(percent, color = AceEmerald, fontWeight = FontWeight.Bold, fontSize = 14.sp)
      Text(label, color = AceTextSecondary, fontSize = 9.sp, maxLines = 1)
    }
  }
}
