package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.models.BookProduct
import com.example.ui.theme.*

@Composable
fun NewFanPostDialog(
  authorBooks: List<BookProduct>,
  onDismiss: () -> Unit,
  onPost: (title: String, content: String, audioTitle: String?, attachedProductId: String?, tag: String) -> Unit
) {
  var title by remember { mutableStateOf("✨ New chapter coming Friday: The Machine 2 — Chapter 6 Teaser") }
  var content by remember { mutableStateOf("Just finalized the spatial audio mix in Write-Sound. Listen to this 3-minute clip where the orbital defense perimeter discovers the AI has self-evolved.") }
  var includeAudio by remember { mutableStateOf(true) }
  var audioTitle by remember { mutableStateOf("The Machine 2 — Chapter 6 Audio Preview") }
  var selectedProduct by remember { mutableStateOf(authorBooks.firstOrNull()) }
  var tag by remember { mutableStateOf("📢 Release Announcement") }

  val tagOptions = listOf("📢 Release Announcement", "🎧 Audiobook Preview", "📖 Behind the Scenes", "✨ New Chapter", "💬 Author Post")

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f)
        .testTag("new_fan_post_dialog"),
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
                .background(Brush.linearGradient(listOf(AceGold, AceGoldDark))),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Campaign,
                contentDescription = null,
                tint = AceObsidian,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Create Fan Community Post",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Text(
                text = "Includes Direct Purchase Attribution",
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
          Text("Select Post Category:", color = AceTextSecondary, fontSize = 11.sp)
          Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            tagOptions.take(3).forEach { t ->
              FilterChip(
                selected = tag == t,
                onClick = { tag = t },
                label = { Text(t, fontSize = 10.sp) },
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
            value = title,
            onValueChange = { title = it },
            label = { Text("Post Headline") },
            modifier = Modifier.fillMaxWidth().testTag("fan_post_title_input"),
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
            value = content,
            onValueChange = { content = it },
            label = { Text("Message & Story Behind The Scenes") },
            maxLines = 4,
            modifier = Modifier.fillMaxWidth().testTag("fan_post_content_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              focusedLabelColor = AceGold,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Audio preview toggle
          Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AceDarkCard,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.Headphones, contentDescription = null, tint = AceIndigoLight)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Attach Audio Teaser / Sample", color = AceTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Switch(
                  checked = includeAudio,
                  onCheckedChange = { includeAudio = it },
                  colors = SwitchDefaults.colors(checkedThumbColor = AceGold, checkedTrackColor = AceIndigoDark)
                )
              }

              if (includeAudio) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = audioTitle,
                  onValueChange = { audioTitle = it },
                  label = { Text("Audio Track Sample Name") },
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
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Link to product for direct conversion attribution
          Text("Attach Book For 1-Click Purchase & Attribution:", color = AceTextSecondary, fontSize = 11.sp)
          Spacer(modifier = Modifier.height(6.dp))

          authorBooks.forEach { book ->
            val isSelected = selectedProduct?.id == book.id
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .clickable { selectedProduct = if (isSelected) null else book },
              color = if (isSelected) AceIndigoDark.copy(alpha = 0.4f) else AceDarkCard,
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) AceGold else AceDarkCardBorder
              )
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                  contentDescription = null,
                  tint = if (isSelected) AceGold else AceTextMuted,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(book.title, color = AceTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                  Text("$${"%.2f".format(book.price)} • ${book.format.displayName}", color = AceGold, fontSize = 11.sp)
                }
              }
            }
          }
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
              if (title.isNotBlank() && content.isNotBlank()) {
                onPost(
                  title,
                  content,
                  if (includeAudio) audioTitle else null,
                  selectedProduct?.id,
                  tag
                )
              }
            },
            modifier = Modifier.weight(1.5f).height(46.dp).testTag("publish_fan_post_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AceGold)
          ) {
            Icon(Icons.Default.Send, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Publish Post", color = AceObsidian, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
