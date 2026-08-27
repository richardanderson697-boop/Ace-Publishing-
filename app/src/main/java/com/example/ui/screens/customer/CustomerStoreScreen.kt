package com.example.ui.screens.customer

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
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceUiState
import com.example.ui.viewmodel.AceViewModel

@Composable
fun CustomerStoreScreen(
  uiState: AceUiState,
  viewModel: AceViewModel,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedGenre by remember { mutableStateOf<String?>(null) }

  val genres = listOf("All", "Sci-Fi Thriller", "Cyberpunk", "Space Opera", "Noir Mystery", "Audiobook")

  val filteredProducts = remember(uiState.marketplaceProducts, searchQuery, selectedGenre) {
    uiState.marketplaceProducts.filter { product ->
      val matchesSearch = searchQuery.isBlank() ||
        product.title.contains(searchQuery, ignoreCase = true) ||
        product.authorName.contains(searchQuery, ignoreCase = true) ||
        product.description.contains(searchQuery, ignoreCase = true)

      val matchesGenre = selectedGenre == null || selectedGenre == "All" || product.genres.any { it.contains(selectedGenre!!, ignoreCase = true) }

      matchesSearch && matchesGenre
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(AceDarkSurface)
      .padding(14.dp)
      .testTag("customer_store_screen"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Hero Banner: Spotlight on Write-Sound mastered releases
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = AceDarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.4f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.linearGradient(listOf(AceDarkCard, AceIndigoDark.copy(alpha = 0.6f)))
            )
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AceGold)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text("SPOTLIGHT RELEASE", color = AceObsidian, fontSize = 9.sp, fontWeight = FontWeight.Black)
            }
            Text("85% Directly to Creators", color = AceEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "The Machine 2: Autonomous Dawn",
            color = AceTextPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp
          )
          Text(
            text = "By Richard Anderson • Mastered with 3D Spatial Audio",
            color = AceGold,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(10.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            val spotlight = uiState.marketplaceProducts.firstOrNull()
            Button(
              onClick = {
                if (spotlight != null) viewModel.selectProductForDetail(spotlight)
              },
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = AceGold),
              modifier = Modifier.weight(1f).testTag("spotlight_view_button")
            ) {
              Text("Explore Book", color = AceObsidian, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            OutlinedButton(
              onClick = {
                if (spotlight != null) {
                  viewModel.playAudioSample(
                    title = spotlight.title,
                    authorName = spotlight.authorName,
                    coverRes = spotlight.coverDrawableRes,
                    durationSeconds = spotlight.audioDurationMinutes * 60
                  )
                }
              },
              shape = RoundedCornerShape(10.dp),
              border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.6f)),
              modifier = Modifier.weight(1f).testTag("spotlight_play_sample_button")
            ) {
              Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Listen Sample", color = AceGold, fontSize = 12.sp)
            }
          }
        }
      }
    }

    // Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search books, audiobooks, authors...", color = AceTextMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AceGold) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Clear", tint = AceTextSecondary)
            }
          }
        },
        modifier = Modifier.fillMaxWidth().testTag("store_search_input"),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AceGold,
          unfocusedBorderColor = AceDarkCardBorder,
          unfocusedTextColor = AceTextPrimary,
          focusedTextColor = AceTextPrimary,
          focusedContainerColor = AceDarkCard,
          unfocusedContainerColor = AceDarkCard
        )
      )
    }

    // Genre Filter Pills
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        genres.forEach { g ->
          val isSelected = (selectedGenre == g) || (selectedGenre == null && g == "All")
          FilterChip(
            selected = isSelected,
            onClick = { selectedGenre = if (g == "All") null else g },
            label = { Text(g, fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = AceGold,
              selectedLabelColor = AceObsidian,
              labelColor = AceTextSecondary
            )
          )
        }
      }
    }

    // Products List
    items(filteredProducts, key = { it.id }) { product ->
      StorefrontBookCard(
        product = product,
        onOpenDetail = { viewModel.selectProductForDetail(product) },
        onAddToCart = { viewModel.addToCart(product) },
        onPlaySample = {
          viewModel.playAudioSample(
            title = product.title,
            authorName = product.authorName,
            coverRes = product.coverDrawableRes,
            durationSeconds = product.audioDurationMinutes * 60,
            audioFilePath = product.localAudioPath,
            localCoverUri = product.localCoverUri
          )
        }
      )
    }
  }
}

@Composable
fun StorefrontBookCard(
  product: BookProduct,
  onOpenDetail: () -> Unit,
  onAddToCart: () -> Unit,
  onPlaySample: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onOpenDetail() }
      .testTag("product_card_${product.id}"),
    color = AceDarkCard,
    shape = RoundedCornerShape(16.dp),
    border = androidx.compose.foundation.BorderStroke(1.dp, AceDarkCardBorder)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        // Book Cover Art
        Image(
          painter = painterResource(id = product.coverDrawableRes),
          contentDescription = product.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .width(85.dp)
            .height(118.dp)
            .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(AceIndigoDark.copy(alpha = 0.4f))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(product.format.displayName, color = AceIndigoLight, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = product.title,
            color = AceTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 19.sp
          )
          Text(
            text = "By ${product.authorName}",
            color = AceGold,
            fontSize = 12.sp
          )

          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${product.chapterCount} Chapters • ${product.audioDurationMinutes / 60}h ${product.audioDurationMinutes % 60}m Audio",
            color = AceTextSecondary,
            fontSize = 11.sp
          )

          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = AceGold, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("${product.rating}", color = AceTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(3.dp))
            Text("(${product.reviewCount})", color = AceTextMuted, fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))
      Text(
        text = product.description,
        color = AceTextSecondary,
        fontSize = 12.sp,
        maxLines = 2,
        lineHeight = 16.sp
      )

      Spacer(modifier = Modifier.height(12.dp))
      Divider(color = AceDarkCardBorder.copy(alpha = 0.6f))
      Spacer(modifier = Modifier.height(8.dp))

      // Bottom Row: Price & Actions
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
            fontSize = 16.sp
          )
          Text(
            text = "85% directly to author",
            color = AceEmerald,
            fontSize = 9.sp
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedButton(
            onClick = onPlaySample,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(36.dp).testTag("play_sample_${product.id}"),
            border = androidx.compose.foundation.BorderStroke(1.dp, AceGold.copy(alpha = 0.5f))
          ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = AceGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Sample", color = AceGold, fontSize = 11.sp)
          }

          Button(
            onClick = onAddToCart,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(36.dp).testTag("add_to_cart_${product.id}"),
            colors = ButtonDefaults.buttonColors(containerColor = AceGold)
          ) {
            Icon(Icons.Default.AddShoppingCart, contentDescription = null, tint = AceObsidian, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add to Cart", color = AceObsidian, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
