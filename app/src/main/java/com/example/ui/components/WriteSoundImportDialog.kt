package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.ProductFormat
import com.example.data.models.PublicationStatus
import com.example.data.models.WriteSoundStudioProject
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteSoundImportDialog(
  projects: List<WriteSoundStudioProject>,
  onDismiss: () -> Unit,
  onPublish: (projectId: String, title: String, price: Double, description: String, releaseDate: String, format: ProductFormat, publicationStatus: PublicationStatus) -> Unit
) {
  var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }
  var customTitle by remember { mutableStateOf(selectedProject?.title ?: "The Machine 2: Autonomous Dawn") }
  var customPrice by remember { mutableStateOf(selectedProject?.defaultPrice?.toString() ?: "14.99") }
  var description by remember { mutableStateOf(selectedProject?.synopsis ?: "") }
  var releaseDate by remember { mutableStateOf("September 15, 2026") }
  var format by remember { mutableStateOf(ProductFormat.BUNDLE) }
  var publicationStatus by remember { mutableStateOf(PublicationStatus.PRIVATE_DRAFT) }
  var showStorePreview by remember { mutableStateOf(false) }

  // Update fields when project selection changes
  LaunchedEffect(selectedProject) {
    selectedProject?.let {
      customTitle = it.title
      customPrice = it.defaultPrice.toString()
      description = it.synopsis
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.92f)
        .testTag("write_sound_import_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
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
                imageVector = Icons.Default.CloudDownload,
                contentDescription = null,
                tint = AceTextPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Write-Sound Studio Bridge",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = "Import & Publish Mastered Projects",
                color = AceGold,
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
          Text(
            text = "1. SELECT WRITE-SOUND PROJECT",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))

          projects.forEach { proj ->
            val isSelected = selectedProject?.projectId == proj.projectId
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { selectedProject = proj },
              color = if (isSelected) AceIndigoDark.copy(alpha = 0.4f) else AceDarkCard,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) AceGold else AceDarkCardBorder
              )
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Image(
                  painter = painterResource(id = proj.coverDrawableRes),
                  contentDescription = proj.title,
                  modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = proj.title,
                    color = AceTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                  Text(
                    text = "${proj.chapterCount} Chapters • ${proj.masteredAudioDurationMin} min Binaural Master",
                    color = AceGold,
                    fontSize = 11.sp
                  )
                  Text(
                    text = "Studio Status: ${proj.synopsis}",
                    color = AceTextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1
                  )
                }
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = AceGold,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "2. PUBLICATION CONFIGURATION",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customTitle,
            onValueChange = { customTitle = it },
            label = { Text("Book & Audiobook Title") },
            modifier = Modifier.fillMaxWidth().testTag("import_title_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedTextField(
              value = customPrice,
              onValueChange = { customPrice = it },
              label = { Text("List Price ($)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              modifier = Modifier.weight(1f).testTag("import_price_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AceGold,
                unfocusedBorderColor = AceDarkCardBorder,
                focusedLabelColor = AceGold,
                unfocusedTextColor = AceTextPrimary,
                focusedTextColor = AceTextPrimary
              )
            )

            OutlinedTextField(
              value = releaseDate,
              onValueChange = { releaseDate = it },
              label = { Text("Release Date") },
              modifier = Modifier.weight(1.2f).testTag("import_release_date_input"),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AceGold,
                unfocusedBorderColor = AceDarkCardBorder,
                focusedLabelColor = AceGold,
                unfocusedTextColor = AceTextPrimary,
                focusedTextColor = AceTextPrimary
              )
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Format selector
          Text("Format:", color = AceTextSecondary, fontSize = 11.sp)
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            ProductFormat.values().forEach { fmt ->
              FilterChip(
                selected = format == fmt,
                onClick = { format = fmt },
                label = { Text(fmt.displayName, fontSize = 10.sp) },
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
            value = description,
            onValueChange = { description = it },
            label = { Text("Storefront Description & Synopsis") },
            maxLines = 4,
            modifier = Modifier.fillMaxWidth().testTag("import_desc_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // ACE Automated Ingestion Pipeline Notice
          Text(
            text = "3. ACE AUTOMATED INGESTION PIPELINE",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceObsidian,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ZIP Unpack • Segment Stitching • Cover Embed", color = AceGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "ACE automatically unpacks the ElevenLabs voice segments, validates sequencing (001-048), concatenates with natural pauses, embeds your jacket cover and chapter cues, and outputs a playable master draft.",
                color = AceTextSecondary,
                fontSize = 10.sp,
                lineHeight = 14.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Publication Mode / Private Testing selector
          Text(
            text = "4. PUBLICATION DESTINATION & VISIBILITY",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              modifier = Modifier
                .weight(1f)
                .clickable { publicationStatus = PublicationStatus.PRIVATE_DRAFT },
              shape = RoundedCornerShape(10.dp),
              color = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold.copy(alpha = 0.15f) else AceDarkCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold else AceDarkCardBorder
              )
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold else AceTextMuted,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    "Private Draft",
                    color = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold else AceTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  "Test lossless audio and cue markers privately in your author workspace.",
                  color = AceTextSecondary,
                  fontSize = 10.sp
                )
              }
            }

            Surface(
              modifier = Modifier
                .weight(1f)
                .clickable { publicationStatus = PublicationStatus.PUBLISHED_LIVE },
              shape = RoundedCornerShape(10.dp),
              color = if (publicationStatus == PublicationStatus.PUBLISHED_LIVE) AceEmeraldBg else AceDarkCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (publicationStatus == PublicationStatus.PUBLISHED_LIVE) AceEmerald else AceDarkCardBorder
              )
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    Icons.Default.Public,
                    contentDescription = null,
                    tint = if (publicationStatus == PublicationStatus.PUBLISHED_LIVE) AceEmerald else AceTextMuted,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    "Publish Live",
                    color = if (publicationStatus == PublicationStatus.PUBLISHED_LIVE) AceEmerald else AceTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  "Immediate listing on ACE Public Marketplace for readers.",
                  color = AceTextSecondary,
                  fontSize = 10.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Revenue Projection note
          val priceVal = customPrice.toDoubleOrNull() ?: 14.99
          val authorCut = priceVal * 0.85
          val platformCut = priceVal * 0.15

          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceDarkCard,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceEmerald.copy(alpha = 0.3f))
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(22.dp))
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "85% Creator Share: $${"%.2f".format(authorCut)} per copy",
                  color = AceEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
                Text(
                  text = "ACE Platform Fee (15%): $${"%.2f".format(platformCut)} • Direct automated settlement",
                  color = AceTextSecondary,
                  fontSize = 10.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action buttons
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
              val projId = selectedProject?.projectId ?: "ws_proj_machine2"
              val p = customPrice.toDoubleOrNull() ?: 14.99
              onPublish(projId, customTitle, p, description, releaseDate, format, publicationStatus)
            },
            modifier = Modifier.weight(1.5f).height(46.dp).testTag("publish_to_ace_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold else AceEmerald)
          ) {
            Icon(
              if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) Icons.Default.Lock else Icons.Default.Publish,
              contentDescription = null,
              tint = AceObsidian,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) "Create Private Draft" else "Publish Live to ACE",
              color = AceObsidian,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
