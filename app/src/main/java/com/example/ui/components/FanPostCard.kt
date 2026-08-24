package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.data.models.FanPost
import com.example.ui.theme.*

@Composable
fun FanPostCard(
  post: FanPost,
  isAuthorView: Boolean,
  isPlayingAudio: Boolean,
  onPlayAudioTeaser: () -> Unit,
  onToggleLike: () -> Unit,
  onOpenComments: () -> Unit,
  onBuyAttachedProduct: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag("fan_post_${post.postId}"),
    color = AceDarkCard,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Header: Author Avatar & Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(listOf(AceGold, AceIndigo))
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = post.authorName.take(1),
              color = AceObsidian,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = post.authorName,
              color = AceTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = post.authorHandle,
              color = AceTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        // Tag chip
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AceIndigoDark.copy(alpha = 0.4f))
            .border(1.dp, AceIndigoLight.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = post.tag,
            color = AceIndigoLight,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Title & Content
      Text(
        text = post.title,
        color = AceGold,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = post.content,
        color = AceTextPrimary,
        fontSize = 13.sp,
        lineHeight = 18.sp
      )

      // Audio Teaser Player Widget (if present)
      if (!post.audioPreviewTitle.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onPlayAudioTeaser() },
          color = AceDarkSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, AceIndigoLight.copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isPlayingAudio) AceEmerald else AceGold),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = "Play Teaser",
                tint = AceObsidian,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = post.audioPreviewTitle,
                color = AceTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
              )
              Text(
                text = "Audiobook Clip • Spatial Master (${post.audioDurationSec}s)",
                color = AceGold,
                fontSize = 10.sp
              )
            }
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = if (isPlayingAudio) AceEmerald else AceIndigoLight,
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }

      // Attached Book Buy Widget (Direct Conversion Trigger)
      if (post.attachedProductId != null && post.attachedProductTitle != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          color = AceDarkSurface,
          border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Featured Release",
                color = AceTextMuted,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
              Text(
                text = post.attachedProductTitle,
                color = AceTextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                maxLines = 1
              )
              Text(
                text = "$${"%.2f".format(post.attachedProductPrice ?: 14.99)} • 85% Creator Share",
                color = AceEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
              )
            }
            Button(
              onClick = { onBuyAttachedProduct(post.attachedProductId) },
              modifier = Modifier.height(34.dp).testTag("buy_from_post_${post.postId}"),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceGold)
            ) {
              Text("Get Book", color = AceObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Author Attribution Performance Banner (Answers: "Which posts lead to purchases?")
      if (isAuthorView) {
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = AceEmeraldBg.copy(alpha = 0.35f),
          border = androidx.compose.foundation.BorderStroke(1.dp, AceEmerald.copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.TrendingUp, contentDescription = null, tint = AceEmerald, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Conversion Attribution:",
                color = AceEmerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "${post.conversionsCount} Direct Purchases ($${"%.2f".format(post.conversionsCount * (post.attachedProductPrice ?: 14.99) * 0.85)} creator net)",
              color = AceTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      Divider(color = AceDarkCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(8.dp))

      // Interaction Bar: Likes, Comments, Audio Plays, Shares
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Like button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { onToggleLike() }
              .testTag("like_post_${post.postId}")
          ) {
            Icon(
              imageVector = if (post.isLikedByCurrentUser) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
              contentDescription = "Like",
              tint = if (post.isLikedByCurrentUser) AceRose else AceTextSecondary,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${post.likesCount}",
              color = if (post.isLikedByCurrentUser) AceRose else AceTextSecondary,
              fontSize = 12.sp
            )
          }

          // Comments button
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable { onOpenComments() }
              .testTag("comments_post_${post.postId}")
          ) {
            Icon(
              imageVector = Icons.Default.ChatBubbleOutline,
              contentDescription = "Comments",
              tint = AceTextSecondary,
              modifier = Modifier.size(17.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${post.commentCount}",
              color = AceTextSecondary,
              fontSize = 12.sp
            )
          }

          // Audio Plays
          if (post.previewPlays > 0) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Headphones,
                contentDescription = null,
                tint = AceIndigoLight,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${post.previewPlays} plays",
                color = AceIndigoLight,
                fontSize = 11.sp
              )
            }
          }
        }

        Text(
          text = "Yesterday",
          color = AceTextMuted,
          fontSize = 10.sp
        )
      }
    }
  }
}
