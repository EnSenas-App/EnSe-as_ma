package com.example.ense_as_ma.forum.data

import com.example.ense_as_ma.forum.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class ForumRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Referencias a colecciones
    private val postsCollection = db.collection("posts")
    private val categoriesCollection = db.collection("categories")
    private val commentsCollection = db.collection("comments")
    private val usersCollection = db.collection("users")

    // Posts
    suspend fun getPosts(limit: Long = 20): Flow<List<Post>> = flow {
        val snapshot = postsCollection
            .whereEqualTo("status", PostStatus.ACTIVE)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        emit(snapshot.toObjects(Post::class.java))
    }

    suspend fun getPostsByCategory(categoryId: String): Flow<List<Post>> = flow {
        val snapshot = postsCollection
            .whereEqualTo("categoryId", categoryId)
            .whereEqualTo("status", PostStatus.ACTIVE)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        emit(snapshot.toObjects(Post::class.java))
    }

    suspend fun createPost(post: Post): Result<String> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))

            // Obtener información del usuario
            val userDoc = usersCollection.document(currentUser.uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("Información de usuario no encontrada"))

            // Crear el post con la información del usuario
            val docRef = postsCollection.document()
            val postWithUserInfo = post.copy(
                postId = docRef.id,
                userId = currentUser.uid,
                userName = user.username,
                userImage = user.profileImage,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            // Guardar el post
            docRef.set(postWithUserInfo).await()

            // Actualizar contadores
            db.runTransaction { transaction ->
                // Incrementar contador en la categoría
                val categoryRef = categoriesCollection.document(post.categoryId)
                transaction.update(categoryRef, "postCount", com.google.firebase.firestore.FieldValue.increment(1))

                // Incrementar contador en estadísticas del usuario
                val userRef = usersCollection.document(currentUser.uid)
                transaction.update(userRef, "stats.postsCount", com.google.firebase.firestore.FieldValue.increment(1))
            }.await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePost(postId: String, updates: Map<String, Any>): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val postDoc = postsCollection.document(postId).get().await()
            val post = postDoc.toObject(Post::class.java)
                ?: return Result.failure(Exception("Post no encontrado"))

            // Verificar permisos
            if (post.userId != currentUser.uid && !isUserModerator(currentUser.uid)) {
                return Result.failure(Exception("No tienes permiso para editar este post"))
            }

            // Registrar historial de edición
            val editRecord = EditRecord(
                editedBy = currentUser.uid,
                previousContent = post.content,
                editedAt = Timestamp.now()
            )

            val updatesWithMetadata = updates.toMutableMap().apply {
                put("updatedAt", Timestamp.now())
                put("editHistory", post.editHistory + editRecord)
            }

            postsCollection.document(postId).update(updatesWithMetadata).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Comentarios
    suspend fun getComments(postId: String): Flow<List<Comment>> = flow {
        val snapshot = commentsCollection
            .whereEqualTo("postId", postId)
            .whereEqualTo("status", CommentStatus.ACTIVE)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .await()
        emit(snapshot.toObjects(Comment::class.java))
    }

    suspend fun addComment(comment: Comment): Result<String> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))

            // Obtener información del usuario
            val userDoc = usersCollection.document(currentUser.uid).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return Result.failure(Exception("Información de usuario no encontrada"))

            val docRef = commentsCollection.document()
            val commentWithUserInfo = comment.copy(
                commentId = docRef.id,
                userId = currentUser.uid,
                userName = user.username,
                userImage = user.profileImage,
                createdAt = Timestamp.now()
            )

            // Transacción para crear comentario y actualizar contadores
            db.runTransaction { transaction ->
                // Guardar comentario
                transaction.set(docRef, commentWithUserInfo)

                // Incrementar contador de comentarios en el post
                val postRef = postsCollection.document(comment.postId)
                transaction.update(postRef,
                    "commentCount", com.google.firebase.firestore.FieldValue.increment(1),
                    "lastActivity", Timestamp.now()
                )

                // Incrementar contador en estadísticas del usuario
                val userRef = usersCollection.document(currentUser.uid)
                transaction.update(userRef, "stats.commentsCount", com.google.firebase.firestore.FieldValue.increment(1))
            }.await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    // Categorías
    suspend fun getCategories(): Flow<List<Category>> = flow {
        val snapshot = categoriesCollection
            .whereEqualTo("isActive", true)
            .orderBy("order")
            .get()
            .await()
        emit(snapshot.toObjects(Category::class.java))
    }

    suspend fun toggleLike(postId: String): Result<Boolean> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val postRef = postsCollection.document(postId)
            val likesRef = postRef.collection("likes").document(currentUser.uid)

            var liked = false
            db.runTransaction { transaction ->
                val likeDoc = transaction.get(likesRef)
                if (likeDoc.exists()) {
                    // Quitar like
                    transaction.delete(likesRef)
                    transaction.update(postRef, "likeCount", com.google.firebase.firestore.FieldValue.increment(-1))
                    liked = false
                } else {
                    // Añadir like
                    transaction.set(likesRef, mapOf(
                        "userId" to currentUser.uid,
                        "createdAt" to Timestamp.now()
                    ))
                    transaction.update(postRef, "likeCount", com.google.firebase.firestore.FieldValue.increment(1))
                    liked = true
                }
            }.await()

            Result.success(liked)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun reportPost(postId: String, reason: String, description: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val report = Report(
                reportedBy = currentUser.uid,
                reason = reason,
                description = description,
                reportedAt = Timestamp.now()
            )

            val postRef = postsCollection.document(postId)
            postRef.update("reports", com.google.firebase.firestore.FieldValue.arrayUnion(report)).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Utilidades
    private suspend fun isUserModerator(userId: String): Boolean {
        val userDoc = usersCollection.document(userId).get().await()
        val user = userDoc.toObject(User::class.java)
        return user?.isAdmin == true
    }
}