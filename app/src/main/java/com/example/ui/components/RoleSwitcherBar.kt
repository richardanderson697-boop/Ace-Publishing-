package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.models.AuthorWorkspace
import com.example.data.models.UserRole
import com.example.ui.theme.*

@Composable
fun RoleSwitcherBar(
  currentRole: UserRole,
  onRoleSelected: (UserRole) -> Unit,
  currentAuthorId: String,
  allWorkspaces: Map<String, AuthorWorkspace>,
  onAuthorSelected: (String) -> Unit,
  onOpenCart: () -> Unit,
  cartCount: Int,
  modifier: Modifier = Modifier
) {
  var showAuthorDropdown by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("role_switcher_bar"),
    color = AceDarkSurface,
    tonalElevation = 6.dp,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
      // Top row: Brand & Role Switcher
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // ACE Brand Logo Badge
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(end = 8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(
                Brush.linearGradient(listOf(AceGold, AceGoldDark))
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "ACE",
              color = AceObsidian,
              fontWeight = FontWeight.Black,
              fontSize = 11.sp,
              letterSpacing = 0.5.sp
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "ACE PLATFORM",
              color = AceTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              letterSpacing = 0.5.sp
            )
            Text(
              text = when (currentRole) {
                UserRole.AUTHOR -> "Author Workspace (Isolated)"
                UserRole.CUSTOMER -> "Reader & Fan Storefront"
                UserRole.ADMIN -> "ACE Master Governance"
              },
              color = AceTextSecondary,
              fontSize = 10.sp
            )
          }
        }

        // Cart button if in customer mode
        if (currentRole == UserRole.CUSTOMER) {
          BadgedBox(
            badge = {
              if (cartCount > 0) {
                Badge(containerColor = AceGold, contentColor = AceObsidian) {
                  Text(text = "$cartCount", fontWeight = FontWeight.Bold)
                }
              }
            }
          ) {
            IconButton(
              onClick = onOpenCart,
              modifier = Modifier.testTag("cart_button")
            ) {
              Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = AceGold
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Role Pill Selector Row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(AceDarkCard)
          .padding(3.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        RoleTabButton(
          title = "🛍️ Reader",
          isSelected = currentRole == UserRole.CUSTOMER,
          onClick = { onRoleSelected(UserRole.CUSTOMER) },
          modifier = Modifier.weight(1f).testTag("role_customer_button")
        )
        RoleTabButton(
          title = "✍️ Author",
          isSelected = currentRole == UserRole.AUTHOR,
          onClick = { onRoleSelected(UserRole.AUTHOR) },
          modifier = Modifier.weight(1f).testTag("role_author_button")
        )
        RoleTabButton(
          title = "🛠️ Admin",
          isSelected = currentRole == UserRole.ADMIN,
          onClick = { onRoleSelected(UserRole.ADMIN) },
          modifier = Modifier.weight(1f).testTag("role_admin_button")
        )
      }

      // If Author Role, display isolated workspace selector banner
      if (currentRole == UserRole.AUTHOR) {
        Spacer(modifier = Modifier.height(8.dp))
        val currentWs = allWorkspaces[currentAuthorId]

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(AceIndigoDark.copy(alpha = 0.35f))
            .border(1.dp, AceIndigoLight.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .clickable { showAuthorDropdown = true }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("author_workspace_dropdown_trigger"),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = AceIndigoLight,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Text(
                text = "ISOLATED WORKSPACE: ${currentWs?.storeName ?: "Richard Anderson"}",
                color = AceIndigoLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "${currentWs?.handle ?: "@richard_anderson"} • ${currentWs?.payoutEmail ?: "payout"}",
                color = AceTextSecondary,
                fontSize = 10.sp
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Switch Author",
              color = AceGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
            Icon(
              imageVector = Icons.Default.ArrowDropDown,
              contentDescription = "Switch Author",
              tint = AceGold,
              modifier = Modifier.size(18.dp)
            )
          }

          DropdownMenu(
            expanded = showAuthorDropdown,
            onDismissRequest = { showAuthorDropdown = false },
            modifier = Modifier
              .background(AceDarkCard)
              .border(1.dp, AceDarkCardBorder, RoundedCornerShape(8.dp))
          ) {
            Text(
              text = "Author Multi-Tenancy Switcher",
              color = AceGold,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
            Divider(color = AceDarkCardBorder)
            allWorkspaces.forEach { (id, ws) ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(
                      text = ws.storeName,
                      color = if (id == currentAuthorId) AceGold else AceTextPrimary,
                      fontWeight = if (id == currentAuthorId) FontWeight.Bold else FontWeight.Normal,
                      fontSize = 13.sp
                    )
                    Text(
                      text = "${ws.handle} • ${ws.followerCount} followers",
                      color = AceTextSecondary,
                      fontSize = 11.sp
                    )
                  }
                },
                leadingIcon = {
                  Icon(
                    imageVector = if (id == currentAuthorId) Icons.Default.CheckCircle else Icons.Default.Person,
                    contentDescription = null,
                    tint = if (id == currentAuthorId) AceGold else AceTextMuted
                  )
                },
                onClick = {
                  onAuthorSelected(id)
                  showAuthorDropdown = false
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun RoleTabButton(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(9.dp))
      .background(if (isSelected) AceGold else androidx.compose.ui.graphics.Color.Transparent)
      .clickable(onClick = onClick)
      .padding(vertical = 7.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = title,
      color = if (isSelected) AceObsidian else AceTextSecondary,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      fontSize = 12.sp
    )
  }
}
