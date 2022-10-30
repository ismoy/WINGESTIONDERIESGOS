package com.win.gestionderiesgos.data.remote.provider

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

/** * Created by ISMOY BELIZAIRE on 23/10/2022. */
class AuthProvider {
 private var auth:FirebaseAuth = FirebaseAuth.getInstance()

 suspend fun register(email:String?,password:String?):Task<AuthResult>{
  return auth.createUserWithEmailAndPassword(email!!,password!!)
 }
  fun getId():String?{
  return auth.currentUser?.uid
 }
 fun isEmailVerified(): Boolean? {
  return auth.currentUser?.isEmailVerified
 }
 fun logout() {
  auth.signOut()
 }

 fun language() {
  auth.setLanguageCode("es")
 }
 suspend fun login(email:String, password: String): Task<AuthResult> {
  return auth.signInWithEmailAndPassword(email, password)
 }

 fun resetPassword(email: String): Task<Void> {
  return auth.sendPasswordResetEmail(email)
 }
 fun existSession(): Boolean {
  var exist: Boolean = false
  if (auth.currentUser != null) {
   exist = true
  }
  return exist
 }

}