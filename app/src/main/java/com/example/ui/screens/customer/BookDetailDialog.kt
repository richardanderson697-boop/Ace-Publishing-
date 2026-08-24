package com.example.ui.screens.customer

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.BookProduct
import com.example.ui.theme.*

@Composable
fun BookDetailDialog(
  product: BookProduct,
  isFollowingAuthor: Boolean,
  onDismiss: () -> Unit,
  onToggleFollowAuthor: () -> Unit,
  onAddToCart: () -> Unit,
  onPlayAudioSample: () -> Unit,
  onInstantBuy: () -> Unit
) {
  var selectedTab by remember { mutableStateOf(0) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.92f)
        .testTag("book_detail_dialog"),
      shape = RoundedCornerShape(22.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
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
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AceIndigoDark.copy(alpha = 0.4f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(product.format.displayName, color = AceIndigoLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = AceTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
        ) {
          // Cover & Main Metadata
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Image(
              painter = painterResource(id = product.coverDrawableRes),
              contentDescription = product.title,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .width(110.dp)
                .height(155.dp)
                .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = product.title,
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                lineHeight = 22.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "By ${product.authorName}",
                color = AceGold,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Follow Author Button
              OutlinedButton(
                onClick = onToggleFollowAuthor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp).testTag("detail_follow_author_btn"),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFollowingAuthor) AceEmerald else AceGold)
              ) {
                Icon(
                  imageVector = if (isFollowingAuthor) Icons.Default.Check else Icons.Default.PersonAdd,
                  contentDescription = null,
                  tint = if (isFollowingAuthor) AceEmerald else AceGold,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isFollowingAuthor) "Following" else "Follow Author",
                  color = if (isFollowingAuthor) AceEmerald else AceGold,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "${product.chapterCount} Chapters • ${product.audioDurationMinutes / 60}h ${product.audioDurationMinutes % 60}m Mastered Audio",
                color = AceTextSecondary,
                fontSize = 11.sp
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = AceGold, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${product.rating} (${product.reviewCount} reviews)", color = AceTextPrimary, fontSize = 11.sp)
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Tab Selector: Synopsis / Sample Text / Audio
          TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AceDarkCard,
            contentColor = AceGold,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
          ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
              Text("Synopsis", fontSize = 12.sp, modifier = Modifier.padding(vertical = 10.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
              Text("Sample Reader", fontSize = 12.sp, modifier = Modifier.padding(vertical = 10.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
              Text("Audio Sample", fontSize = 12.sp, modifier = Modifier.padding(vertical = 10.dp))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          when (selectedTab) {
            0 -> {
              Text(
                text = product.description,
                color = AceTextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text("Genres & Tags:", color = AceTextSecondary, fontSize = 11.sp)
              Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                product.genres.forEach { g ->
                  SuggestionChip(
                    onClick = {},
                    label = { Text(g, fontSize = 10.sp, color = AceGold) },
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = AceDarkCard)
                  )
                }
              }
            }
            1 -> {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = AceDarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Text(
                    text = product.previewSampleTitle,
                    color = AceGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                  )
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = product.sampleText.ifBlank { product.description },
                    color = AceTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                  )
                }
              }
            }
            2 -> {
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = AceDarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.4f))
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Headphones, contentDescription = null, tint = AceGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mastered Spatial Audio Clip", color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                  }
                  Spacer(modifier = Modifier.height(8.dp))
                  Text(
                    text = "Experience the cast narration and spatial sound master direct from Write-Sound Studio.",
                    color = AceTextSecondary,
                    fontSize = 11.sp
                  )
                  Spacer(modifier = Modifier.height(10.dp))
                  Button(
                    onClick = onPlayAudioSample,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AceGold),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceObsidian)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Play Audio Sample (${product.audioDurationMinutes / 10} min clip)", color = AceObsidian, fontWeight = FontWeight.Bold)
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(12.dp))

        // Price & Purchase Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "$${"%.2f".format(product.price)}",
              color = AceTextPrimary,
              fontWeight = FontWeight.Black,
              fontSize = 20.sp
            )
            Text("85% ($${"%.2f".format(product.price * 0.85)}) to Author", color = AceEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
              onClick = onAddToCart,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.height(44.dp).testTag("detail_add_to_cart_btn"),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
            ) {
              Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add to Cart", color = AceGold, fontSize = 11.sp)
            }

            Button(
              onClick = onInstantBuy,
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.height(44.dp).testTag("detail_instant_buy_btn"),
              colors = ButtonDefaults.buttonColors(containerColor = AceGold)
            ) {
              Text("Buy Now", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
          }
        }
      }
    }
  }
}
