package com.example.firebasecompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme

class TechnologyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FireBaseComposeTheme {
                TechnologyScreen()
            }
        }
    }

    @Composable
    fun TechnologyScreen() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Выберите технологию", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    startActivity(Intent(context, AddNewElement::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Добавить элемент")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    startActivity(Intent(context, FirestoreActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Firestore")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}