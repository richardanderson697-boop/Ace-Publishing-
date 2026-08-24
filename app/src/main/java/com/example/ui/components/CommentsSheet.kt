package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.FanComment
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(
  postId: String,
  comments: List<FanComment>,
  isAuthorRole: Boolean,
  onDismiss: () -> Unit,
  onPostComment: (String) -> Unit
) {
  var commentInput by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.75f)
        .testTag("comments_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = AceDarkSurface,
      border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Fan Discussion",
              color = AceTextPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Text(
              text = "${comments.size} comments",
              color = AceGold,
              fontSize = 11.sp
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = AceTextMuted)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = AceDarkCardBorder)
        Spacer(modifier = Modifier.height(10.dp))

        // Comments list
        if (comments.isEmpty()) {
          Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Text("No comments yet. Start the conversation!", color = AceTextSecondary, fontSize = 13.sp)
          }
        } else {
          LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(comments) { comment ->
              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = if (comment.isAuthorReply) AceIndigoDark.copy(alpha = 0.35f) else AceDarkCard,
                border = androidx.compose.foundation.BorderStroke(
                  1.dp,
                  if (comment.isAuthorReply) AceIndigoLight.copy(alpha = 0.4f) else AceDarkCardBorder
                )
              ) {
                Column(modifier = Modifier.padding(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                      Text(
                        text = comment.userName,
                        color = if (comment.isAuthorReply) AceGold else AceTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                      )
                      if (comment.isAuthorReply) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                          modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AceGold)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                          Text("AUTHOR", color = AceObsidian, fontSize = 8.sp, fontWeight = FontWeight.Black)
                        }
                      }
                    }
                    Text(comment.formattedTime, color = AceTextMuted, fontSize = 10.sp)
                  }
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(comment.content, color = AceTextPrimary, fontSize = 12.sp, lineHeight = 16.sp)
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Comment input bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = commentInput,
            onValueChange = { commentInput = it },
            placeholder = { Text(if (isAuthorRole) "Reply as Author..." else "Write a comment...", fontSize = 12.sp) },
            modifier = Modifier.weight(1f).testTag("comment_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = AceGold,
              unfocusedBorderColor = AceDarkCardBorder,
              unfocusedTextColor = AceTextPrimary,
              focusedTextColor = AceTextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (commentInput.isNotBlank()) {
                onPostComment(commentInput)
                commentInput = ""
              }
            },
            modifier = Modifier
              .size(44.dp)
              .background(AceGold, RoundedCornerShape(12.dp))
              .testTag("send_comment_button")
          ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = AceObsidian)
          }
        }
      }
    }
  }
}
