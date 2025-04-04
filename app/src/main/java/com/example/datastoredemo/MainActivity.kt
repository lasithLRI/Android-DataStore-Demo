package com.example.datastoredemo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

var colours = listOf(Color.Black, Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.White)
var colours_str = listOf("Black", "Red", "Green", "Blue", "Yellow", "White")

var correct = 0
var total = 0

class MainActivity : ComponentActivity() {

    lateinit var preferences: Preferences
    lateinit var totalKey: Preferences.Key<Int>
    lateinit var correctKey: Preferences.Key<Int>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        totalKey = intPreferencesKey("total")
        correctKey = intPreferencesKey("correct")

        runBlocking {
            preferences = datastore.data.first()
        }

        setContent {

        }
    }
}

