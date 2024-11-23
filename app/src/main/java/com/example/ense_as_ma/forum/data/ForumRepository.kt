package com.example.ense_as_ma.forum.data

import com.example.ense_as_ma.forum.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForumRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")
    private val categoriesCollection = db.collection("categories")
    private val commentsCollection = db.collection("comments")

    suspend fun getPosts(): Flow<List<Post>> = flow {
        val snapshot = postsCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
        emit(snapshot.toObjects(Post::class.java))
    }

    suspend fun getPost(postId: String): Post? {
        return postsCollection.document(postId).get().await().toObject(Post::class.java)
    }

    suspend fun createPost(post: Post): Result<String> = try {
        val docRef = postsCollection.document()
        val postWithId = post.copy(postId = docRef.id)
        docRef.set(postWithId).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getCategories(): Flow<List<Category>> = flow {
        val snapshot = categoriesCollection.get().await()
        emit(snapshot.toObjects(Category::class.java))
    }

    suspend fun getComments(postId: String): Flow<List<Comment>> = flow {
        val snapshot = commentsCollection
            .whereEqualTo("postId", postId)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .await()
        emit(snapshot.toObjects(Comment::class.java))
    }

    suspend fun addComment(comment: Comment): Result<String> = try {
        val docRef = commentsCollection.document()
        val commentWithId = comment.copy(commentId = docRef.id)
        docRef.set(commentWithId).await()
        Result.success(docRef.id)
    } catch (e: Exception) {
        Result.failure(e)
    }
}