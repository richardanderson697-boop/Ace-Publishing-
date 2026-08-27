package com.example.ui.screens.author

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.data.models.BookProduct
import com.example.data.models.PublicationStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun AuthorBooksTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val allBooks = uiState.authorBooks
  var filterSelected by remember { mutableStateOf(0) } // 0: All, 1: Live, 2: Private Drafts

  val filteredBooks = when (filterSelected) {
    1 -> allBooks.filter { it.publicationStatus == PublicationStatus.PUBLISHED_LIVE }
    2 -> allBooks.filter { it.publicationStatus == PublicationStatus.PRIVATE_DRAFT }
    else -> allBooks
  }

  val liveCount = allBooks.count { it.publicationStatus == PublicationStatus.PUBLISHED_LIVE }
  val draftCount = allBooks.count { it.publicationStatus == PublicationStatus.PRIVATE_DRAFT }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_books_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "CATALOG & MASTER DRAFTS",
            color = AceGold,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp
          )
          Text(
            text = "$liveCount Live Releases • $draftCount Private Drafts",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
        }

        Button(
          onClick = { viewModel.openImportFromWriteSound(true) },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = AceGold),
          modifier = Modifier.testTag("import_new_book_btn")
        ) {
          Icon(Icons.Default.CloudDownload, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Import / Publish", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
      }
    }

    // Filter Tabs
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = filterSelected == 0,
          onClick = { filterSelected = 0 },
          label = { Text("All Material (${allBooks.size})", fontSize = 11.sp, fontWeight = if (filterSelected == 0) FontWeight.Bold else FontWeight.Normal) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AceIndigo,
            selectedLabelColor = AceTextPrimary,
            containerColor = AceDarkCard,
            labelColor = AceTextSecondary
          )
        )

        FilterChip(
          selected = filterSelected == 1,
          onClick = { filterSelected = 1 },
          label = { Text("Live Marketplace ($liveCount)", fontSize = 11.sp, fontWeight = if (filterSelected == 1) FontWeight.Bold else FontWeight.Normal) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AceEmerald,
            selectedLabelColor = AceObsidian,
            containerColor = AceDarkCard,
            labelColor = AceTextSecondary
          )
        )

        FilterChip(
          selected = filterSelected == 2,
          onClick = { filterSelected = 2 },
          label = { Text("Private Drafts ($draftCount)", fontSize = 11.sp, fontWeight = if (filterSelected == 2) FontWeight.Bold else FontWeight.Normal) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = AceGold,
            selectedLabelColor = AceObsidian,
            containerColor = AceDarkCard,
            labelColor = AceTextSecondary
          )
        )
      }
    }

    if (filteredBooks.isEmpty()) {
      item {
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = AceDarkCard,
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("No releases matching this filter.", color = AceTextMuted, fontSize = 13.sp)
          }
        }
      }
    }

    items(filteredBooks, key = { it.id }) { book ->
      AuthorBookCard(
        book = book,
        onPlaySample = {
          viewModel.playAudioSample(
            title = book.title,
            authorName = book.authorName,
            coverRes = book.coverDrawableRes,
            durationSeconds = book.audioDurationMinutes * 60,
            audioFilePath = book.localAudioPath,
            localCoverUri = book.localCoverUri
          )
        },
        onPublishLive = {
          viewModel.publishDraftToLive(book.id)
        },
        onDeleteDraft = {
          viewModel.deleteProductDraft(book.id)
        }
      )
    }
  }
}

@Composable
private fun AuthorBookCard(
  book: BookProduct,
  onPlaySample: () -> Unit,
  onPublishLive: () -> Unit,
  onDeleteDraft: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isDraft = book.publicationStatus == PublicationStatus.PRIVATE_DRAFT

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = AceDarkCard,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(
      1.dp,
      if (isDraft) AceGold.copy(alpha = 0.6f) else AceDarkCardBorder
    )
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Draft Banner
      if (isDraft) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AceGold.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = AceGold, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "PRIVATE TEST DRAFT • UNLISTED",
              color = AceGold,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "Testing Only",
            color = AceGold,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        if (book.localCoverUri != null) {
          coil.compose.AsyncImage(
            model = book.localCoverUri,
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .width(80.dp)
              .height(110.dp)
              .clip(RoundedCornerShape(10.dp))
          )
        } else {
          Image(
            painter = painterResource(id = book.coverDrawableRes),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .width(80.dp)
              .height(110.dp)
              .clip(RoundedCornerShape(10.dp))
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isDraft) AceGold.copy(alpha = 0.2f) else AceIndigoDark.copy(alpha = 0.4f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                book.format.displayName,
                color = if (isDraft) AceGold else AceIndigoLight,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }

            // Ingestion Source & Royalty Badge
            val isEcosystem = book.ingestionSource == com.example.data.models.IngestionSource.WRITE_SOUND_ECOSYSTEM
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isEcosystem) AceIndigoDark.copy(alpha = 0.6f) else AceGold.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = if (isEcosystem) "Write-Sound (85%)" else "ZIP Intake (75%)",
                color = if (isEcosystem) AceTextPrimary else AceGold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(book.title, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
          Text(book.subtitle, color = AceTextSecondary, fontSize = 11.sp, maxLines = 2)
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "${book.chapterCount} Chapters • ${book.audioDurationMinutes / 60}h ${book.audioDurationMinutes % 60}m Master Audio",
            color = AceGold,
            fontSize = 11.sp
          )
          if (book.zipFileName != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Archive: ${book.zipFileName} (${if (book.extractedSegmentCount > 0) "${book.extractedSegmentCount} voice segments" else "Master Stitched"})",
              color = AceTextMuted,
              fontSize = 9.sp,
              fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          if (!isDraft) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Star, contentDescription = null, tint = AceGold, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("${book.rating} (${book.reviewCount} reviews)", color = AceTextSecondary, fontSize = 11.sp)
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      Divider(color = AceDarkCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(10.dp))

      // Financials & Actions
      val royaltyPct = book.royaltyRatePercent
      val netPayout = book.price * (royaltyPct / 100.0)

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Target Price: $${"%.2f".format(book.price)}",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
          Text(
            text = "Creator Net (${royaltyPct.toInt()}%): $${"%.2f".format(netPayout)}",
            color = AceEmerald,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onPlaySample,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f)),
            modifier = Modifier.height(34.dp)
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (isDraft) "Test Master" else "Sample Audio", color = AceGold, fontSize = 11.sp)
          }

          if (isDraft) {
            Button(
              onClick = onPublishLive,
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceEmerald),
              modifier = Modifier.height(34.dp)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Publish Live", color = AceObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            IconButton(
              onClick = onDeleteDraft,
              modifier = Modifier.size(34.dp)
            ) {
              Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Draft", tint = AceTextMuted, modifier = Modifier.size(18.dp))
            }
          }
        }
      }
    }
  }
}
