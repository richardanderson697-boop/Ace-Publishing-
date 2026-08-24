package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun AdminWorkspaceScreen(
  uiState: AceUiState,
  viewModel: AceViewModel,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Platform KPIs, 1: Author Workspaces, 2: Audio CDN Moderation

  val allWorkspaces = uiState.allAuthorWorkspaces.values.toList()
  val totalPlatformGMV = 48600.00
  val platformFee15 = totalPlatformGMV * 0.15
  val authorPayouts85 = totalPlatformGMV * 0.85

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(AceDarkSurface)
      .padding(14.dp)
      .testTag("admin_workspace_screen"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Banner
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.4f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(listOf(AceDarkCard, AceIndigoDark.copy(alpha = 0.7f)))
            )
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(AceGold),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "ACE PLATFORM GOVERNANCE",
                  color = AceGold,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  letterSpacing = 1.sp
                )
                Text(
                  text = "Master Operations & Tenant Oversight",
                  color = AceTextPrimary,
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp
                )
              }
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AceEmeraldBg)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("System Healthy (99.99%)", color = AceEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // Tab Navigation
    item {
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = AceDarkCard,
        contentColor = AceGold,
        modifier = Modifier.clip(RoundedCornerShape(12.dp))
      ) {
        Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
          Text("Platform KPIs", fontSize = 11.sp, modifier = Modifier.padding(vertical = 10.dp), fontWeight = FontWeight.SemiBold)
        }
        Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
          Text("Author Multi-Tenancy", fontSize = 11.sp, modifier = Modifier.padding(vertical = 10.dp), fontWeight = FontWeight.SemiBold)
        }
        Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
          Text("Audio CDN & Moderation", fontSize = 11.sp, modifier = Modifier.padding(vertical = 10.dp), fontWeight = FontWeight.SemiBold)
        }
      }
    }

    when (selectedTab) {
      0 -> {
        // Platform KPIs
        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            AdminKpiCard(
              title = "Gross Platform GMV",
              value = "$${"%,.2f".format(totalPlatformGMV)}",
              subtitle = "Across all creator stores",
              tint = AceGold,
              modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
              title = "ACE 15% Platform Take",
              value = "$${"%,.2f".format(platformFee15)}",
              subtitle = "Platform operational fee",
              tint = AceIndigoLight,
              modifier = Modifier.weight(1f)
            )
          }
        }

        item {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            AdminKpiCard(
              title = "Creator 85% Disbursed",
              value = "$${"%,.2f".format(authorPayouts85)}",
              subtitle = "Direct to author bank accounts",
              tint = AceEmerald,
              modifier = Modifier.weight(1f)
            )
            AdminKpiCard(
              title = "Active Author Stores",
              value = "${allWorkspaces.size}",
              subtitle = "Isolated workspaces",
              tint = AceBlue,
              modifier = Modifier.weight(1f)
            )
          }
        }

        item {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = AceDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text("GLOBAL DISPATCH & ORDER TELEMETRY", color = AceTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(8.dp))
              Text("• Total Master Audio Streams Served: 14,290 hours", color = AceTextPrimary, fontSize = 12.sp)
              Text("• Average Attribution Conversion Rate: 43.8% from author posts", color = AceEmerald, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              Text("• Automated Payout Settlement Frequency: Weekly (T+2)", color = AceTextSecondary, fontSize = 12.sp)
              Text("• CDN Edge Delivery Latency: < 24ms globally", color = AceTextSecondary, fontSize = 12.sp)
            }
          }
        }
      }

      1 -> {
        // Author Tenancy Inspection
        items(allWorkspaces, key = { it.authorId }) { ws ->
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = AceDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(ws.storeName, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                  Text("${ws.handle} • ${ws.authorId}", color = AceGold, fontSize = 11.sp)
                }
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AceEmeraldBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                  Text("Tenant Verified", color = AceEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text("Payout Destination: ${ws.payoutEmail}", color = AceTextSecondary, fontSize = 11.sp)
              Text("Community: ${ws.followerCount} Followers (${ws.activeFollowersThisWeek} active this week)", color = AceTextSecondary, fontSize = 11.sp)

              Spacer(modifier = Modifier.height(10.dp))
              Button(
                onClick = {
                  viewModel.selectAuthorWorkspace(ws.authorId)
                  viewModel.switchRole(com.example.data.models.UserRole.AUTHOR)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AceIndigo),
                modifier = Modifier.fillMaxWidth().height(34.dp)
              ) {
                Text("Inspect Isolated Workspace", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      2 -> {
        // Audio CDN & Moderation
        item {
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = AceDarkCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text("WRITE-SOUND MASTER AUDIO INGESTION QUEUE", color = AceGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Spacer(modifier = Modifier.height(10.dp))

              AudioQueueItem(
                title = "The Machine 2: Autonomous Dawn — Full Audio Master",
                author = "Richard Anderson",
                format = "Lossless FLAC 96kHz / 24-bit Spatial Binaural",
                status = "Ingested & Encrypted (Ready)"
              )

              Spacer(modifier = Modifier.height(8.dp))
              AudioQueueItem(
                title = "Stellar Drift: Void Walkers — Master Track",
                author = "Elena Vance",
                format = "320kbps AAC + Spatial Metadata",
                status = "Ingested & Encrypted (Ready)"
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AdminKpiCard(
  title: String,
  value: String,
  subtitle: String,
  tint: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    color = AceDarkCard,
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Text(title, color = AceTextSecondary, fontSize = 10.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(value, color = tint, fontWeight = FontWeight.Black, fontSize = 16.sp)
      Spacer(modifier = Modifier.height(2.dp))
      Text(subtitle, color = AceTextMuted, fontSize = 9.sp)
    }
  }
}

@Composable
private fun AudioQueueItem(
  title: String,
  author: String,
  format: String,
  status: String
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(8.dp),
    color = AceDarkSurface
  ) {
    Row(
      modifier = Modifier.padding(10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(title, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        Text("Author: $author • $format", color = AceTextSecondary, fontSize = 10.sp)
      }
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(4.dp))
          .background(AceEmeraldBg)
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text(status, color = AceEmerald, fontSize = 8.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}
