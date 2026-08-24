package com.example.data.models

enum class UserRole {
  CUSTOMER,
  AUTHOR,
  ADMIN
}

enum class OrderStatus(val displayName: String) {
  PAYMENT_RECEIVED("Payment Received"),
  PROCESSING("Processing"),
  DIGITAL_DELIVERY_READY("Digital Delivery Ready"),
  COMPLETED("Delivered & Downloaded"),
  PAYMENT_FAILED("Payment Issue"),
  REFUND_REQUESTED("Refund Requested")
}

enum class ReleaseType(val displayName: String) {
  CHAPTER("Chapter"),
  FULL_AUDIOBOOK("Full Audiobook"),
  PREVIEW("Audio Preview")
}

enum class ReleaseStatus(val displayName: String) {
  DRAFT("Draft"),
  SCHEDULED("Scheduled"),
  PUBLISHED("Published")
}

enum class TargetAudience(val displayName: String) {
  PUBLIC("Public Marketplace"),
  FOLLOWERS_ONLY("Followers Only")
}

enum class AttributionSource(val displayName: String) {
  FAN_POST("Fan Post"),
  AUDIO_PREVIEW("Audiobook Preview"),
  STOREFRONT_SEARCH("Storefront Search"),
  DIRECT_LINK("Direct Author Link")
}

enum class IngestionSource(val displayName: String, val defaultRoyaltyPercent: Double, val feeExplanation: String) {
  WRITE_SOUND_ECOSYSTEM("Write-Sound Ecosystem", 85.0, "15% platform fee • 0% intake fee"),
  STANDALONE_ZIP_IMPORT("Standalone ZIP Intake", 75.0, "15% platform fee + 10% transcoding/ingestion fee")
}

enum class ProductFormat(val displayName: String) {
  AUDIOBOOK("Mastered Audiobook"),
  EBOOK("EPUB / Manuscript"),
  BUNDLE("Audiobook + E-Book Bundle")
}

enum class PublicationStatus(val displayName: String) {
  PRIVATE_DRAFT("Private Test Draft"),
  PUBLISHED_LIVE("Published Live"),
  SCHEDULED_PREORDER("Pre-Order Scheduled")
}

data class AuthorWorkspace(
  val authorId: String,
  val firebaseUid: String,
  val storeName: String,
  val handle: String,
  val bio: String,
  val payoutEmail: String,
  val followerCount: Int,
  val activeFollowersThisWeek: Int,
  val createdAt: Long,
  val avatarDrawableRes: Int? = null,
  val bannerTitle: String = "Author Studio"
)

data class BookProduct(
  val id: String,
  val authorId: String,
  val authorName: String,
  val title: String,
  val subtitle: String,
  val description: String,
  val price: Double,
  val coverDrawableRes: Int,
  val format: ProductFormat,
  val chapterCount: Int,
  val audioDurationMinutes: Int,
  val rating: Double,
  val reviewCount: Int,
  val genres: List<String>,
  val isPublished: Boolean = true,
  val publicationStatus: PublicationStatus = PublicationStatus.PUBLISHED_LIVE,
  val ingestionSource: IngestionSource = IngestionSource.WRITE_SOUND_ECOSYSTEM,
  val royaltyRatePercent: Double = 85.0,
  val localAudioPath: String? = null,
  val localCoverUri: String? = null,
  val zipFileName: String? = null,
  val extractedSegmentCount: Int = 0,
  val releaseDate: String = "Aug 2026",
  val previewSampleTitle: String = "Sample Clip",
  val sampleText: String = ""
)

data class ScheduledRelease(
  val id: String,
  val authorId: String,
  val title: String,
  val subtitle: String = "",
  val type: ReleaseType,
  val scheduledDate: Long, // timestamp
  val formattedDate: String,
  val status: ReleaseStatus,
  val targetAudience: TargetAudience,
  val writeSoundProjectId: String? = null,
  val previewSampleText: String = "",
  val audioSampleDurationSec: Int = 180,
  val previewPlaysCount: Int = 0
)

data class OperationalOrder(
  val orderId: String,
  val authorId: String,
  val customerId: String,
  val customerName: String,
  val productId: String,
  val productTitle: String,
  val format: ProductFormat,
  val grossAmount: Double,
  val royaltyRatePercent: Double = 85.0,
  val authorCut85: Double,
  val platformFee15: Double,
  val ingestionSource: IngestionSource = IngestionSource.WRITE_SOUND_ECOSYSTEM,
  val status: OrderStatus,
  val attributionSource: AttributionSource? = null,
  val attributionPostId: String? = null,
  val attributionDetail: String? = null,
  val timestamp: Long,
  val formattedTime: String
)

data class CurrentPeriodAnalytics(
  val unitsSold: Int,
  val grossSales: Double,
  val creatorShare85: Double,
  val newFollowers: Int,
  val audiobookSales: Int,
  val ebookSales: Int
)

data class PriorPeriodComparison(
  val salesGrowthPercent: Double,
  val followerGrowthPercent: Double,
  val revenueGrowthPercent: Double
)

data class ForecastingEstimate(
  val projectedRangeMin: Double,
  val projectedRangeMax: Double,
  val confidenceNote: String
)

data class SalesAnalyticsSummary(
  val authorId: String,
  val currentPeriod: CurrentPeriodAnalytics,
  val priorPeriodComparison: PriorPeriodComparison,
  val forecastingEstimate: ForecastingEstimate
)

data class FanPost(
  val postId: String,
  val authorId: String,
  val authorName: String,
  val authorHandle: String,
  val title: String,
  val content: String,
  val mediaPreviewUrl: String? = null,
  val audioPreviewTitle: String? = null,
  val audioDurationSec: Int = 0,
  val attachedProductId: String? = null,
  val attachedProductTitle: String? = null,
  val attachedProductPrice: Double? = null,
  val likesCount: Int,
  val commentCount: Int,
  val previewPlays: Int,
  val conversionsCount: Int, // Direct purchases linked to this fan post!
  val createdAt: Long,
  val tag: String = "Behind The Scenes",
  val isLikedByCurrentUser: Boolean = false
)

data class FanComment(
  val commentId: String,
  val postId: String,
  val userName: String,
  val content: String,
  val timestamp: Long,
  val formattedTime: String,
  val isAuthorReply: Boolean = false
)

data class WriteSoundStudioProject(
  val projectId: String,
  val title: String,
  val subtitle: String,
  val chapterCount: Int,
  val wordCount: Int,
  val masteredAudioDurationMin: Int,
  val defaultPrice: Double,
  val coverDrawableRes: Int,
  val synopsis: String,
  val readyForPublishing: Boolean = true,
  val lastEdited: String = "2 hours ago"
)

data class CartItem(
  val product: BookProduct,
  val quantity: Int = 1
)

data class CustomerLibraryItem(
  val id: String,
  val product: BookProduct,
  val purchaseDate: String,
  val downloadProgress: Float = 1.0f,
  val lastPlaybackPositionSec: Int = 340,
  val totalDurationSec: Int = 14400,
  val isDownloaded: Boolean = true
)
