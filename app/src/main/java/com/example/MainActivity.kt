package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.models.UserRole
import com.example.ui.components.*
import com.example.ui.screens.admin.AdminWorkspaceScreen
import com.example.ui.screens.author.AuthorWorkspaceScreen
import com.example.ui.screens.customer.BookDetailDialog
import com.example.ui.screens.customer.CustomerCartSheet
import com.example.ui.screens.customer.CustomerFanFeedScreen
import com.example.ui.screens.customer.CustomerLibraryScreen
import com.example.ui.screens.customer.CustomerStoreScreen
import com.example.ui.theme.*
import com.example.ui.viewmodel.AceViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AceTheme {
        val aceViewModel: AceViewModel = viewModel()
        val uiState by aceViewModel.uiState.collectAsStateWithLifecycle()

        var customerTab by remember { mutableStateOf(0) } // 0: Discover Store, 1: Fan Community Feed, 2: My Library

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          containerColor = AceDarkSurface,
          topBar = {
            RoleSwitcherBar(
              currentRole = uiState.currentRole,
              onRoleSelected = { aceViewModel.switchRole(it) },
              currentAuthorId = uiState.currentAuthorId,
              allWorkspaces = uiState.allAuthorWorkspaces,
              onAuthorSelected = { aceViewModel.selectAuthorWorkspace(it) },
              onOpenCart = { aceViewModel.openCart(true) },
              cartCount = uiState.customerCart.sumOf { it.quantity }
            )
          },
          bottomBar = {
            Column {
              // Audio preview bar floating above navigation
              if (uiState.currentlyPlayingSample != null) {
                AudioPreviewBar(
                  sample = uiState.currentlyPlayingSample!!,
                  onTogglePlay = { aceViewModel.toggleAudioPlayPause() },
                  onSeek = { aceViewModel.seekAudio(it) },
                  onClose = { aceViewModel.stopAudio() }
                )
              }

              // Customer Navigation Bar (only visible when in Reader/Customer mode)
              if (uiState.currentRole == UserRole.CUSTOMER) {
                NavigationBar(
                  containerColor = AceDarkCard,
                  contentColor = AceGold,
                  modifier = Modifier.testTag("customer_bottom_nav")
                ) {
                  NavigationBarItem(
                    selected = customerTab == 0,
                    onClick = { customerTab = 0 },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Storefront") },
                    label = { Text("Storefront", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = AceObsidian,
                      selectedTextColor = AceGold,
                      indicatorColor = AceGold,
                      unselectedIconColor = AceTextSecondary,
                      unselectedTextColor = AceTextSecondary
                    ),
                    modifier = Modifier.testTag("customer_nav_store")
                  )

                  NavigationBarItem(
                    selected = customerTab == 1,
                    onClick = { customerTab = 1 },
                    icon = { Icon(Icons.Default.Campaign, contentDescription = "Fan Feed") },
                    label = { Text("Fan Feed", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = AceObsidian,
                      selectedTextColor = AceGold,
                      indicatorColor = AceGold,
                      unselectedIconColor = AceTextSecondary,
                      unselectedTextColor = AceTextSecondary
                    ),
                    modifier = Modifier.testTag("customer_nav_feed")
                  )

                  NavigationBarItem(
                    selected = customerTab == 2,
                    onClick = { customerTab = 2 },
                    icon = {
                      BadgedBox(badge = {
                        if (uiState.customerLibrary.isNotEmpty()) {
                          Badge(containerColor = AceEmerald, contentColor = AceObsidian) {
                            Text("${uiState.customerLibrary.size}", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                          }
                        }
                      }) {
                        Icon(Icons.Default.LibraryBooks, contentDescription = "Library")
                      }
                    },
                    label = { Text("My Library", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = AceObsidian,
                      selectedTextColor = AceGold,
                      indicatorColor = AceGold,
                      unselectedIconColor = AceTextSecondary,
                      unselectedTextColor = AceTextSecondary
                    ),
                    modifier = Modifier.testTag("customer_nav_library")
                  )
                }
              }
            }
          }
        ) { paddingValues ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(paddingValues)
          ) {
            when (uiState.currentRole) {
              UserRole.CUSTOMER -> {
                when (customerTab) {
                  0 -> CustomerStoreScreen(uiState = uiState, viewModel = aceViewModel)
                  1 -> CustomerFanFeedScreen(uiState = uiState, viewModel = aceViewModel)
                  2 -> CustomerLibraryScreen(uiState = uiState, viewModel = aceViewModel)
                }
              }
              UserRole.AUTHOR -> {
                AuthorWorkspaceScreen(uiState = uiState, viewModel = aceViewModel)
              }
              UserRole.ADMIN -> {
                AdminWorkspaceScreen(uiState = uiState, viewModel = aceViewModel)
              }
            }

            // Write-Sound Studio Import & Publish Dialog
            if (uiState.showImportFromWriteSound) {
              WriteSoundImportDialog(
                projects = uiState.writeSoundProjects,
                onDismiss = { aceViewModel.openImportFromWriteSound(false) },
                onPublish = { projId, title, price, desc, relDate, format, status ->
                  aceViewModel.publishProjectFromWriteSound(projId, title, price, desc, relDate, format, status)
                }
              )
            }

            // New Upcoming Material Schedule Dialog
            if (uiState.showNewUpcomingReleaseDialog) {
              NewUpcomingReleaseDialog(
                onDismiss = { aceViewModel.openNewUpcomingRelease(false) },
                onSchedule = { title, sub, type, dateText, aud, sample ->
                  aceViewModel.scheduleNewRelease(title, sub, type, dateText, aud, sample)
                }
              )
            }

            // New Fan Post Dialog
            if (uiState.showNewFanPostDialog) {
              NewFanPostDialog(
                authorBooks = uiState.authorBooks,
                onDismiss = { aceViewModel.openNewFanPost(false) },
                onPost = { title, content, audioTitle, attachedBookId, tag ->
                  aceViewModel.createFanPost(title, content, audioTitle, attachedBookId, tag)
                }
              )
            }

            // Book Details Dialog
            if (uiState.selectedProductForDetail != null) {
              val prod = uiState.selectedProductForDetail!!
              val isFollowing = uiState.authorWorkspace?.authorId == prod.authorId || prod.authorName.contains("Richard")
              BookDetailDialog(
                product = prod,
                isFollowingAuthor = isFollowing,
                onDismiss = { aceViewModel.selectProductForDetail(null) },
                onToggleFollowAuthor = { aceViewModel.toggleFollowAuthor(prod.authorId) },
                onAddToCart = {
                  aceViewModel.addToCart(prod)
                  aceViewModel.selectProductForDetail(null)
                },
                onPlayAudioSample = {
                  aceViewModel.playAudioSample(
                    title = prod.title,
                    authorName = prod.authorName,
                    coverRes = prod.coverDrawableRes,
                    durationSeconds = prod.audioDurationMinutes * 60
                  )
                },
                onInstantBuy = {
                  aceViewModel.addToCart(prod)
                  aceViewModel.selectProductForDetail(null)
                  aceViewModel.checkoutCart()
                }
              )
            }

            // Comments Sheet Dialog
            if (uiState.activeCommentsPostId != null) {
              val postId = uiState.activeCommentsPostId!!
              CommentsSheet(
                postId = postId,
                comments = uiState.commentsMap[postId] ?: emptyList(),
                isAuthorRole = uiState.currentRole == UserRole.AUTHOR,
                onDismiss = { aceViewModel.closeComments() },
                onPostComment = { content -> aceViewModel.addCommentToPost(postId, content) }
              )
            }

            // Shopping Cart Sheet Dialog
            if (uiState.showCartSheet) {
              CustomerCartSheet(
                cartItems = uiState.customerCart,
                onDismiss = { aceViewModel.openCart(false) },
                onRemoveItem = { aceViewModel.removeFromCart(it) },
                onCheckout = { aceViewModel.checkoutCart() }
              )
            }
          }
        }
      }
    }
  }
}
