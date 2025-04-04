package com.example.datastoredemo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

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
            GUI()
        }
    }

    override fun onPause() {
        super.onPause()

        runBlocking {
            datastore.edit { settings ->

                settings[totalKey] = total
                settings[correctKey] = correct

            }
        }

    }
}

@Composable
fun GUI(){

    var colour_chosen by remember { mutableStateOf(Color.Yellow)}

    val index = colours.indexOf(colour_chosen)
    val colour_chosen_str = colours_str[index]

    var second_colour_str = colours_str[Random.nextInt(colours.size)]
    while (second_colour_str == colour_chosen_str)
        second_colour_str = colours_str[Random.nextInt(colours.size)]

    val correct_button = Random.nextInt(2)

    var first_button_label = colour_chosen_str
    var second_button_label = second_colour_str

    if (correct_button == 1) {
        first_button_label = second_colour_str
        second_button_label = colour_chosen_str
    }



}

