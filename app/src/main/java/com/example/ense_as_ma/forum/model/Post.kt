package com.example.ense_as_ma.forum.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

/**
 * Modelo que representa una publicación en el foro.
 * Incluye toda la información necesaria para mostrar y gestionar un post.
 */
data class Post(
    @DocumentId
    val postId: String = "",

    // Información del autor
    val userId: String = "",
    val userName: String = "",
    val userImage: String = "",

    // Contenido del post
    val title: String = "",
    val content: String = "",
    val attachments: List<Attachment> = emptyList(),
    val tags: List<String> = emptyList(),
    val categoryId: String = "",

    // Metadatos
    @ServerTimestamp
    val createdAt: Timestamp = Timestamp.now(),
    @ServerTimestamp
    val updatedAt: Timestamp = Timestamp.now(),
    val lastActivity: Timestamp = Timestamp.now(),

    // Estadísticas
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val shareCount: Int = 0,

    // Estado y configuración
    val status: PostStatus = PostStatus.ACTIVE,
    val visibility: PostVisibility = PostVisibility.PUBLIC,
    val allowComments: Boolean = true,

    // Metadatos adicionales
    val editHistory: List<EditRecord> = emptyList(),
    val reports: List<Report> = emptyList(),
    val pinnedUntil: Timestamp? = null,

    // Campos para eventos (si el post es un evento)
    val isEvent: Boolean = false,
    val eventDetails: EventDetails? = null
)

/**
 * Representa un archivo adjunto en un post
 */
data class Attachment(
    val id: String = "",
    val type: AttachmentType = AttachmentType.IMAGE,
    val url: String = "",
    val name: String = "",
    val size: Long = 0,
    val mimeType: String = "",
    @ServerTimestamp
    val uploadedAt: Timestamp = Timestamp.now()
)

/**
 * Registro de ediciones del post
 */
data class EditRecord(
    val editedBy: String = "",
    val previousContent: String = "",
    val reason: String = "",
    @ServerTimestamp
    val editedAt: Timestamp = Timestamp.now()
)

/**
 * Registro de reportes del post
 */
data class Report(
    val reportedBy: String = "",
    val reason: String = "",
    val description: String = "",
    @ServerTimestamp
    val reportedAt: Timestamp = Timestamp.now(),
    val status: ReportStatus = ReportStatus.PENDING
)

/**
 * Detalles específicos para posts que son eventos
 */
data class EventDetails(
    val location: String = "",
    val locationDetails: String = "",
    val coordinates: GeoPoint? = null,
    val startDate: Timestamp = Timestamp.now(),
    val endDate: Timestamp? = null,
    val timezone: String = "America/Bogota",
    val maxAttendees: Int? = null,
    val currentAttendees: Int = 0,
    val requiresApproval: Boolean = false,
    val organizerIds: List<String> = emptyList(),
    val status: EventStatus = EventStatus.UPCOMING
)

/**
 * Representa un punto geográfico para la ubicación de eventos
 */
data class GeoPoint(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

/**
 * Enumeraciones para los diferentes estados y tipos
 */
enum class PostStatus {
    ACTIVE,
    HIDDEN,
    DELETED,
    UNDER_REVIEW
}

enum class PostVisibility {
    PUBLIC,
    PRIVATE,
    FOLLOWERS_ONLY
}

enum class AttachmentType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    LINK
}

enum class ReportStatus {
    PENDING,
    REVIEWED,
    RESOLVED,
    DISMISSED
}

enum class EventStatus {
    UPCOMING,
    ONGOING,
    COMPLETED,
    CANCELLED,
    POSTPONED
}