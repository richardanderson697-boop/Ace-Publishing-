package com.example.ui.screens.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Headphones
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
fun CustomerFanFeedScreen(
  uiState: AceUiState,
  viewModel: AceViewModel,
  modifier: Modifier = Modifier
) {
  val posts = uiState.fanPosts

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(AceDarkSurface)
      .padding(14.dp)
      .testTag("customer_fan_feed_screen"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header Banner
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AceIndigoDark.copy(alpha = 0.35f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.4f))
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Brush.linearGradient(listOf(AceGold, AceIndigo))),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Campaign, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(24.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "FAN COMMUNITY & DROPS",
              color = AceGold,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 1.sp
            )
            Text(
              text = "Direct from Independent Authors",
              color = AceTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = "Listen to exclusive 3D audio chapter teasers and support creators directly.",
              color = AceTextSecondary,
              fontSize = 11.sp
            )
          }
        }
      }
    }

    items(posts, key = { it.postId }) { post ->
      FanPostCard(
        post = post,
        isAuthorView = false,
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
