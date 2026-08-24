package com.example.ui.screens.author

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.ReleaseStatus
import com.example.data.models.ScheduledRelease
import com.example.data.models.TargetAudience
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun AuthorUpcomingTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val releases = uiState.scheduledReleases

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_upcoming_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceIndigoDark.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "UPCOMING MATERIAL & PREVIEWS",
                color = AceIndigoLight,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp
              )
              Text(
                text = "Build Anticipation Before Full Launch",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }

            Button(
              onClick = { viewModel.openNewUpcomingRelease(true) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceIndigoLight),
              modifier = Modifier.testTag("add_upcoming_release_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Schedule", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Share serialized chapter drops, early drafts, and audio teaser clips with followers to drive pre-orders and direct post conversions.",
            color = AceTextSecondary,
            fontSize = 12.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    items(releases) { rel ->
      ScheduledReleaseCard(
        release = rel,
        onPlayAudio = {
          viewModel.playAudioSample(
            title = rel.title,
            authorName = uiState.authorWorkspace?.storeName ?: "Author",
            coverRes = R.drawable.cover_machine2_1787574231599,
            durationSeconds = rel.audioSampleDurationSec
          )
        },
        onToggleStatus = {
          val next = if (rel.status == ReleaseStatus.SCHEDULED) ReleaseStatus.PUBLISHED else ReleaseStatus.SCHEDULED
          viewModel.updateReleaseStatus(rel.id, next)
        }
      )
    }
  }
}

@Composable
private fun ScheduledReleaseCard(
  release: ScheduledRelease,
  onPlayAudio: () -> Unit,
  onToggleStatus: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (statusColor, statusBg) = when (release.status) {
    ReleaseStatus.DRAFT -> AceTextMuted to AceDarkSurface
    ReleaseStatus.SCHEDULED -> AceAmber to AceAmber.copy(alpha = 0.15f)
    ReleaseStatus.PUBLISHED -> AceEmerald to AceEmeraldBg
  }

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = AceDarkCard,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AceIndigoDark.copy(alpha = 0.4f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(release.type.displayName, color = AceIndigoLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(if (release.targetAudience == TargetAudience.FOLLOWERS_ONLY) AceGold.copy(alpha = 0.2f) else AceDarkSurface)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(release.targetAudience.displayName, color = if (release.targetAudience == TargetAudience.FOLLOWERS_ONLY) AceGold else AceTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(statusBg)
            .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(release.status.displayName, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(release.title, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
      if (release.subtitle.isNotBlank()) {
        Text(release.subtitle, color = AceTextSecondary, fontSize = 12.sp)
      }

      Spacer(modifier = Modifier.height(8.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = AceGold, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Release: ${release.formattedDate}", color = AceGold, fontSize = 12.sp, fontWeight = FontWeight.Medium)
      }

      if (release.previewSampleText.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = AceDarkSurface
        ) {
          Text(
            text = "“${release.previewSampleText}”",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            modifier = Modifier.padding(10.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      Divider(color = AceDarkCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${release.previewPlaysCount} Preview Plays logged",
          color = AceIndigoLight,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onPlayAudio,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(34.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Preview Audio (${release.audioSampleDurationSec}s)", color = AceGold, fontSize = 10.sp)
          }

          Button(
            onClick = onToggleStatus,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(34.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceIndigo)
          ) {
            Text(if (release.status == ReleaseStatus.SCHEDULED) "Publish Now" else "Set Scheduled", fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
