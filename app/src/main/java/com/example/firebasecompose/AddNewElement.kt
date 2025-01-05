package com.example.firebasecompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddNewElement : ComponentActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FireBaseComposeTheme {
                AddNewElementScreen()
            }
        }
    }

    @Composable
    fun AddNewElementScreen() {
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var version by remember { mutableStateOf("") }
        var releaseYear by remember { mutableStateOf("") }
        val context = LocalContext.current

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Добавить новый элемент технологии", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = version,
                    onValueChange = { version = it },
                    label = { Text("Версия") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = releaseYear,
                    onValueChange = { releaseYear = it },
                    label = { Text("Год выпуска") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && description.isNotEmpty() && version.isNotEmpty() && releaseYear.isNotEmpty()) {
                            try {
                                val technology = Technology(name, description, version, releaseYear)
                                addTechnology(technology)
                            } catch (e: NumberFormatException) {
                                Toast.makeText(context, "Введите корректный год выпуска", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Добавить элемент")
                }
            }
        }
    }

    private fun addTechnology(technology: Technology) {
        db.collection("technologies").document(technology.name).set(technology)
    }
}