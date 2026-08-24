package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import coil.compose.AsyncImage
import com.example.R
import com.example.data.models.ProductFormat
import com.example.data.models.PublicationStatus
import com.example.data.models.WriteSoundStudioProject
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteSoundImportDialog(
  projects: List<WriteSoundStudioProject>,
  isProcessing: Boolean = false,
  processingLogs: List<String> = emptyList(),
  onDismiss: () -> Unit,
  onPublishWriteSound: (projectId: String, title: String, price: Double, description: String, releaseDate: String, format: ProductFormat, publicationStatus: PublicationStatus) -> Unit,
  onPublishStandaloneZip: (zipUri: Uri?, coverUri: Uri?, title: String, price: Double, description: String, releaseDate: String, format: ProductFormat, publicationStatus: PublicationStatus, zipFileName: String) -> Unit
) {
  // Ingestion Mode: 0 = Write-Sound Ecosystem (85%), 1 = Standalone ZIP + Jacket (75%)
  var selectedTab by remember { mutableStateOf(0) }

  // Write-Sound selection
  var selectedProject by remember { mutableStateOf(projects.firstOrNull()) }

  // Standalone ZIP selection
  var selectedZipUri by remember { mutableStateOf<Uri?>(null) }
  var selectedZipName by remember { mutableStateOf("the_machine_2_elevenlabs_master.zip") }
  var selectedCoverUri by remember { mutableStateOf<Uri?>(null) }

  // Form Fields
  var customTitle by remember { mutableStateOf(selectedProject?.title ?: "The Machine 2: Autonomous Dawn") }
  var customPrice by remember { mutableStateOf(selectedProject?.defaultPrice?.toString() ?: "14.99") }
  var description by remember { mutableStateOf(selectedProject?.synopsis ?: "") }
  var releaseDate by remember { mutableStateOf("September 15, 2026") }
  var format by remember { mutableStateOf(ProductFormat.BUNDLE) }
  var publicationStatus by remember { mutableStateOf(PublicationStatus.PRIVATE_DRAFT) }

  // Launchers for picking files
  val zipPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      selectedZipUri = uri
      selectedZipName = uri.lastPathSegment?.substringAfterLast("/") ?: "custom_audio_package.zip"
    }
  }

  val coverPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    if (uri != null) {
      selectedCoverUri = uri
    }
  }

  // Update fields when project selection changes in Tab 0
  LaunchedEffect(selectedProject, selectedTab) {
    if (selectedTab == 0) {
      selectedProject?.let {
        customTitle = it.title
        customPrice = it.defaultPrice.toString()
        description = it.synopsis
      }
    } else {
      if (customTitle.isBlank()) customTitle = "Master Chapter 1: The First Resonance"
      if (description.isBlank()) description = "Standalone audio master unpacked from 48 ElevenLabs voice segments with embedded jacket art."
    }
  }

  Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.94f)
        .testTag("write_sound_import_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(18.dp)
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
                text = "Audio Ingestion & Publishing",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
              Text(
                text = "Write-Sound Bridge & ZIP Packager",
                color = AceGold,
                fontSize = 11.sp
              )
            }
          }

          if (!isProcessing) {
            IconButton(onClick = onDismiss) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = AceTextMuted)
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Ingestion Channel Tabs (Write-Sound 85% vs Standalone ZIP 75%)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(AceDarkCard)
            .padding(3.dp)
        ) {
          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .clickable { if (!isProcessing) selectedTab = 0 },
            color = if (selectedTab == 0) AceIndigo else AceDarkCard
          ) {
            Column(
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "Write-Sound (85% Royalty)",
                color = if (selectedTab == 0) AceTextPrimary else AceTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
              Text(
                text = "Ecosystem Direct • 0% Intake Fee",
                color = if (selectedTab == 0) AceGold else AceTextMuted,
                fontSize = 9.sp
              )
            }
          }

          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .clickable { if (!isProcessing) selectedTab = 1 },
            color = if (selectedTab == 1) AceGold else AceDarkCard
          ) {
            Column(
              modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "Standalone ZIP (75% Royalty)",
                color = if (selectedTab == 1) AceObsidian else AceTextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
              Text(
                text = "External Upload • +10% Intake Fee",
                color = if (selectedTab == 1) AceObsidian.copy(alpha = 0.8f) else AceTextMuted,
                fontSize = 9.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(10.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
          if (selectedTab == 0) {
            // --- TAB 0: WRITE-SOUND STUDIO (85% ROYALTY) ---
            Text(
              text = "1. SELECT WRITE-SOUND STUDIO PROJECT",
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
                    Text(proj.title, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(proj.subtitle, color = AceTextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      "${proj.chapterCount} Chapters • ${proj.masteredAudioDurationMin / 60}h ${proj.masteredAudioDurationMin % 60}m Master Audio",
                      color = AceGold,
                      fontSize = 10.sp
                    )
                  }
                  if (isSelected) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AceGold, modifier = Modifier.size(20.dp))
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceObsidian,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceEmerald.copy(alpha = 0.4f))
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  "Verified Ecosystem Project: 85% direct creator payout on all marketplace sales.",
                  color = AceTextSecondary,
                  fontSize = 10.sp
                )
              }
            }

          } else {
            // --- TAB 1: STANDALONE ZIP & JACKET PHOTO INTAKE (75% ROYALTY) ---
            Text(
              text = "1. STANDALONE AUDIO ZIP & JACKET ASSETS",
              color = AceTextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // ZIP Picker Card
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceDarkCard,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FolderZip, contentDescription = null, tint = AceGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Audio Archive (.ZIP)", color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  }

                  Button(
                    onClick = { zipPickerLauncher.launch("*/*") },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AceIndigo),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                  ) {
                    Icon(Icons.Default.FileOpen, contentDescription = null, tint = AceTextPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pick ZIP File", color = AceTextPrimary, fontSize = 10.sp)
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                  text = "File: $selectedZipName",
                  color = AceGold,
                  fontSize = 11.sp,
                  fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                  text = "Contains raw voice segments (e.g. 001..048). ACE will unpack, sequence, stitch, and calibrate runtime.",
                  color = AceTextMuted,
                  fontSize = 10.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cover Photo Picker Card
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceDarkCard,
              shape = RoundedCornerShape(12.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                if (selectedCoverUri != null) {
                  AsyncImage(
                    model = selectedCoverUri,
                    contentDescription = "User Jacket Art",
                    modifier = Modifier
                      .size(54.dp)
                      .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                  )
                } else {
                  Image(
                    painter = painterResource(id = R.drawable.cover_machine2_1787574231599),
                    contentDescription = "Default Jacket Art",
                    modifier = Modifier
                      .size(54.dp)
                      .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text("Jacket Cover Artwork", color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                  Text(
                    if (selectedCoverUri != null) "User custom photo selected" else "Using default high-res cover (or ZIP embedded art)",
                    color = AceTextSecondary,
                    fontSize = 10.sp
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  OutlinedButton(
                    onClick = { coverPickerLauncher.launch("image/*") },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                  ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = AceGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Select Jacket Image", color = AceGold, fontSize = 9.sp)
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Standalone Fee Transparency Card
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceObsidian,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.4f))
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Info, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Standalone Royalty & Transcoding Terms", color = AceGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  "• 75% Creator Royalty Rate (vs 85% for Write-Sound ecosystem)\n• 15% Standard ACE Platform Fee\n• 10% Standalone Audio Ingestion & Transcoding Fee",
                  color = AceTextSecondary,
                  fontSize = 10.sp,
                  lineHeight = 14.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 2. Publication Details
          Text(
            text = "2. PUBLICATION METADATA & FORMAT",
            color = AceTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = customTitle,
            onValueChange = { customTitle = it },
            label = { Text("Book / Chapter Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedTextColor = AceTextPrimary,
              unfocusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
              value = customPrice,
              onValueChange = { customPrice = it },
              label = { Text("Price ($ USD)") },
              modifier = Modifier.weight(1f),
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AceGold,
                unfocusedBorderColor = AceDarkCardBorder,
                focusedTextColor = AceTextPrimary,
                unfocusedTextColor = AceTextPrimary
              )
            )

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
              value = releaseDate,
              onValueChange = { releaseDate = it },
              label = { Text("Release Date") },
              modifier = Modifier.weight(1f),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AceGold,
                unfocusedBorderColor = AceDarkCardBorder,
                focusedTextColor = AceTextPrimary,
                unfocusedTextColor = AceTextPrimary
              )
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Synopsis & Metadata") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedTextColor = AceTextPrimary,
              unfocusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // 3. Automated Ingestion Pipeline Notice
          Text(
            text = "3. ACE AUTOMATED INGESTION ENGINE",
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
            Column(modifier = Modifier.padding(10.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ZIP Unpack • Segment Stitching • Jacket Tagging", color = AceGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "ACE unpacks audio segments (001..048), verifies sequential continuity, concatenates into a master stream, writes ID3 embedded jacket cover art and chapter cues, and calibrates total playback duration.",
                color = AceTextSecondary,
                fontSize = 10.sp,
                lineHeight = 14.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // 4. Publication Destination (Private Draft vs Live)
          Text(
            text = "4. PUBLICATION DESTINATION",
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
                    fontSize = 11.sp
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  "Listen & verify in author workspace before going live.",
                  color = AceTextSecondary,
                  fontSize = 9.sp
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
                    fontSize = 11.sp
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  "Immediate listing on public ACE Marketplace.",
                  color = AceTextSecondary,
                  fontSize = 9.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Revenue Projection
          val priceVal = customPrice.toDoubleOrNull() ?: 14.99
          val royaltyPercent = if (selectedTab == 0) 85.0 else 75.0
          val creatorCut = priceVal * (royaltyPercent / 100.0)
          val totalAceFee = priceVal * ((100.0 - royaltyPercent) / 100.0)

          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceDarkCard,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Your Net Payout (${royaltyPercent.toInt()}%):",
                  color = AceEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
                Text(
                  text = "$${"%.2f".format(creatorCut)} / sale",
                  color = AceEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = if (selectedTab == 0) "ACE Platform Fee (15%):" else "ACE Fee + 10% Transcoding (25%):",
                  color = AceTextMuted,
                  fontSize = 10.sp
                )
                Text(
                  text = "$${"%.2f".format(totalAceFee)}",
                  color = AceTextMuted,
                  fontSize = 10.sp
                )
              }
            }
          }

          // Processing Logs (if in progress)
          if (isProcessing || processingLogs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceObsidian,
              shape = RoundedCornerShape(8.dp)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text("ZIP Extraction & Audio Packaging Stream", color = AceGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                processingLogs.forEach { logLine ->
                  Text(
                    text = logLine,
                    color = if (logLine.startsWith("[ERROR]")) AceRose else if (logLine.startsWith("[8/8]") || logLine.startsWith("[7/8]")) AceEmerald else AceTextSecondary,
                    fontSize = 9.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            enabled = !isProcessing,
            modifier = Modifier.weight(1f).height(46.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AceTextSecondary)
          ) {
            Text("Cancel")
          }

          Button(
            onClick = {
              val p = customPrice.toDoubleOrNull() ?: 14.99
              if (selectedTab == 0) {
                val projId = selectedProject?.projectId ?: "ws_proj_machine2"
                onPublishWriteSound(projId, customTitle, p, description, releaseDate, format, publicationStatus)
              } else {
                onPublishStandaloneZip(
                  selectedZipUri,
                  selectedCoverUri,
                  customTitle,
                  p,
                  description,
                  releaseDate,
                  format,
                  publicationStatus,
                  selectedZipName
                )
              }
            },
            enabled = !isProcessing,
            modifier = Modifier.weight(1.5f).height(46.dp).testTag("publish_to_ace_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) AceGold else AceEmerald
            )
          ) {
            if (isProcessing) {
              CircularProgressIndicator(color = AceObsidian, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
              Spacer(modifier = Modifier.width(6.dp))
              Text("Packaging Audio...", color = AceObsidian, fontWeight = FontWeight.Bold)
            } else {
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
}
