package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AceRepository
import com.example.ui.theme.*

@Composable
fun AudioPreviewBar(
  sample: AceRepository.PlayingAudioSample?,
  onTogglePlay: () -> Unit,
  onSeek: (Int) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  AnimatedVisibility(
    visible = sample != null,
    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
    modifier = modifier
  ) {
    if (sample == null) return@AnimatedVisibility

    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 6.dp)
        .testTag("audio_preview_bar"),
      shape = RoundedCornerShape(18.dp),
      color = AceDarkCard,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.4f)),
      shadowElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              colors = listOf(AceDarkCard, AceDarkSurface)
            )
          )
          .padding(horizontal = 14.dp, vertical = 10.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Cover thumbnail
          Image(
            painter = painterResource(id = sample.coverRes),
            contentDescription = "Audio Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(8.dp))
          )

          Spacer(modifier = Modifier.width(12.dp))

          // Title & Author
          Column(
            modifier = Modifier.weight(1f)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Headphones,
                contentDescription = null,
                tint = AceGold,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "AUDIO PREVIEW",
                color = AceGold,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
            }
            Text(
              text = sample.title,
              color = AceTextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = sample.authorName,
              color = AceTextSecondary,
              fontSize = 12.sp,
              maxLines = 1
            )
          }

          // Controls
          IconButton(
            onClick = onTogglePlay,
            modifier = Modifier
              .size(40.dp)
              .background(AceGold, CircleShape)
              .testTag("audio_play_pause_button")
          ) {
            Icon(
              imageVector = if (sample.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (sample.isPlaying) "Pause" else "Play",
              tint = AceObsidian,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          IconButton(
            onClick = onClose,
            modifier = Modifier.size(32.dp).testTag("audio_close_button")
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close Audio",
              tint = AceTextMuted,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Progress bar & Time
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          val progress = if (sample.durationSeconds > 0) {
            (sample.currentPositionSeconds.toFloat() / sample.durationSeconds.toFloat()).coerceIn(0f, 1f)
          } else 0f

          Text(
            text = formatDuration(sample.currentPositionSeconds),
            color = AceTextSecondary,
            fontSize = 10.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
          )

          Slider(
            value = progress,
            onValueChange = { frac ->
              onSeek((frac * sample.durationSeconds).toInt())
            },
            modifier = Modifier
              .weight(1f)
              .height(24.dp)
              .padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(
              thumbColor = AceGold,
              activeTrackColor = AceGold,
              inactiveTrackColor = AceDarkCardBorder
            )
          )

          Text(
            text = formatDuration(sample.durationSeconds),
            color = AceTextSecondary,
            fontSize = 10.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
          )
        }
      }
    }
  }
}

private fun formatDuration(seconds: Int): String {
  val mins = seconds / 60
  val secs = seconds % 60
  return "%d:%02d".format(mins, secs)
}
