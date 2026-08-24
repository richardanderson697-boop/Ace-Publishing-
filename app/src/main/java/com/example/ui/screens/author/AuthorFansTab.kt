package com.example.ui.screens.author

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.FanPostCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun AuthorFansTab(
  uiState: AceUiState,
  viewModel: AceViewModel
) {
  val ws = uiState.authorWorkspace
  val authorPosts = uiState.fanPosts.filter { it.authorId == uiState.currentAuthorId }
  val totalPlays = authorPosts.sumOf { it.previewPlays }
  val totalComments = authorPosts.sumOf { it.commentCount }
  val totalConversions = authorPosts.sumOf { it.conversionsCount }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
      .testTag("author_fans_tab"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Fan Activity Dashboard Header
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceDarkCard,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "FAN COMMUNITY & ATTRIBUTION",
                color = AceGold,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp
              )
              Text(
                text = "Fan Activity & Direct Conversions",
                color = AceTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }

            Button(
              onClick = { viewModel.openNewFanPost(true) },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceGold),
              modifier = Modifier.testTag("create_fan_post_button")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Create Post", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 4 Key Fan Metrics Grid (As defined in user prompt: 312 followers, 48 active, 23 comments, 17 preview plays)
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FanStatTile(
              count = "${ws?.followerCount ?: 312}",
              label = "Followers",
              icon = Icons.Default.Groups,
              tint = AceGold,
              modifier = Modifier.weight(1f)
            )
            FanStatTile(
              count = "${ws?.activeFollowersThisWeek ?: 48}",
              label = "Active This Week",
              icon = Icons.Default.Bolt,
              tint = AceIndigoLight,
              modifier = Modifier.weight(1f)
            )
            FanStatTile(
              count = "$totalComments",
              label = "Comments",
              icon = Icons.Default.ChatBubble,
              tint = AceBlue,
              modifier = Modifier.weight(1f)
            )
            FanStatTile(
              count = "$totalPlays",
              label = "Preview Plays",
              icon = Icons.Default.Headphones,
              tint = AceEmerald,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Conversion insight callout
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = AceEmeraldBg.copy(alpha = 0.4f),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceEmerald.copy(alpha = 0.4f))
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Insights, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Post-to-Purchase Attribution Active",
                  color = AceEmerald,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
                Text(
                  text = "$totalConversions purchases directly converted from your teaser posts this month. Your preview plays correlate directly with sales.",
                  color = AceTextPrimary,
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }
    }

    // 2. Author's Posts Feed
    item {
      Text(
        text = "YOUR POSTS & ENGAGEMENT ANALYTICS",
        color = AceTextSecondary,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        letterSpacing = 1.sp
      )
    }

    if (authorPosts.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
          contentAlignment = Alignment.Center
        ) {
          Text("No posts published yet. Share a teaser with fans!", color = AceTextSecondary, fontSize = 13.sp)
        }
      }
    } else {
      items(authorPosts, key = { it.postId }) { post ->
        FanPostCard(
          post = post,
          isAuthorView = true,
          isPlayingAudio = uiState.currentlyPlayingSample?.title == post.audioPreviewTitle && (uiState.currentlyPlayingSample?.isPlaying == true),
          onPlayAudioTeaser = {
            if (post.audioPreviewTitle != null) {
              viewModel.playAudioSample(
                title = post.audioPreviewTitle,
                authorName = post.authorName,
                coverRes = R.drawable.cover_machine2_1787574231599,
                durationSeconds = post.audioDurationSec,
                postId = post.postId
              )
            }
          },
          onToggleLike = { viewModel.toggleLikePost(post.postId) },
          onOpenComments = { viewModel.openComments(post.postId) },
          onBuyAttachedProduct = { pId ->
            val p = uiState.marketplaceProducts.find { it.id == pId }
            if (p != null) {
              viewModel.addToCart(p)
              viewModel.checkoutCart(attributionPostId = post.postId)
            }
          }
        )
      }
    }
  }
}

@Composable
private fun FanStatTile(
  count: String,
  label: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tint: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    color = AceDarkSurface,
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(
      modifier = Modifier.padding(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(count, color = AceTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
      Text(label, color = AceTextSecondary, fontSize = 8.sp, maxLines = 1)
    }
  }
}
