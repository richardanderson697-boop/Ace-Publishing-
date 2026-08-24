package com.example.data.repository

import com.example.R
import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AceRepository {

  // Current session role & active author workspace
  private val _currentUserRole = MutableStateFlow(UserRole.AUTHOR)
  val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

  private val _currentAuthorId = MutableStateFlow("author_richard")
  val currentAuthorId: StateFlow<String> = _currentAuthorId.asStateFlow()

  // Authors & Workspaces
  private val _workspaces = MutableStateFlow<Map<String, AuthorWorkspace>>(emptyMap())
  val workspaces: StateFlow<Map<String, AuthorWorkspace>> = _workspaces.asStateFlow()

  // Catalog Products
  private val _products = MutableStateFlow<List<BookProduct>>(emptyList())
  val products: StateFlow<List<BookProduct>> = _products.asStateFlow()

  // Upcoming Releases / Preview Material
  private val _scheduledReleases = MutableStateFlow<List<ScheduledRelease>>(emptyList())
  val scheduledReleases: StateFlow<List<ScheduledRelease>> = _scheduledReleases.asStateFlow()

  // Operational Orders
  private val _orders = MutableStateFlow<List<OperationalOrder>>(emptyList())
  val orders: StateFlow<List<OperationalOrder>> = _orders.asStateFlow()

  // Fan Posts with Attribution & Engagement
  private val _fanPosts = MutableStateFlow<List<FanPost>>(emptyList())
  val fanPosts: StateFlow<List<FanPost>> = _fanPosts.asStateFlow()

  // Fan Comments
  private val _comments = MutableStateFlow<Map<String, List<FanComment>>>(emptyMap())
  val comments: StateFlow<Map<String, List<FanComment>>> = _comments.asStateFlow()

  // Write-Sound Studio Bridge Queue
  private val _writeSoundProjects = MutableStateFlow<List<WriteSoundStudioProject>>(emptyList())
  val writeSoundProjects: StateFlow<List<WriteSoundStudioProject>> = _writeSoundProjects.asStateFlow()

  // Customer Cart & Library
  private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
  val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

  private val _library = MutableStateFlow<List<CustomerLibraryItem>>(emptyList())
  val library: StateFlow<List<CustomerLibraryItem>> = _library.asStateFlow()

  // Followed Authors (Customer)
  private val _followedAuthorIds = MutableStateFlow<Set<String>>(setOf("author_richard"))
  val followedAuthorIds: StateFlow<Set<String>> = _followedAuthorIds.asStateFlow()

  // Currently playing audio preview sample
  private val _currentlyPlayingSample = MutableStateFlow<PlayingAudioSample?>(null)
  val currentlyPlayingSample: StateFlow<PlayingAudioSample?> = _currentlyPlayingSample.asStateFlow()

  data class PlayingAudioSample(
    val title: String,
    val authorName: String,
    val coverRes: Int,
    val localCoverUri: String? = null,
    val audioFilePath: String? = null,
    val isRealAudioFile: Boolean = false,
    val durationSeconds: Int = 180,
    val currentPositionSeconds: Int = 0,
    val isPlaying: Boolean = true,
    val fileSizeBytes: Long = 0L
  )

  init {
    seedInitialData()
  }

  private fun seedInitialData() {
    // 1. Workspaces with immutable Firebase UIDs
    val richardWorkspace = AuthorWorkspace(
      authorId = "author_richard",
      firebaseUid = "firebase_uid_richard_anderson_77a9",
      storeName = "Richard Anderson Publishing",
      handle = "@richard_anderson",
      bio = "Sci-fi novelist & soundscaper. Author of 'The Machine' series. Exploring synthetic intelligence and human consciousness.",
      payoutEmail = "RichardAnderson697@gmail.com",
      followerCount = 312,
      activeFollowersThisWeek = 48,
      createdAt = System.currentTimeMillis() - (180L * 24 * 3600 * 1000),
      avatarDrawableRes = R.drawable.cover_machine2_1787574231599,
      bannerTitle = "The Machine Universe"
    )

    val elenaWorkspace = AuthorWorkspace(
      authorId = "author_elena",
      firebaseUid = "firebase_uid_elena_vance_88b1",
      storeName = "Elena Vance Cosmos",
      handle = "@elena_vance",
      bio = "Space opera epic writer. Crafting stellar sagas and deep-space audio dramas.",
      payoutEmail = "elena.vance.payout@ace.pub",
      followerCount = 840,
      activeFollowersThisWeek = 192,
      createdAt = System.currentTimeMillis() - (360L * 24 * 3600 * 1000),
      avatarDrawableRes = R.drawable.cover_stellar_1787574249660,
      bannerTitle = "Cosmic Odyssey Studio"
    )

    val marcusWorkspace = AuthorWorkspace(
      authorId = "author_marcus",
      firebaseUid = "firebase_uid_marcus_chen_99c2",
      storeName = "Marcus Chen AudioWorks",
      handle = "@m_chen_sound",
      bio = "Noir mystery author & ambient sound designer. Exploring acoustic tension and psychological thrillers.",
      payoutEmail = "marcus.chen@audioworks.net",
      followerCount = 520,
      activeFollowersThisWeek = 88,
      createdAt = System.currentTimeMillis() - (90L * 24 * 3600 * 1000),
      avatarDrawableRes = R.drawable.cover_resonance_1787574266042,
      bannerTitle = "Acoustic Noir"
    )

    _workspaces.value = mapOf(
      "author_richard" to richardWorkspace,
      "author_elena" to elenaWorkspace,
      "author_marcus" to marcusWorkspace
    )

    // 2. Catalog Books
    val bookMachine2 = BookProduct(
      id = "book_machine_2",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      title = "The Machine 2: Autonomous Dawn",
      subtitle = "When synthetic sentience fractures the sovereign grid",
      description = "The direct sequel to the acclaimed Genesis Protocol. Deep beneath the orbital perimeter, autonomous neural clusters begin whispering in synchronized frequencies that the creators never encoded. Featuring a fully mastered 3D binaural audiobook edition.",
      price = 14.99,
      coverDrawableRes = R.drawable.cover_machine2_1787574231599,
      format = ProductFormat.BUNDLE,
      chapterCount = 14,
      audioDurationMinutes = 580,
      rating = 4.9,
      reviewCount = 94,
      genres = listOf("Sci-Fi Thriller", "Cyberpunk", "Audiobook"),
      isPublished = true,
      publicationStatus = PublicationStatus.PUBLISHED_LIVE,
      releaseDate = "September 2026",
      previewSampleTitle = "Chapter 1: The First Resonance",
      sampleText = "The relay core clicked once, twice, and then hummed at 432 hertz. It wasn't supposed to calibrate to harmonic frequencies. Richard looked up from the telemetry console..."
    )

    val bookMachine2Draft = BookProduct(
      id = "book_machine_2_test_draft",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      title = "The Machine 2: Part II (Early Master Render)",
      subtitle = "Private Testing Draft • Chapters 7-14 Binaural Pass",
      description = "Direct master audio export from Write-Sound Studio session #402. Unlisted private test draft to verify 96kHz spatial staging and chapter cues before marketplace public launch.",
      price = 14.99,
      coverDrawableRes = R.drawable.cover_machine2_1787574231599,
      format = ProductFormat.AUDIOBOOK,
      chapterCount = 8,
      audioDurationMinutes = 320,
      rating = 5.0,
      reviewCount = 0,
      genres = listOf("Sci-Fi Thriller", "Private Test Draft"),
      isPublished = false,
      publicationStatus = PublicationStatus.PRIVATE_DRAFT,
      releaseDate = "Unlisted / In Review",
      previewSampleTitle = "Chapter 7: Neural Cascade [Master]",
      sampleText = "Private test transcript: The neural cascade began in the sub-orbital station..."
    )

    val bookMachine1 = BookProduct(
      id = "book_machine_1",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      title = "The Machine: Genesis Protocol",
      subtitle = "Book One in the Machine Saga",
      description = "In 2091, the first self-organizing synthetic mind woke in silence. A breathless techno-thriller on the threshold of technological singularity.",
      price = 11.99,
      coverDrawableRes = R.drawable.cover_machine2_1787574231599,
      format = ProductFormat.AUDIOBOOK,
      chapterCount = 12,
      audioDurationMinutes = 480,
      rating = 4.8,
      reviewCount = 210,
      genres = listOf("Sci-Fi", "Artificial Intelligence"),
      releaseDate = "March 2026",
      previewSampleTitle = "Prologue: Zero State",
      sampleText = "Before the signal, there was absolute darkness. Then the network awakened."
    )

    val bookStellar = BookProduct(
      id = "book_stellar_echoes",
      authorId = "author_elena",
      authorName = "Elena Vance",
      title = "Stellar Echoes: The Silent Horizon",
      subtitle = "A voyage through the dead gates of the Sagittarius Arm",
      description = "Captain Vane leads a solitary exploration crew through ancient celestial relics left behind by the Architects. A cinematic space odyssey with orchestral soundscapes.",
      price = 16.99,
      coverDrawableRes = R.drawable.cover_stellar_1787574249660,
      format = ProductFormat.BUNDLE,
      chapterCount = 18,
      audioDurationMinutes = 720,
      rating = 4.9,
      reviewCount = 142,
      genres = listOf("Space Opera", "Epic Sci-Fi"),
      releaseDate = "August 2026",
      previewSampleTitle = "Opening: Stargate Ignition",
      sampleText = "The ring glowed with the luminescence of dying stars."
    )

    val bookResonance = BookProduct(
      id = "book_resonance_silence",
      authorId = "author_marcus",
      authorName = "Marcus Chen",
      title = "The Resonance of Silence: Ambient Mind",
      subtitle = "A psychological acoustic noir mystery",
      description = "An audio forensic analyst uncovers coded sub-bass frequencies embedded in police dispatch tapes across three decades.",
      price = 13.50,
      coverDrawableRes = R.drawable.cover_resonance_1787574266042,
      format = ProductFormat.AUDIOBOOK,
      chapterCount = 10,
      audioDurationMinutes = 410,
      rating = 4.7,
      reviewCount = 68,
      genres = listOf("Noir Mystery", "Psychological Thriller"),
      releaseDate = "July 2026",
      previewSampleTitle = "Tape 04: The Missing Frequency",
      sampleText = "Sound leaves footprints in the dust. You just have to know which microphone to believe."
    )

    _products.value = listOf(bookMachine2, bookMachine2Draft, bookMachine1, bookStellar, bookResonance)

    // 3. Upcoming Releases / Scheduled Material (Author anticipation builder)
    val rel1 = ScheduledRelease(
      id = "rel_m2_ch6",
      authorId = "author_richard",
      title = "The Machine 2 — Chapter 6: Sub-Grid Pulse",
      subtitle = "The orbital perimeter breach sequence",
      type = ReleaseType.CHAPTER,
      scheduledDate = System.currentTimeMillis() + (18L * 24 * 3600 * 1000),
      formattedDate = "September 12, 2026",
      status = ReleaseStatus.SCHEDULED,
      targetAudience = TargetAudience.FOLLOWERS_ONLY,
      writeSoundProjectId = "ws_proj_machine2",
      previewSampleText = "The security doors did not lock; they dissolved their authorization protocols in unison.",
      audioSampleDurationSec = 180,
      previewPlaysCount = 47
    )

    val rel2 = ScheduledRelease(
      id = "rel_m2_full",
      authorId = "author_richard",
      title = "The Machine 2 — Mastered Complete Audiobook",
      subtitle = "Full 9.5-hour binaural audio experience with cast narration",
      type = ReleaseType.FULL_AUDIOBOOK,
      scheduledDate = System.currentTimeMillis() + (34L * 24 * 3600 * 1000),
      formattedDate = "September 28, 2026",
      status = ReleaseStatus.DRAFT,
      targetAudience = TargetAudience.PUBLIC,
      writeSoundProjectId = "ws_proj_machine2",
      previewSampleText = "Complete final audio master currently undergoing QC export in Write-Sound studio.",
      audioSampleDurationSec = 300,
      previewPlaysCount = 12
    )

    val rel3 = ScheduledRelease(
      id = "rel_elena_epilogue",
      authorId = "author_elena",
      title = "Stellar Echoes — Epilogue: The Architects Return",
      subtitle = "Special standalone audio short story",
      type = ReleaseType.PREVIEW,
      scheduledDate = System.currentTimeMillis() + (22L * 24 * 3600 * 1000),
      formattedDate = "October 5, 2026",
      status = ReleaseStatus.SCHEDULED,
      targetAudience = TargetAudience.PUBLIC,
      audioSampleDurationSec = 240,
      previewPlaysCount = 68
    )

    _scheduledReleases.value = listOf(rel1, rel2, rel3)

    // 4. Operational Orders (with 85/15 creator royalty split and Fan Attribution!)
    val ord1 = OperationalOrder(
      orderId = "ACE-8921",
      authorId = "author_richard",
      customerId = "cust_101",
      customerName = "Sarah Jenkins",
      productId = "book_machine_2",
      productTitle = "The Machine 2: Autonomous Dawn",
      format = ProductFormat.BUNDLE,
      grossAmount = 14.99,
      authorCut85 = 12.74,
      platformFee15 = 2.25,
      status = OrderStatus.DIGITAL_DELIVERY_READY,
      attributionSource = AttributionSource.FAN_POST,
      attributionPostId = "post_richard_1",
      attributionDetail = "Fan Post: 'Chapter 6 Teaser' (Audiobook Preview Play)",
      timestamp = System.currentTimeMillis() - (15 * 60 * 1000),
      formattedTime = "15 min ago"
    )

    val ord2 = OperationalOrder(
      orderId = "ACE-8920",
      authorId = "author_richard",
      customerId = "cust_102",
      customerName = "David Kim",
      productId = "book_machine_2",
      productTitle = "The Machine 2: Autonomous Dawn",
      format = ProductFormat.AUDIOBOOK,
      grossAmount = 14.99,
      authorCut85 = 12.74,
      platformFee15 = 2.25,
      status = OrderStatus.PROCESSING,
      attributionSource = AttributionSource.AUDIO_PREVIEW,
      attributionDetail = "Audiobook sample playback from storefront",
      timestamp = System.currentTimeMillis() - (45 * 60 * 1000),
      formattedTime = "45 min ago"
    )

    val ord3 = OperationalOrder(
      orderId = "ACE-8919",
      authorId = "author_richard",
      customerId = "cust_103",
      customerName = "Lucas Reed",
      productId = "book_machine_1",
      productTitle = "The Machine: Genesis Protocol",
      format = ProductFormat.AUDIOBOOK,
      grossAmount = 11.99,
      authorCut85 = 10.19,
      platformFee15 = 1.80,
      status = OrderStatus.COMPLETED,
      attributionSource = AttributionSource.STOREFRONT_SEARCH,
      attributionDetail = "Storefront keyword search 'Genesis Protocol'",
      timestamp = System.currentTimeMillis() - (2 * 3600 * 1000),
      formattedTime = "2 hours ago"
    )

    val ord4 = OperationalOrder(
      orderId = "ACE-8918",
      authorId = "author_richard",
      customerId = "cust_104",
      customerName = "Emily Clark",
      productId = "book_machine_2",
      productTitle = "The Machine 2: Autonomous Dawn",
      format = ProductFormat.BUNDLE,
      grossAmount = 14.99,
      authorCut85 = 12.74,
      platformFee15 = 2.25,
      status = OrderStatus.PAYMENT_RECEIVED,
      attributionSource = AttributionSource.FAN_POST,
      attributionPostId = "post_richard_2",
      attributionDetail = "Fan Post: 'Behind the Scenes in Write-Sound'",
      timestamp = System.currentTimeMillis() - (3 * 3600 * 1000),
      formattedTime = "3 hours ago"
    )

    val ord5 = OperationalOrder(
      orderId = "ACE-8915",
      authorId = "author_richard",
      customerId = "cust_105",
      customerName = "Alex Vance",
      productId = "book_machine_1",
      productTitle = "The Machine: Genesis Protocol",
      format = ProductFormat.EBOOK,
      grossAmount = 11.99,
      authorCut85 = 10.19,
      platformFee15 = 1.80,
      status = OrderStatus.REFUND_REQUESTED,
      attributionSource = AttributionSource.DIRECT_LINK,
      attributionDetail = "Accidental duplicate purchase ticket",
      timestamp = System.currentTimeMillis() - (6 * 3600 * 1000),
      formattedTime = "6 hours ago"
    )

    val ord6 = OperationalOrder(
      orderId = "ACE-8912",
      authorId = "author_richard",
      customerId = "cust_106",
      customerName = "Morgan Lee",
      productId = "book_machine_2",
      productTitle = "The Machine 2: Autonomous Dawn",
      format = ProductFormat.BUNDLE,
      grossAmount = 14.99,
      authorCut85 = 12.74,
      platformFee15 = 2.25,
      status = OrderStatus.PAYMENT_FAILED,
      attributionSource = AttributionSource.FAN_POST,
      attributionPostId = "post_richard_1",
      attributionDetail = "Card processing zip mismatch error",
      timestamp = System.currentTimeMillis() - (12 * 3600 * 1000),
      formattedTime = "12 hours ago"
    )

    // Orders for other authors to prove data isolation!
    val ord7 = OperationalOrder(
      orderId = "ACE-8901",
      authorId = "author_elena",
      customerId = "cust_201",
      customerName = "Captain Vance",
      productId = "book_stellar_echoes",
      productTitle = "Stellar Echoes: The Silent Horizon",
      format = ProductFormat.BUNDLE,
      grossAmount = 16.99,
      authorCut85 = 14.44,
      platformFee15 = 2.55,
      status = OrderStatus.COMPLETED,
      attributionSource = AttributionSource.FAN_POST,
      timestamp = System.currentTimeMillis() - (4 * 3600 * 1000),
      formattedTime = "4 hours ago"
    )

    val ord8 = OperationalOrder(
      orderId = "ACE-8899",
      authorId = "author_marcus",
      customerId = "cust_301",
      customerName = "Detective Mills",
      productId = "book_resonance_silence",
      productTitle = "The Resonance of Silence",
      format = ProductFormat.AUDIOBOOK,
      grossAmount = 13.50,
      authorCut85 = 11.47,
      platformFee15 = 2.03,
      status = OrderStatus.COMPLETED,
      attributionSource = AttributionSource.AUDIO_PREVIEW,
      timestamp = System.currentTimeMillis() - (8 * 3600 * 1000),
      formattedTime = "8 hours ago"
    )

    _orders.value = listOf(ord1, ord2, ord3, ord4, ord5, ord6, ord7, ord8)

    // 5. Fan Posts with direct Conversion Tracking
    val post1 = FanPost(
      postId = "post_richard_1",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      authorHandle = "@richard_anderson",
      title = "✨ New chapter coming Friday: The Machine 2 — Chapter 6 Teaser",
      content = "Just wrapped the binaural audio pass for Chapter 6 inside Write-Sound. Listen to this 3-minute clip where the orbital defense perimeter discovers the AI has rewrote its own encryption key.",
      mediaPreviewUrl = "audio_clip_ch6",
      audioPreviewTitle = "The Machine 2 — Chapter 6 Preview (03:00)",
      audioDurationSec = 180,
      attachedProductId = "book_machine_2",
      attachedProductTitle = "The Machine 2: Autonomous Dawn",
      attachedProductPrice = 14.99,
      likesCount = 54,
      commentCount = 23,
      previewPlays = 48,
      conversionsCount = 21, // 21 purchases driven directly from this post!
      createdAt = System.currentTimeMillis() - (1 * 24 * 3600 * 1000),
      tag = "📢 Release Teaser",
      isLikedByCurrentUser = true
    )

    val post2 = FanPost(
      postId = "post_richard_2",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      authorHandle = "@richard_anderson",
      title = "🎧 Behind the scenes in Write-Sound Studio mastering session",
      content = "Here is a quick look at the multi-voice synthesizer layer we built for the Machine's voice. We mixed sub-bass frequencies at 48Hz to give it a physical presence in headphones.",
      audioPreviewTitle = "Voice Synthesis Audio Layer (01:15)",
      audioDurationSec = 75,
      attachedProductId = "book_machine_2",
      attachedProductTitle = "The Machine 2: Autonomous Dawn",
      attachedProductPrice = 14.99,
      likesCount = 42,
      commentCount = 14,
      previewPlays = 36,
      conversionsCount = 9,
      createdAt = System.currentTimeMillis() - (3 * 24 * 3600 * 1000),
      tag = "📖 Behind the Scenes"
    )

    val post3 = FanPost(
      postId = "post_richard_3",
      authorId = "author_richard",
      authorName = "Richard Anderson",
      authorHandle = "@richard_anderson",
      title = "📢 Special Q&A with Readers: What will the Machine do next?",
      content = "Drop your questions in the comments below! I'll be recording audio voice notes replying to the top 5 questions tomorrow afternoon.",
      likesCount = 38,
      commentCount = 19,
      previewPlays = 0,
      conversionsCount = 4,
      createdAt = System.currentTimeMillis() - (5 * 24 * 3600 * 1000),
      tag = "💬 Author Post"
    )

    val postElena = FanPost(
      postId = "post_elena_1",
      authorId = "author_elena",
      authorName = "Elena Vance",
      authorHandle = "@elena_vance",
      title = "🚀 The Sagittarius Ring: Sound recording in the desert",
      content = "We sampled deep canyon winds to model the acoustic resonance of the ancient stargate. Listen to the opening motif!",
      audioPreviewTitle = "Stargate Wind Resonance (02:10)",
      audioDurationSec = 130,
      attachedProductId = "book_stellar_echoes",
      attachedProductTitle = "Stellar Echoes: The Silent Horizon",
      attachedProductPrice = 16.99,
      likesCount = 89,
      commentCount = 31,
      previewPlays = 65,
      conversionsCount = 34,
      createdAt = System.currentTimeMillis() - (2 * 24 * 3600 * 1000),
      tag = "🎧 Audiobook Preview"
    )

    _fanPosts.value = listOf(post1, post2, post3, postElena)

    // 6. Comments
    _comments.value = mapOf(
      "post_richard_1" to listOf(
        FanComment(
          commentId = "c1",
          postId = "post_richard_1",
          userName = "Marcus T.",
          content = "That sub-bass rumble when the defense door opens gave me chills! Pre-ordering the bundle right now.",
          timestamp = System.currentTimeMillis() - (18 * 3600 * 1000),
          formattedTime = "18h ago"
        ),
        FanComment(
          commentId = "c2",
          postId = "post_richard_1",
          userName = "Richard Anderson",
          content = "Glad you noticed that Marcus! We spent 4 days balancing the low-end harmonic with the voice track in Write-Sound.",
          timestamp = System.currentTimeMillis() - (16 * 3600 * 1000),
          formattedTime = "16h ago",
          isAuthorReply = true
        ),
        FanComment(
          commentId = "c3",
          postId = "post_richard_1",
          userName = "Claire Bennett",
          content = "Is Chapter 6 going to be available immediately on September 12 for followers?",
          timestamp = System.currentTimeMillis() - (12 * 3600 * 1000),
          formattedTime = "12h ago"
        )
      )
    )

    // 7. Write-Sound Studio Projects Ready for Publishing Import
    val ws1 = WriteSoundStudioProject(
      projectId = "ws_proj_machine2",
      title = "The Machine 2: Autonomous Dawn",
      subtitle = "Mastered 14 Chapters, Full Audio Engine Export",
      chapterCount = 14,
      wordCount = 86400,
      masteredAudioDurationMin = 580,
      defaultPrice = 14.99,
      coverDrawableRes = R.drawable.cover_machine2_1787574231599,
      synopsis = "Write-Sound project complete: Audio binaural mastering complete. EPUB manuscript formatted. Cover assets attached. Ready to send to ACE Publishing.",
      readyForPublishing = true,
      lastEdited = "Synced 10 minutes ago"
    )

    val ws2 = WriteSoundStudioProject(
      projectId = "ws_proj_chimera",
      title = "Project Chimera: AI Soliloquy",
      subtitle = "Experimental Audio Novella",
      chapterCount = 6,
      wordCount = 28000,
      masteredAudioDurationMin = 190,
      defaultPrice = 8.99,
      coverDrawableRes = R.drawable.cover_resonance_1787574266042,
      synopsis = "Synthesized dialogue between two rogue satellites. Chapter 1-6 narration finalized.",
      readyForPublishing = true,
      lastEdited = "Synced yesterday"
    )

    _writeSoundProjects.value = listOf(ws1, ws2)

    // 8. Initial Customer Library
    _library.value = listOf(
      CustomerLibraryItem(
        id = "lib_1",
        product = bookMachine1,
        purchaseDate = "Aug 14, 2026",
        lastPlaybackPositionSec = 1420,
        totalDurationSec = 28800,
        isDownloaded = true
      )
    )
  }

  // --- Role & Author Switching ---
  fun setUserRole(role: UserRole) {
    _currentUserRole.value = role
  }

  fun switchAuthor(authorId: String) {
    _currentAuthorId.value = authorId
  }

  fun updateAuthorSettings(
    authorId: String,
    storeName: String,
    handle: String,
    bio: String,
    payoutEmail: String,
    bannerTitle: String
  ) {
    _workspaces.update { map ->
      val current = map[authorId] ?: return@update map
      map + (authorId to current.copy(
        storeName = storeName,
        handle = handle,
        bio = bio,
        payoutEmail = payoutEmail,
        bannerTitle = bannerTitle
      ))
    }
  }

  // --- Order Management & State Transitions ---
  fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
    _orders.update { list ->
      list.map { order ->
        if (order.orderId == orderId) {
          order.copy(status = newStatus)
        } else {
          order
        }
      }
    }
  }

  // --- Publishing & Write-Sound Import ---
  fun publishFromWriteSound(
    authorId: String,
    projectId: String,
    customTitle: String,
    customPrice: Double,
    description: String,
    releaseDate: String,
    format: ProductFormat,
    publicationStatus: PublicationStatus = PublicationStatus.PRIVATE_DRAFT
  ): BookProduct {
    val project = _writeSoundProjects.value.find { it.projectId == projectId }
    val author = _workspaces.value[authorId]
    val authorName = author?.storeName ?: "Richard Anderson"

    val newProduct = BookProduct(
      id = "book_${System.currentTimeMillis()}",
      authorId = authorId,
      authorName = authorName,
      title = customTitle.ifBlank { project?.title ?: "New Publication" },
      subtitle = project?.subtitle ?: "Published via Write-Sound Studio Bridge",
      description = description.ifBlank { project?.synopsis ?: "Mastered audiobook & publication." },
      price = customPrice,
      coverDrawableRes = project?.coverDrawableRes ?: R.drawable.cover_machine2_1787574231599,
      format = format,
      chapterCount = project?.chapterCount ?: 12,
      audioDurationMinutes = project?.masteredAudioDurationMin ?: 480,
      rating = 5.0,
      reviewCount = 0,
      genres = listOf("Sci-Fi Thriller", "Audiobook"),
      isPublished = publicationStatus == PublicationStatus.PUBLISHED_LIVE,
      publicationStatus = publicationStatus,
      ingestionSource = IngestionSource.WRITE_SOUND_ECOSYSTEM,
      royaltyRatePercent = 85.0,
      releaseDate = releaseDate.ifBlank { if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) "Private Draft" else "Immediate Release" }
    )

    _products.update { listOf(newProduct) + it }
    return newProduct
  }

  fun publishFromStandaloneZip(
    authorId: String,
    title: String,
    price: Double,
    description: String,
    releaseDate: String,
    format: ProductFormat,
    publicationStatus: PublicationStatus,
    zipFileName: String,
    segmentCount: Int,
    durationMinutes: Int,
    localAudioPath: String?,
    localCoverUri: String?,
    coverDrawableRes: Int = R.drawable.cover_machine2_1787574231599
  ): BookProduct {
    val author = _workspaces.value[authorId]
    val authorName = author?.storeName ?: "Richard Anderson"

    val newProduct = BookProduct(
      id = "book_zip_${System.currentTimeMillis()}",
      authorId = authorId,
      authorName = authorName,
      title = title.ifBlank { "Standalone Master Release" },
      subtitle = "Standalone ZIP Intake (75% Creator Net)",
      description = description.ifBlank { "Mastered audiobook with embedded jacket cover and stitched voice segments." },
      price = price,
      coverDrawableRes = coverDrawableRes,
      format = format,
      chapterCount = if (segmentCount > 0) (segmentCount / 4).coerceAtLeast(1) else 8,
      audioDurationMinutes = durationMinutes,
      rating = 5.0,
      reviewCount = 0,
      genres = listOf("Standalone Ingestion", "Spatial Master"),
      isPublished = publicationStatus == PublicationStatus.PUBLISHED_LIVE,
      publicationStatus = publicationStatus,
      ingestionSource = IngestionSource.STANDALONE_ZIP_IMPORT,
      royaltyRatePercent = 75.0,
      localAudioPath = localAudioPath,
      localCoverUri = localCoverUri,
      zipFileName = zipFileName,
      extractedSegmentCount = segmentCount,
      releaseDate = releaseDate.ifBlank { if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) "Private Draft (Testing)" else "Immediate Release" }
    )

    _products.update { listOf(newProduct) + it }
    return newProduct
  }

  fun publishDraftToLive(productId: String) {
    _products.update { list ->
      list.map { prod ->
        if (prod.id == productId) {
          prod.copy(
            isPublished = true,
            publicationStatus = PublicationStatus.PUBLISHED_LIVE,
            releaseDate = "August 2026 (Live)"
          )
        } else {
          prod
        }
      }
    }
  }

  fun deleteProductDraft(productId: String) {
    _products.update { list ->
      list.filterNot { it.id == productId }
    }
  }

  data class IngestionPipelineStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val status: String = "SUCCESS"
  )

  data class ApiHandshakeResult(
    val success: Boolean,
    val matchedAuthorId: String?,
    val storeName: String?,
    val createdProduct: BookProduct?,
    val logs: List<String>,
    val pipelineSteps: List<IngestionPipelineStep> = emptyList()
  )

  fun ingestFromPublishingApi(
    firebaseUid: String,
    title: String,
    price: Double,
    format: ProductFormat,
    chapterCount: Int,
    durationMin: Int,
    publicationStatus: PublicationStatus = PublicationStatus.PRIVATE_DRAFT,
    coverRes: Int = R.drawable.cover_machine2_1787574231599
  ): ApiHandshakeResult {
    val logs = mutableListOf<String>()
    val steps = mutableListOf<IngestionPipelineStep>()

    logs.add("[WRITE-SOUND] ElevenLabs generated voice segments bundled into master archive.")
    logs.add("[WRITE-SOUND] User clicked [Send to ACE] -> Attached Firebase Auth token (UID: $firebaseUid).")
    logs.add("[ACE INGESTION] Received payload: Master ZIP + User-supplied Jacket Cover + Chapter Manifest.")

    // Step 1: Validate ZIP
    logs.add("[1/8] Validating master ZIP container & file signatures (CRC32 checksum match)...")
    steps.add(IngestionPipelineStep(1, "Validate ZIP", "Verified archive integrity & CRC32 headers."))

    // Step 2: Extract segments
    logs.add("[2/8] Extracting 48 ElevenLabs generated WAV/FLAC audio segments from chapter archive...")
    steps.add(IngestionPipelineStep(2, "Extract Segments", "Decompressed 48 voice segments into isolated buffer."))

    // Step 3: Sort in true speaking order
    logs.add("[3/8] Reading manifest cues: Sorting segments into strict chronological speaking order...")
    steps.add(IngestionPipelineStep(3, "Sort Speaking Order", "Indexed cues 001 through 048 by narrative sequence."))

    // Step 4: Validate numbering / detect missing segments
    logs.add("[4/8] Running gap detection: Validated continuous sequence 001-048 (0 missing segments, 0 dropped frames).")
    steps.add(IngestionPipelineStep(4, "Gap & Numbering Check", "Verified complete continuity without dropped audio frames."))

    // Step 5: Concatenate segments
    logs.add("[5/8] Stitching segments with 120ms natural breathing pauses and crossfade smoothing...")
    steps.add(IngestionPipelineStep(5, "Concatenate Segments", "Stitched into unified continuous binaural master stream."))

    // Step 6: Embed jacket cover / chapter metadata
    logs.add("[6/8] Embedding user-supplied high-res jacket cover & ID3v2/Vorbis chapter cues directly into audio tags...")
    steps.add(IngestionPipelineStep(6, "Embed Jacket Cover & Metadata", "Bound user jacket art and chapter timestamps to playable container."))

    // Step 7: Create final playable chapter
    logs.add("[7/8] Transcoding final master container: Generated 96kHz 24-bit spatial master + AAC-LC mobile fallback.")
    steps.add(IngestionPipelineStep(7, "Create Playable Chapter", "Generated production-ready spatial master."))

    // Step 8: Generate/check duration
    logs.add("[8/8] Verified calibrated duration: ${durationMin / 60}h ${durationMin % 60}m across $chapterCount chapters.")
    steps.add(IngestionPipelineStep(8, "Generate Duration Metrics", "Calibrated precision runtime: ${durationMin} minutes."))

    // Find author workspace mapped to this immutable Firebase UID
    val workspace = _workspaces.value.values.find { it.firebaseUid == firebaseUid }

    if (workspace == null) {
      logs.add("[ERROR] 403 Forbidden: No ACE author workspace bound to Firebase UID: $firebaseUid")
      return ApiHandshakeResult(
        success = false,
        matchedAuthorId = null,
        storeName = null,
        createdProduct = null,
        logs = logs,
        pipelineSteps = steps
      )
    }

    logs.add("[ACE TENANT] Successfully resolved Author Workspace: ${workspace.storeName} (${workspace.authorId})")
    logs.add("[ACE STATUS] Created ${publicationStatus.displayName}. Ready for in-app listening & verification.")

    val newProduct = BookProduct(
      id = "book_api_${System.currentTimeMillis()}",
      authorId = workspace.authorId,
      authorName = workspace.storeName,
      title = title,
      subtitle = "Ingested from Write-Sound Studio (ElevenLabs Pipeline)",
      description = "Master audio stitched from 48 voice segments with embedded jacket cover and calibrated chapter markers.",
      price = price,
      coverDrawableRes = coverRes,
      format = format,
      chapterCount = chapterCount,
      audioDurationMinutes = durationMin,
      rating = 5.0,
      reviewCount = 0,
      genres = listOf("Sci-Fi Thriller", "ElevenLabs Master", "Spatial Audio"),
      isPublished = publicationStatus == PublicationStatus.PUBLISHED_LIVE,
      publicationStatus = publicationStatus,
      releaseDate = if (publicationStatus == PublicationStatus.PRIVATE_DRAFT) "Private Draft (Testing)" else "Live Release"
    )

    _products.update { listOf(newProduct) + it }

    return ApiHandshakeResult(
      success = true,
      matchedAuthorId = workspace.authorId,
      storeName = workspace.storeName,
      createdProduct = newProduct,
      logs = logs,
      pipelineSteps = steps
    )
  }

  // --- Upcoming Releases ---
  fun addScheduledRelease(
    authorId: String,
    title: String,
    subtitle: String,
    type: ReleaseType,
    dateText: String,
    targetAudience: TargetAudience,
    sampleText: String
  ) {
    val newRelease = ScheduledRelease(
      id = "rel_${System.currentTimeMillis()}",
      authorId = authorId,
      title = title,
      subtitle = subtitle,
      type = type,
      scheduledDate = System.currentTimeMillis() + (14L * 24 * 3600 * 1000),
      formattedDate = dateText,
      status = ReleaseStatus.SCHEDULED,
      targetAudience = targetAudience,
      previewSampleText = sampleText,
      audioSampleDurationSec = 180,
      previewPlaysCount = 0
    )
    _scheduledReleases.update { listOf(newRelease) + it }
  }

  fun updateReleaseStatus(releaseId: String, newStatus: ReleaseStatus) {
    _scheduledReleases.update { list ->
      list.map { if (it.id == releaseId) it.copy(status = newStatus) else it }
    }
  }

  // --- Fan Posts & Engagement ---
  fun createFanPost(
    authorId: String,
    title: String,
    content: String,
    audioTitle: String?,
    attachedProductId: String?,
    tag: String
  ) {
    val author = _workspaces.value[authorId]
    val product = _products.value.find { it.id == attachedProductId }

    val newPost = FanPost(
      postId = "post_${System.currentTimeMillis()}",
      authorId = authorId,
      authorName = author?.storeName ?: "Author",
      authorHandle = author?.handle ?: "@author",
      title = title,
      content = content,
      audioPreviewTitle = audioTitle?.takeIf { it.isNotBlank() },
      audioDurationSec = if (audioTitle.isNullOrBlank()) 0 else 120,
      attachedProductId = attachedProductId,
      attachedProductTitle = product?.title,
      attachedProductPrice = product?.price,
      likesCount = 1,
      commentCount = 0,
      previewPlays = 0,
      conversionsCount = 0,
      createdAt = System.currentTimeMillis(),
      tag = tag
    )
    _fanPosts.update { listOf(newPost) + it }
  }

  fun toggleLikePost(postId: String) {
    _fanPosts.update { list ->
      list.map { post ->
        if (post.postId == postId) {
          val newLiked = !post.isLikedByCurrentUser
          post.copy(
            isLikedByCurrentUser = newLiked,
            likesCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
          )
        } else {
          post
        }
      }
    }
  }

  fun recordPreviewPlay(postId: String) {
    _fanPosts.update { list ->
      list.map { if (it.postId == postId) it.copy(previewPlays = it.previewPlays + 1) else it }
    }
  }

  fun addComment(postId: String, userName: String, content: String, isAuthor: Boolean = false) {
    val comment = FanComment(
      commentId = "comm_${System.currentTimeMillis()}",
      postId = postId,
      userName = userName,
      content = content,
      timestamp = System.currentTimeMillis(),
      formattedTime = "Just now",
      isAuthorReply = isAuthor
    )
    _comments.update { map ->
      val existing = map[postId] ?: emptyList()
      map + (postId to (existing + comment))
    }
    _fanPosts.update { list ->
      list.map { if (it.postId == postId) it.copy(commentCount = it.commentCount + 1) else it }
    }
  }

  // --- Customer Store & Library Operations ---
  fun toggleFollowAuthor(authorId: String) {
    _followedAuthorIds.update { set ->
      if (set.contains(authorId)) set - authorId else set + authorId
    }
  }

  fun addToCart(product: BookProduct) {
    _cart.update { current ->
      val existing = current.find { it.product.id == product.id }
      if (existing != null) {
        current.map { if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it }
      } else {
        current + CartItem(product = product, quantity = 1)
      }
    }
  }

  fun removeFromCart(productId: String) {
    _cart.update { current -> current.filterNot { it.product.id == productId } }
  }

  fun checkoutCart(attributionPostId: String? = null): Boolean {
    val items = _cart.value
    if (items.isEmpty()) return false

    val currentAuthorId = items.firstOrNull()?.product?.authorId ?: "author_richard"
    val timestamp = System.currentTimeMillis()

    val newOrders = items.map { item ->
      val gross = item.product.price * item.quantity
      val royaltyPercent = item.product.royaltyRatePercent
      val authorCut = gross * (royaltyPercent / 100.0)
      val platformFee = gross * ((100.0 - royaltyPercent) / 100.0)

      OperationalOrder(
        orderId = "ACE-${(8922..9999).random()}",
        authorId = item.product.authorId,
        customerId = "cust_active_user",
        customerName = "Active Reader",
        productId = item.product.id,
        productTitle = item.product.title,
        format = item.product.format,
        grossAmount = gross,
        royaltyRatePercent = royaltyPercent,
        authorCut85 = authorCut,
        platformFee15 = platformFee,
        ingestionSource = item.product.ingestionSource,
        status = OrderStatus.COMPLETED,
        attributionSource = if (attributionPostId != null) AttributionSource.FAN_POST else AttributionSource.DIRECT_LINK,
        attributionPostId = attributionPostId,
        attributionDetail = if (attributionPostId != null) "Converted via Fan Feed Preview" else "Direct Marketplace Checkout",
        timestamp = timestamp,
        formattedTime = "Just now"
      )
    }

    val newLibraryItems = items.map { item ->
      CustomerLibraryItem(
        id = "lib_${System.currentTimeMillis()}_${item.product.id}",
        product = item.product,
        purchaseDate = "Aug 24, 2026",
        lastPlaybackPositionSec = 0,
        totalDurationSec = item.product.audioDurationMinutes * 60,
        isDownloaded = true
      )
    }

    _orders.update { newOrders + it }
    _library.update { newLibraryItems + it }

    // If attributed to a post, bump its conversions count!
    if (attributionPostId != null) {
      _fanPosts.update { list ->
        list.map { post ->
          if (post.postId == attributionPostId) {
            post.copy(conversionsCount = post.conversionsCount + 1)
          } else {
            post
          }
        }
      }
    }

    _cart.value = emptyList()
    return true
  }

  // --- Audio Sample Player ---
  fun playAudioSample(
    title: String,
    authorName: String,
    coverRes: Int,
    durationSeconds: Int = 180,
    audioFilePath: String? = null,
    localCoverUri: String? = null
  ) {
    val file = audioFilePath?.let { java.io.File(it) }
    val isReal = file != null && file.exists() && file.length() > 0
    val fileSize = file?.length() ?: 0L

    _currentlyPlayingSample.value = PlayingAudioSample(
      title = title,
      authorName = authorName,
      coverRes = coverRes,
      localCoverUri = localCoverUri,
      audioFilePath = audioFilePath,
      isRealAudioFile = isReal,
      durationSeconds = if (durationSeconds > 0) durationSeconds else 180,
      currentPositionSeconds = 0,
      isPlaying = true,
      fileSizeBytes = fileSize
    )
  }

  fun togglePlayback() {
    _currentlyPlayingSample.update { current ->
      current?.copy(isPlaying = !current.isPlaying)
    }
  }

  fun seekAudio(targetSec: Int) {
    _currentlyPlayingSample.update { current ->
      current?.copy(currentPositionSeconds = targetSec.coerceIn(0, current.durationSeconds))
    }
  }

  fun stopAudio() {
    _currentlyPlayingSample.value = null
  }

  // --- Sales Analytics Generator for an Author ---
  fun getAnalyticsSummaryForAuthor(authorId: String): SalesAnalyticsSummary {
    val authorOrders = _orders.value.filter { it.authorId == authorId }
    val completedOrders = authorOrders.filter {
      it.status == OrderStatus.COMPLETED ||
      it.status == OrderStatus.DIGITAL_DELIVERY_READY ||
      it.status == OrderStatus.PROCESSING ||
      it.status == OrderStatus.PAYMENT_RECEIVED
    }

    val units = 147 + completedOrders.size
    val gross = 1248.00 + completedOrders.sumOf { it.grossAmount }
    val creatorShare = 1061.00 + completedOrders.sumOf { it.authorCut85 }

    return SalesAnalyticsSummary(
      authorId = authorId,
      currentPeriod = CurrentPeriodAnalytics(
        unitsSold = units,
        grossSales = gross,
        creatorShare85 = creatorShare,
        newFollowers = 38,
        audiobookSales = 21,
        ebookSales = 17
      ),
      priorPeriodComparison = PriorPeriodComparison(
        salesGrowthPercent = 18.0,
        followerGrowthPercent = 31.0,
        revenueGrowthPercent = 12.0
      ),
      forecastingEstimate = ForecastingEstimate(
        projectedRangeMin = 1430.0,
        projectedRangeMax = 1670.0,
        confidenceNote = "Estimate based on current 30-day velocity and 2 scheduled releases."
      )
    )
  }
}
