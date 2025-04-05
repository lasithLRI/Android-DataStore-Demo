package com.example.datastoredemo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        Text("Score : $correct / $total",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(bottom = 30.dp, top = 10.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.End

            )
        
        Button(
            modifier = Modifier.size(height = 100.dp, width = 100.dp),
            onClick = { /*TODO*/ },
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = colour_chosen
            )
        ) {

        }

        Row (
            modifier = Modifier.padding(top = 30.dp)
        ){

            Button(onClick = {
                ++total
                if (correct_button == 0)
                    ++correct
                colour_chosen = nextGame(colour_chosen)
            }) {
                Text(first_button_label)
            }
            Button(onClick = {
                ++total
                if (correct_button == 1)
                    ++correct
                colour_chosen = nextGame(colour_chosen)
            }) {
                Text(second_button_label)
            }

        }
    }

}

fun nextGame(
    previous_color_choosen:Color
):Color{
    var index = Random.nextInt(colours.size)
    var color_choosen = colours[index]

    while (previous_color_choosen == color_choosen){
        index = Random.nextInt(colours.size)
        color_choosen = colours[index]
    }

    return color_choosen
}

