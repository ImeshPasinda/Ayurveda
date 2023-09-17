package com.example.ayurveda

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveUserSession(email: String) {
        editor.putString("userEmail", email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("userEmail", null)
    }

    fun isUserLoggedIn(): Boolean {
        // Check if the user is logged in based on saved session data
        val userEmail = sharedPreferences.getString("userEmail", null)
        return !userEmail.isNullOrEmpty()
    }

    fun logout() {
        editor.clear()
        editor.apply()
    }
}

