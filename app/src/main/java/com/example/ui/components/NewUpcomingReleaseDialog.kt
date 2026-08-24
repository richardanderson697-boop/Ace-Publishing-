package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.data.models.ReleaseType
import com.example.data.models.TargetAudience
import com.example.ui.theme.*

@Composable
fun NewUpcomingReleaseDialog(
  onDismiss: () -> Unit,
  onSchedule: (title: String, subtitle: String, type: ReleaseType, dateText: String, audience: TargetAudience, sampleText: String) -> Unit
) {
  var title by remember { mutableStateOf("The Machine 2 — Chapter 7: Rogue Protocol") }
  var subtitle by remember { mutableStateOf("Advance preview chapter for dedicated followers") }
  var releaseType by remember { mutableStateOf(ReleaseType.CHAPTER) }
  var dateText by remember { mutableStateOf("September 19, 2026") }
  var targetAudience by remember { mutableStateOf(TargetAudience.FOLLOWERS_ONLY) }
  var sampleText by remember { mutableStateOf("The gateway signal spiked at 0400 hours. The neural node did not request human clearance...") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.88f)
        .testTag("new_upcoming_release_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
      ) {
        // Header
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
                .background(Brush.linearGradient(listOf(AceIndigo, AceIndigoDark))),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = AceTextPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Schedule Upcoming Material",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = "Build fan anticipation & previews",
                color = AceIndigoLight,
                fontSize = 11.sp
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = AceTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
          OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Release / Chapter Title") },
            modifier = Modifier.fillMaxWidth().testTag("upcoming_title_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceIndigoLight,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceIndigoLight,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = subtitle,
            onValueChange = { subtitle = it },
            label = { Text("Subtitle / Teaser Hook") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceIndigoLight,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceIndigoLight,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text("Release Type:", color = AceTextSecondary, fontSize = 11.sp)
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            ReleaseType.values().forEach { rType ->
              FilterChip(
                selected = releaseType == rType,
                onClick = { releaseType = rType },
                label = { Text(rType.displayName, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = AceIndigoLight,
                  selectedLabelColor = AceObsidian,
                  labelColor = AceTextSecondary
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            label = { Text("Target Release Date") },
            modifier = Modifier.fillMaxWidth().testTag("upcoming_date_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceIndigoLight,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceIndigoLight,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text("Audience Access:", color = AceTextSecondary, fontSize = 11.sp)
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            TargetAudience.values().forEach { aud ->
              FilterChip(
                selected = targetAudience == aud,
                onClick = { targetAudience = aud },
                label = { Text(aud.displayName, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = AceGold,
                  selectedLabelColor = AceObsidian,
                  labelColor = AceTextSecondary
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = sampleText,
            onValueChange = { sampleText = it },
            label = { Text("Sample Text / Chapter Excerpt") },
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceIndigoLight,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceIndigoLight,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f).height(46.dp),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Text("Cancel", color = AceTextSecondary)
          }

          Button(
            onClick = {
              if (title.isNotBlank()) {
                onSchedule(title, subtitle, releaseType, dateText, targetAudience, sampleText)
              }
            },
            modifier = Modifier.weight(1.5f).height(46.dp).testTag("confirm_schedule_release_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceIndigoLight)
          ) {
            Icon(Icons.Default.Schedule, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Schedule Release", color = AceObsidian, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
