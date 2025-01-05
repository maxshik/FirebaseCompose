package com.example.firebasecompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireBaseComposeTheme {
                UserProfileScreen(intent.getStringExtra("currentUserEmail").toString())
            }
        }
    }
}

@Composable
fun UserProfileScreen(email: String) {
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
            Text(text = "Добро пожаловать!", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Ваш email: $email", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    Firebase.auth.signOut()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Выйти")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val i = Intent(context, TechnologyActivity::class.java)
                    context.startActivity(i)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Технологии")
            }
        }
    }
}