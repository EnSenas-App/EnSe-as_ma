package com.example.ense_as_ma.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRepository {
    private val auth = Firebase.auth

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    result.user?.let {
                        continuation.resume(Result.success(it))
                    } ?: continuation.resume(Result.failure(Exception("Error de autenticación: Usuario no encontrado")))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(Exception("Error de autenticación: ${exception.localizedMessage ?: "Error desconocido"}")))
                }
        }

    suspend fun signUp(email: String, password: String): Result<FirebaseUser> =
        suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    result.user?.let {
                        continuation.resume(Result.success(it))
                    } ?: continuation.resume(Result.failure(Exception("Error en el registro: Usuario no creado")))
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(Exception("Error en el registro: ${exception.localizedMessage ?: "Error desconocido"}")))
                }
        }


    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut() = auth.signOut()
}