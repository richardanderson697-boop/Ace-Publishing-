package com.example.ui.screens.author

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel
import com.example.ui.viewmodel.AuthorTab

@Composable
fun AuthorWorkspaceScreen(
  uiState: AceUiState,
  viewModel: AceViewModel,
  modifier: Modifier = Modifier
) {
  val currentTab = uiState.authorNavigationTab
  val workspace = uiState.authorWorkspace

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(AceDarkSurface)
  ) {
    // Author Sub-Navigation Scrollable Pill Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(AceDarkCard)
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AuthorTab.values().forEach { tab ->
        val isSelected = currentTab == tab
        val icon = when (tab) {
          AuthorTab.DASHBOARD -> Icons.Default.Dashboard
          AuthorTab.BOOKS -> Icons.Default.MenuBook
          AuthorTab.UPCOMING -> Icons.Default.Event
          AuthorTab.ORDERS -> Icons.Default.ShoppingBag
          AuthorTab.ANALYTICS -> Icons.Default.TrendingUp
          AuthorTab.FANS -> Icons.Default.Group
          AuthorTab.SETTINGS -> Icons.Default.Settings
        }

        Surface(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { viewModel.setAuthorTab(tab) }
            .testTag("author_tab_${tab.name.lowercase()}"),
          color = if (isSelected) AceGold else AceDarkSurface,
          shape = RoundedCornerShape(10.dp),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) AceGold else AceDarkCardBorder
          )
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = if (isSelected) AceObsidian else AceTextSecondary,
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = tab.label,
              color = if (isSelected) AceObsidian else AceTextPrimary,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 12.sp
            )
          }
        }
      }
    }

    // Active Tab View Content
    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
      when (currentTab) {
        AuthorTab.DASHBOARD -> AuthorDashboardTab(uiState, viewModel)
        AuthorTab.BOOKS -> AuthorBooksTab(uiState, viewModel)
        AuthorTab.UPCOMING -> AuthorUpcomingTab(uiState, viewModel)
        AuthorTab.ORDERS -> AuthorOrdersTab(uiState, viewModel)
        AuthorTab.ANALYTICS -> AuthorAnalyticsTab(uiState, viewModel)
        AuthorTab.FANS -> AuthorFansTab(uiState, viewModel)
        AuthorTab.SETTINGS -> AuthorSettingsTab(uiState, viewModel)
      }
    }
  }
}
