package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.audio.AudioPlayerManager
import com.example.data.models.*
import com.example.data.repository.AceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AceUiState(
  val currentRole: UserRole = UserRole.AUTHOR,
  val currentAuthorId: String = "author_richard",
  val authorWorkspace: AuthorWorkspace? = null,
  val allAuthorWorkspaces: Map<String, AuthorWorkspace> = emptyMap(),
  val authorBooks: List<BookProduct> = emptyList(),
  val marketplaceProducts: List<BookProduct> = emptyList(),
  val scheduledReleases: List<ScheduledRelease> = emptyList(),
  val operationalOrders: List<OperationalOrder> = emptyList(),
  val fanPosts: List<FanPost> = emptyList(),
  val writeSoundProjects: List<WriteSoundStudioProject> = emptyList(),
  val customerCart: List<CartItem> = emptyList(),
  val customerLibrary: List<CustomerLibraryItem> = emptyList(),
  val followedAuthorIds: Set<String> = emptySet(),
  val currentlyPlayingSample: AceRepository.PlayingAudioSample? = null,
  val selectedOrderFilter: OrderStatus? = null,
  val selectedProductForDetail: BookProduct? = null,
  val showImportFromWriteSound: Boolean = false,
  val showNewUpcomingReleaseDialog: Boolean = false,
  val showNewFanPostDialog: Boolean = false,
  val showCartSheet: Boolean = false,
  val isLibraryPlayerOpen: Boolean = false,
  val activeLibraryItem: CustomerLibraryItem? = null,
  val commentsMap: Map<String, List<FanComment>> = emptyMap(),
  val activeCommentsPostId: String? = null,
  val authorNavigationTab: AuthorTab = AuthorTab.DASHBOARD,
  val customerNavigationTab: CustomerTab = CustomerTab.DISCOVER,
  val adminNavigationTab: AdminTab = AdminTab.OVERVIEW,
  val isApiHandshakeInProgress: Boolean = false,
  val apiHandshakeLogs: List<String> = emptyList(),
  val apiPipelineSteps: List<com.example.data.repository.AceRepository.IngestionPipelineStep> = emptyList(),
  val isZipProcessingInProgress: Boolean = false,
  val zipProcessingLogs: List<String> = emptyList(),
  val lastZipReport: com.example.data.util.ZipProcessingReport? = null,
  val snackbarMessage: String? = null
)

enum class AuthorTab(val label: String) {
  DASHBOARD("Dashboard"),
  BOOKS("Published Books"),
  UPCOMING("New Material"),
  ORDERS("Orders"),
  ANALYTICS("Analytics & Forecast"),
  FANS("Fan Community"),
  SETTINGS("Store Settings")
}

enum class CustomerTab(val label: String) {
  DISCOVER("Discover"),
  FAN_FEED("Fan Feed"),
  LIBRARY("My Library"),
  CART("Cart")
}

enum class AdminTab(val label: String) {
  OVERVIEW("Platform Overview"),
  AUTHORS("All Authors"),
  ORDERS("Global Orders"),
  APPROVALS("Approvals Queue"),
  SYSTEM("System Health")
}

class AceViewModel(
  application: Application,
  private val repository: AceRepository = AceRepository()
) : AndroidViewModel(application) {

  private val _uiState = MutableStateFlow(AceUiState())
  val uiState: StateFlow<AceUiState> = _uiState.asStateFlow()

  private val audioPlayerManager = AudioPlayerManager(application.applicationContext)

  init {
    // Collect role
    viewModelScope.launch {
      repository.currentUserRole.collect { role ->
        _uiState.update { it.copy(currentRole = role) }
      }
    }

    // Collect workspaces & current author
    viewModelScope.launch {
      combine(repository.currentAuthorId, repository.workspaces) { authorId, workspaces ->
        authorId to workspaces
      }.collect { (authorId, workspaces) ->
        val currentWs = workspaces[authorId]
        _uiState.update { state ->
          state.copy(
            currentAuthorId = authorId,
            authorWorkspace = currentWs,
            allAuthorWorkspaces = workspaces
          )
        }
      }
    }

    // Collect products
    viewModelScope.launch {
      repository.products.collect { prods ->
        _uiState.update { state ->
          state.copy(
            marketplaceProducts = prods.filter { it.publicationStatus == PublicationStatus.PUBLISHED_LIVE },
            authorBooks = prods.filter { it.authorId == state.currentAuthorId }
          )
        }
      }
    }

    // Collect scheduled releases
    viewModelScope.launch {
      repository.scheduledReleases.collect { rels ->
        _uiState.update { state ->
          state.copy(scheduledReleases = rels.filter { it.authorId == state.currentAuthorId })
        }
      }
    }

    // Collect orders
    viewModelScope.launch {
      repository.orders.collect { ords ->
        _uiState.update { state ->
          state.copy(operationalOrders = ords.filter { it.authorId == state.currentAuthorId })
        }
      }
    }

    // Collect fan posts
    viewModelScope.launch {
      repository.fanPosts.collect { posts ->
        _uiState.update { it.copy(fanPosts = posts) }
      }
    }

    // Collect Write-Sound projects
    viewModelScope.launch {
      repository.writeSoundProjects.collect { wsList ->
        _uiState.update { it.copy(writeSoundProjects = wsList) }
      }
    }

    // Collect cart
    viewModelScope.launch {
      repository.cart.collect { cartList ->
        _uiState.update { it.copy(customerCart = cartList) }
      }
    }

    // Collect library
    viewModelScope.launch {
      repository.library.collect { libList ->
        _uiState.update { it.copy(customerLibrary = libList) }
      }
    }

    // Collect playing audio sample from real AudioPlayerManager
    viewModelScope.launch {
      audioPlayerManager.playingSample.collect { sample ->
        _uiState.update { it.copy(currentlyPlayingSample = sample) }
      }
    }

    // Collect comments
    viewModelScope.launch {
      repository.comments.collect { map ->
        _uiState.update { it.copy(commentsMap = map) }
      }
    }

    // Collect followed authors
    viewModelScope.launch {
      repository.followedAuthorIds.collect { set ->
        _uiState.update { it.copy(followedAuthorIds = set) }
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    audioPlayerManager.stop()
  }

  // --- Role & Navigation Control ---
  fun switchRole(role: UserRole) {
    repository.setUserRole(role)
  }

  fun setRole(role: UserRole) {
    repository.setUserRole(role)
  }

  fun selectAuthorWorkspace(authorId: String) {
    repository.switchAuthor(authorId)
    // Also re-filter author books, releases, orders for the new active workspace
    val prods = repository.products.value.filter { it.authorId == authorId }
    val rels = repository.scheduledReleases.value.filter { it.authorId == authorId }
    val ords = repository.orders.value.filter { it.authorId == authorId }
    val ws = repository.workspaces.value[authorId]

    _uiState.update {
      it.copy(
        currentAuthorId = authorId,
        authorWorkspace = ws,
        authorBooks = prods,
        scheduledReleases = rels,
        operationalOrders = ords
      )
    }
  }

  fun switchAuthor(authorId: String) {
    selectAuthorWorkspace(authorId)
  }

  fun setAuthorTab(tab: AuthorTab) {
    _uiState.update { it.copy(authorNavigationTab = tab) }
  }

  fun setCustomerTab(tab: CustomerTab) {
    _uiState.update { it.copy(customerNavigationTab = tab) }
  }

  fun setAdminTab(tab: AdminTab) {
    _uiState.update { it.copy(adminNavigationTab = tab) }
  }

  fun selectOrderFilter(status: OrderStatus?) {
    _uiState.update { it.copy(selectedOrderFilter = status) }
  }

  // --- Order State Transitions ---
  fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    repository.updateOrderStatus(orderId, newStatus)
  }

  // --- Write-Sound Studio Bridge ---
  fun openImportFromWriteSound(isOpen: Boolean) {
    _uiState.update { it.copy(showImportFromWriteSound = isOpen) }
  }

  fun publishProjectFromWriteSound(
    projectId: String,
    title: String,
    price: Double,
    description: String,
    releaseDate: String,
    format: ProductFormat,
    publicationStatus: PublicationStatus = PublicationStatus.PRIVATE_DRAFT
  ) {
    val currentAuthor = _uiState.value.currentAuthorId
    repository.publishFromWriteSound(
      authorId = currentAuthor,
      projectId = projectId,
      customTitle = title,
      customPrice = price,
      description = description,
      releaseDate = releaseDate,
      format = format,
      publicationStatus = publicationStatus
    )
    _uiState.update { it.copy(showImportFromWriteSound = false) }
  }

  fun publishDraftToLive(productId: String) {
    repository.publishDraftToLive(productId)
  }

  fun deleteProductDraft(productId: String) {
    repository.deleteProductDraft(productId)
  }

  fun processAndPublishStandaloneZip(
    context: android.content.Context,
    zipUri: android.net.Uri?,
    coverUri: android.net.Uri?,
    title: String,
    price: Double,
    description: String,
    releaseDate: String,
    format: ProductFormat,
    publicationStatus: PublicationStatus,
    zipFileName: String,
    customChapterCount: Int? = null
  ) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isZipProcessingInProgress = true,
          zipProcessingLogs = listOf("Initiating standalone audio & photo intake pipeline..."),
          lastZipReport = null
        )
      }

      val packager = com.example.data.util.ZipAudioPackager(context)
      val authorId = _uiState.value.currentAuthorId
      val author = _uiState.value.authorWorkspace?.storeName ?: "Richard Anderson"

      val report = packager.processZipAndCreateMaster(
        zipUri = zipUri,
        coverUri = coverUri,
        bookTitle = title,
        authorName = author,
        customZipName = zipFileName,
        customChapterCount = customChapterCount
      )

      if (report.success) {
        val createdProduct = repository.publishFromStandaloneZip(
          authorId = authorId,
          title = title,
          price = price,
          description = description,
          releaseDate = releaseDate,
          format = format,
          publicationStatus = publicationStatus,
          zipFileName = report.zipFileName,
          segmentCount = report.totalSegmentsFound,
          durationMinutes = report.totalEstimatedDurationMinutes,
          localAudioPath = report.extractedAudioPath,
          localCoverUri = report.embeddedCoverPath,
          chapterCount = customChapterCount ?: report.detectedChapterCount
        )

        _uiState.update {
          it.copy(
            isZipProcessingInProgress = false,
            zipProcessingLogs = report.stepLogs,
            lastZipReport = report,
            showImportFromWriteSound = false,
            snackbarMessage = "Successfully created ${if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) "private draft" else "live release"} for '${createdProduct.title}'"
          )
        }
      } else {
        _uiState.update {
          it.copy(
            isZipProcessingInProgress = false,
            zipProcessingLogs = report.stepLogs,
            lastZipReport = report,
            snackbarMessage = "ZIP processing error: ${report.errorMessage}"
          )
        }
      }
    }
  }

  fun clearZipProcessingLogs() {
    _uiState.update { it.copy(zipProcessingLogs = emptyList(), isZipProcessingInProgress = false) }
  }

  fun runApiHandshakeSimulation(
    firebaseUid: String,
    title: String,
    price: Double,
    format: ProductFormat
  ) {
    viewModelScope.launch {
      _uiState.update {
        it.copy(
          isApiHandshakeInProgress = true,
          apiPipelineSteps = emptyList(),
          apiHandshakeLogs = listOf("Initiating Write-Sound [Send to ACE] export...")
        )
      }
      delay(300)
      _uiState.update { it.copy(apiHandshakeLogs = it.apiHandshakeLogs + "Packaging ElevenLabs audio segments (001-048) + Jacket Cover into master ZIP archive...") }
      delay(350)
      _uiState.update { it.copy(apiHandshakeLogs = it.apiHandshakeLogs + "Attached Firebase Auth token (UID: $firebaseUid)") }
      delay(350)
      _uiState.update { it.copy(apiHandshakeLogs = it.apiHandshakeLogs + "POST https://api.ace-audio.com/v1/publish-handshake") }
      delay(400)

      val result = repository.ingestFromPublishingApi(
        firebaseUid = firebaseUid,
        title = title,
        price = price,
        format = format,
        chapterCount = 14,
        durationMin = 540,
        publicationStatus = PublicationStatus.PRIVATE_DRAFT,
        coverRes = com.example.R.drawable.cover_machine2_1787574231599
      )

      _uiState.update {
        it.copy(
          isApiHandshakeInProgress = false,
          apiHandshakeLogs = result.logs,
          apiPipelineSteps = result.pipelineSteps,
          snackbarMessage = "Created Private Draft for '${title}'"
        )
      }
    }
  }

  fun clearApiHandshakeLogs() {
    _uiState.update { it.copy(apiHandshakeLogs = emptyList(), apiPipelineSteps = emptyList(), isApiHandshakeInProgress = false) }
  }

  // --- Upcoming Material / Anticipation builder ---
  fun openNewUpcomingRelease(isOpen: Boolean) {
    _uiState.update { it.copy(showNewUpcomingReleaseDialog = isOpen) }
  }

  fun scheduleNewRelease(
    title: String,
    subtitle: String,
    type: ReleaseType,
    dateText: String,
    targetAudience: TargetAudience,
    sampleText: String
  ) {
    repository.addScheduledRelease(
      authorId = _uiState.value.currentAuthorId,
      title = title,
      subtitle = subtitle,
      type = type,
      dateText = dateText,
      targetAudience = targetAudience,
      sampleText = sampleText
    )
    _uiState.update { it.copy(showNewUpcomingReleaseDialog = false) }
  }

  fun updateReleaseStatus(releaseId: String, newStatus: ReleaseStatus) {
    repository.updateReleaseStatus(releaseId, newStatus)
  }

  // --- Fan Post & Attribution ---
  fun openNewFanPost(isOpen: Boolean) {
    _uiState.update { it.copy(showNewFanPostDialog = isOpen) }
  }

  fun createFanPost(
    title: String,
    content: String,
    audioTitle: String?,
    attachedProductId: String?,
    tag: String
  ) {
    repository.createFanPost(
      authorId = _uiState.value.currentAuthorId,
      title = title,
      content = content,
      audioTitle = audioTitle,
      attachedProductId = attachedProductId,
      tag = tag
    )
    _uiState.update { it.copy(showNewFanPostDialog = false) }
  }

  fun toggleLikePost(postId: String) {
    repository.toggleLikePost(postId)
  }

  fun openComments(postId: String?) {
    _uiState.update { it.copy(activeCommentsPostId = postId) }
  }

  fun closeComments() {
    _uiState.update { it.copy(activeCommentsPostId = null) }
  }

  fun addCommentToPost(postId: String, text: String) {
    if (text.isBlank()) return
    val isAuthor = _uiState.value.currentRole == UserRole.AUTHOR
    val userName = if (isAuthor) {
      _uiState.value.authorWorkspace?.storeName ?: "Author"
    } else {
      "Reader"
    }
    repository.addComment(postId, userName, text, isAuthor)
  }

  // --- Storefront & Cart ---
  fun selectProductForDetail(product: BookProduct?) {
    _uiState.update { it.copy(selectedProductForDetail = product) }
  }

  fun toggleFollowAuthor(authorId: String) {
    repository.toggleFollowAuthor(authorId)
  }

  fun addToCart(product: BookProduct) {
    repository.addToCart(product)
  }

  fun removeFromCart(productId: String) {
    repository.removeFromCart(productId)
  }

  fun openCart(isOpen: Boolean) {
    _uiState.update { it.copy(showCartSheet = isOpen) }
  }

  fun checkoutCart(attributionPostId: String? = null) {
    val success = repository.checkoutCart(attributionPostId)
    if (success) {
      _uiState.update { it.copy(showCartSheet = false) }
    }
  }

  // --- Audio Sample Player Controls ---
  fun playAudioSample(
    title: String,
    authorName: String,
    coverRes: Int,
    durationSeconds: Int = 180,
    audioFilePath: String? = null,
    localCoverUri: String? = null,
    postId: String? = null
  ) {
    audioPlayerManager.play(
      title = title,
      authorName = authorName,
      coverRes = coverRes,
      durationSeconds = durationSeconds,
      audioFilePath = audioFilePath,
      localCoverUri = localCoverUri
    )
    if (postId != null) {
      repository.recordPreviewPlay(postId)
    }
  }

  fun toggleAudioPlayPause() {
    audioPlayerManager.togglePlayPause()
  }

  fun togglePlayback() {
    audioPlayerManager.togglePlayPause()
  }

  fun seekAudio(sec: Int) {
    audioPlayerManager.seekTo(sec)
  }

  fun stopAudio() {
    audioPlayerManager.stop()
  }

  // --- Author Settings ---
  fun updateAuthorSettings(
    storeName: String,
    handle: String,
    bio: String,
    payoutEmail: String,
    bannerTitle: String
  ) {
    repository.updateAuthorSettings(
      authorId = _uiState.value.currentAuthorId,
      storeName = storeName,
      handle = handle,
      bio = bio,
      payoutEmail = payoutEmail,
      bannerTitle = bannerTitle
    )
  }

  fun getAnalyticsSummary(): SalesAnalyticsSummary {
    return repository.getAnalyticsSummaryForAuthor(_uiState.value.currentAuthorId)
  }
}
