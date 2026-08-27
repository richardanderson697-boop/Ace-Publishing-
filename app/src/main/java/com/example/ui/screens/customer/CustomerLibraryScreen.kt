package com.example.ui.screens.customer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomerLibraryItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun CustomerLibraryScreen(
  uiState: AceUiState,
  viewModel: AceViewModel,
  modifier: Modifier = Modifier
) {
  val libraryItems = uiState.customerLibrary

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(AceDarkSurface)
      .padding(14.dp)
      .testTag("customer_library_screen"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = "YOUR ACE LIBRARY",
              color = AceGold,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.sp
            )
            Text(
              text = "${libraryItems.size} Purchased Master Titles",
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
            Text("DRM-Free Masters", color = AceEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    if (libraryItems.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.LibraryBooks, contentDescription = null, tint = AceTextMuted, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text("Your library is empty.", color = AceTextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Explore the storefront to get your first master audiobook.", color = AceTextMuted, fontSize = 12.sp)
          }
        }
      }
    } else {
      items(libraryItems, key = { it.id }) { item ->
        LibraryBookCard(
          item = item,
          onPlayAudio = {
            viewModel.playAudioSample(
              title = item.product.title,
              authorName = item.product.authorName,
              coverRes = item.product.coverDrawableRes,
              durationSeconds = item.totalDurationSec,
              audioFilePath = item.product.localAudioPath,
              localCoverUri = item.product.localCoverUri
            )
          }
        )
      }
    }
  }
}

@Composable
private fun LibraryBookCard(
  item: CustomerLibraryItem,
  onPlayAudio: () -> Unit,
  modifier: Modifier = Modifier
) {
  val progressFraction = if (item.totalDurationSec > 0) {
    (item.lastPlaybackPositionSec.toFloat() / item.totalDurationSec.toFloat()).coerceIn(0f, 1f)
  } else 0f

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = AceDarkCard,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        if (item.product.localCoverUri != null) {
          coil.compose.AsyncImage(
            model = item.product.localCoverUri,
            contentDescription = item.product.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .width(80.dp)
              .height(112.dp)
              .clip(RoundedCornerShape(10.dp))
          )
        } else {
          Image(
            painter = painterResource(id = item.product.coverDrawableRes),
            contentDescription = item.product.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .width(80.dp)
              .height(112.dp)
              .clip(RoundedCornerShape(10.dp))
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AceIndigoDark.copy(alpha = 0.4f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(item.product.format.displayName, color = AceIndigoLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(item.product.title, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text("By ${item.product.authorName}", color = AceGold, fontSize = 12.sp)

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Purchased: ${item.purchaseDate} • ${item.product.chapterCount} Chapters",
            color = AceTextSecondary,
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          LinearProgressIndicator(
            progress = { progressFraction },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = AceGold,
            trackColor = AceDarkSurface
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
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CloudDone, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Lossless 96kHz Spatial Audio", color = AceEmerald, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }

        Button(
          onClick = onPlayAudio,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.height(36.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AceGold)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Play Master", color = AceObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
