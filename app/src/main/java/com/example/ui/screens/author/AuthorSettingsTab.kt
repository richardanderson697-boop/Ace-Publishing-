package com.example.ui.screens.author

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun AuthorSettingsTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val ws = uiState.authorWorkspace

  var storeName by remember(ws) { mutableStateOf(ws?.storeName ?: "Richard Anderson Publishing") }
  var handle by remember(ws) { mutableStateOf(ws?.handle ?: "@richard_anderson") }
  var bio by remember(ws) { mutableStateOf(ws?.bio ?: "") }
  var payoutEmail by remember(ws) { mutableStateOf(ws?.payoutEmail ?: "RichardAnderson697@gmail.com") }
  var bannerTitle by remember(ws) { mutableStateOf(ws?.bannerTitle ?: "The Machine Universe") }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_settings_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "AUTHOR & STORE SETTINGS",
            color = AceGold,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            letterSpacing = 1.sp
          )
          Text(
            text = "Workspace Profile & Payout Governance",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          OutlinedTextField(
            value = storeName,
            onValueChange = { storeName = it },
            label = { Text("Author / Imprint Name") },
            modifier = Modifier.fillMaxWidth().testTag("settings_store_name_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = handle,
            onValueChange = { handle = it },
            label = { Text("Public Author Handle") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = payoutEmail,
            onValueChange = { payoutEmail = it },
            label = { Text("Direct Royalty Settlement Email (85% Net)") },
            modifier = Modifier.fillMaxWidth().testTag("settings_payout_email_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = bannerTitle,
            onValueChange = { bannerTitle = it },
            label = { Text("Storefront Studio Header Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Author Biography & Studio Vision") },
            maxLines = 4,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = {
              viewModel.updateAuthorSettings(storeName, handle, bio, payoutEmail, bannerTitle)
            },
            modifier = Modifier.fillMaxWidth().height(46.dp).testTag("save_author_settings_btn"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceGold)
          ) {
            Icon(Icons.Default.Save, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Save Workspace Settings", color = AceObsidian, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Write-Sound Studio Bridge & Firebase Authentication Card
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.VpnKey, contentDescription = null, tint = AceGold, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "WRITE-SOUND STUDIO BRIDGE",
                color = AceGold,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 1.sp
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AceEmeraldBg)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text("CONNECTED", color = AceEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Firebase Identity Bridge is active. Material exported from Write-Sound is verified via your Firebase Auth UID and routed into your isolated ACE workspace as a Private Draft.",
            color = AceTextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceObsidian,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Text("Firebase UID Binding:", color = AceTextMuted, fontSize = 10.sp)
              Text(
                text = ws?.firebaseUid ?: "firebase_uid_richard_anderson_77a9",
                color = AceGold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text("Tenant Workspace: ${ws?.authorId ?: "author_richard"}", color = AceTextSecondary, fontSize = 10.sp)
              Text("API Ingestion Endpoint: https://api.ace-audio.com/v1/publish-handshake", color = AceIndigoLight, fontSize = 10.sp)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Automated 8-Step Chapter Ingestion Engine:",
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.height(6.dp))

          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceObsidian,
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              val stepsList = listOf(
                "1. Validate ZIP archive & CRC32 header signatures",
                "2. Extract ElevenLabs WAV/FLAC segments (001-048)",
                "3. Sort in strict narrative speaking sequence order",
                "4. Validate numbering & detect dropped audio frames",
                "5. Concatenate segments with 120ms natural breathing",
                "6. Embed jacket cover & chapter cues in ID3/Vorbis tags",
                "7. Create playable 96kHz spatial master chapter container",
                "8. Generate runtime & write unlisted PRIVATE_DRAFT"
              )
              stepsList.forEach { s ->
                Row(
                  modifier = Modifier.padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(12.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(s, color = AceTextSecondary, fontSize = 10.sp)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Button(
            onClick = {
              viewModel.runApiHandshakeSimulation(
                firebaseUid = ws?.firebaseUid ?: "firebase_uid_richard_anderson_77a9",
                title = "The Machine 2: Part III (ElevenLabs Master)",
                price = 14.99,
                format = com.example.data.models.ProductFormat.AUDIOBOOK
              )
            },
            enabled = !uiState.isApiHandshakeInProgress,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceIndigo),
            modifier = Modifier.fillMaxWidth().height(42.dp)
          ) {
            if (uiState.isApiHandshakeInProgress) {
              CircularProgressIndicator(color = AceTextPrimary, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Executing 8-Step Ingestion Pipeline...", color = AceTextPrimary, fontSize = 11.sp)
            } else {
              Icon(Icons.Default.CloudSync, contentDescription = null, tint = AceTextPrimary, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Simulate [Send to ACE] Ingestion Pipeline", color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }

          if (uiState.apiPipelineSteps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceEmeraldBg,
              shape = RoundedCornerShape(8.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceEmerald.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Pipeline Complete: Playable Chapter Master Created", color = AceEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                uiState.apiPipelineSteps.forEach { step ->
                  Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Top
                  ) {
                    Text("[Step ${step.stepNumber}]", color = AceGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${step.title} — ${step.description}", color = AceTextPrimary, fontSize = 10.sp)
                  }
                }
              }
            }
          }

          if (uiState.apiHandshakeLogs.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              modifier = Modifier.fillMaxWidth(),
              color = AceObsidian,
              shape = RoundedCornerShape(8.dp)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("Raw API & Ingestion Telemetry Stream", color = AceGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                  IconButton(
                    onClick = { viewModel.clearApiHandshakeLogs() },
                    modifier = Modifier.size(20.dp)
                  ) {
                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = AceTextMuted, modifier = Modifier.size(12.dp))
                  }
                }
                Spacer(modifier = Modifier.height(4.dp))
                uiState.apiHandshakeLogs.forEach { logLine ->
                  Text(
                    text = logLine,
                    color = if (logLine.startsWith("[ERROR]")) AceRose else if (logLine.startsWith("[SUCCESS]") || logLine.startsWith("[ACE STATUS]")) AceEmerald else if (logLine.startsWith("[WRITE-SOUND]") || logLine.startsWith("[ACE INGESTION]")) AceGold else AceTextSecondary,
                    fontSize = 9.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                  )
                }
              }
            }
          }
        }
      }
    }

    // Tenancy Isolation & Privacy Guarantee Box
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceIndigoDark.copy(alpha = 0.3f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.4f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = AceIndigoLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "DATA ISOLATION GUARANTEE",
              color = AceIndigoLight,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              letterSpacing = 1.sp
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Each author workspace operates in complete data isolation. Authors cannot view each other's customer records, operational orders, financial statements, or unreleased draft manuscripts.",
            color = AceTextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
          )
        }
      }
    }
  }
}
