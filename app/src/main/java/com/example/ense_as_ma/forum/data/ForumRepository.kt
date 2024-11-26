package com.example.ense_as_ma.forum.data

import com.example.ense_as_ma.forum.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue

class ForumRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val postsCollection = db.collection("posts")

    // Obtener posts
    suspend fun getPosts(): Flow<List<Post>> = flow {
        try {
            val snapshot = postsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(postId = doc.id)
            }
            emit(posts)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    // Crear post
    suspend fun createPost(post: Post): Result<String> {
        return try {
            // Obtener usuario actual
            val currentUser = auth.currentUser ?: return Result.failure(Exception("No hay usuario autenticado"))

            // Crear un nuevo documento con ID automático
            val postRef = postsCollection.document()

            // Preparar el post con información adicional
            val newPost = post.copy(
                postId = postRef.id,
                userId = currentUser.uid,
                userName = currentUser.displayName ?: "Usuario",
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            // Guardar el post en Firebase
            postRef.set(newPost).await()

            Result.success(postRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementLikeCount(postId: String) : Result<String> {
        return try {
            // Referencia al documento del post
            val postRef = postsCollection.document(postId)

            // Incrementa el campo "likeCount" en 1
            postRef.update("likeCount", FieldValue.increment(1)).await()

            Result.success(postRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener un post específico
    suspend fun getPost(postId: String): Result<Post> {
        return try {
            val doc = postsCollection.document(postId).get().await()
            val post = doc.toObject(Post::class.java)?.copy(postId = doc.id)
                ?: return Result.failure(Exception("Post no encontrado"))
            Result.success(post)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}