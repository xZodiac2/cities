package com.ilya.words

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    
    private lateinit var myApplication: MyApplication
    private var prevCity = ""
    private val usedCities = mutableListOf<String>()
    private var isFirstCity = true
    private var lastChar = '_'
    private var displayLastChar = '_'
    
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CitiesGameUI() {
        
        var city by remember { mutableStateOf("") }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(top = 180.dp)
            ) {
                Column(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Прошлый город: ${prevCity.replaceFirstChar {
                        if (it.isLowerCase())
                            it.titlecase()
                        else
                            it.toString()
                    }}")
                    Text(text = "Буква: $displayLastChar", fontSize = 22.sp)
                    TextField(
                        value = city,
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.LightGray, textColor = Color.Black),
                        onValueChange = {
                            city = it
                        },
                        label = {
                            Text(text = "Введите название города")
                        })
                }
            }
            Button(
                onClick = {
                    checkCity(city)
                    city = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.DarkGray)
            ) {
                Text(text = "Подтвердить")
            }
        }
    }
    
    private fun setDisplayLastChar(currentCity: String) {
        displayLastChar = if (currentCity[prevCity.length - 1] == 'ь' || currentCity[prevCity.length - 1] == 'ъ' || currentCity[prevCity.length - 1] == 'ы')
            currentCity[currentCity.length - 2]
        else
            currentCity[currentCity.length - 1]
    }
    
    private fun setLastChar() {
        if (!isFirstCity) {
            lastChar = if (prevCity[prevCity.length - 1] == 'ь' || prevCity[prevCity.length - 1] == 'ъ' || prevCity[prevCity.length - 1] == 'ы')
                prevCity[prevCity.length - 2]
            else
                prevCity[prevCity.length - 1]
        }
    }
    
    private fun checkCity(city: String) {
    
        setLastChar()
        
        if (city.lowercase() !in myApplication.getCities()) {
            Toast.makeText(this, "Неизвестный город", Toast.LENGTH_SHORT).show()
            return
        } else if (city in usedCities) {
            Toast.makeText(this, "Город уже использован", Toast.LENGTH_SHORT).show()
            return
        } else if (!isFirstCity) {
            if (city[0].lowercase() != lastChar.lowercase()) {
                Toast.makeText(this, "Первая буква города не совпадает с последней буквой предыдущего города", Toast.LENGTH_SHORT).show()
                return
            }
        }
    
        prevCity = city
        isFirstCity = false
        usedCities += city
        setDisplayLastChar(city)
        
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        myApplication = (applicationContext as MyApplication)
        
        setContent {
            CitiesGameUI()
        }
    }
}
