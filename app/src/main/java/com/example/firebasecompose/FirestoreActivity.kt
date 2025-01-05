package com.example.firebasecompose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreActivity : ComponentActivity() {
    private val technologies = mutableStateListOf<Technology>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Firebase.firestore

        db.collection("technologies")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val tech = Technology(
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        version = document.getString("version") ?: "",
                        releaseYear = document.getString("releaseYear") ?: ""
                    )
                    technologies.add(tech)
                }
                setContent {
                    FireBaseComposeTheme {
                        Scaffold { innerPadding ->
                            TechnologyList(technologies, Modifier.padding(innerPadding))
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }
}

@Composable
fun TechnologyList(technologies: List<Technology>, modifier: Modifier) {
    val context = LocalContext.current

    LazyColumn(modifier = modifier) {
        items(technologies.size) { index ->
            val technology = technologies[index]
            TechnologyCard(technology) {
                val intent = Intent(context, TechnologyDetailActivity::class.java).apply {
                    putExtra("technologyName", technology.name)
                    putExtra("technologyDescription", technology.description)
                    putExtra("technologyVersion", technology.version)
                    putExtra("technologyReleaseYear", technology.releaseYear)
                }
                context.startActivity(intent)
            }
        }
    }
}

@Composable
fun TechnologyCard(technology: Technology, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = Color.LightGray
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = technology.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "Release Year: ${technology.releaseYear}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FireBaseComposeTheme {
        TechnologyCard(Technology("Sample Tech", "A sample technology.", "1.0", "2023")) {}
    }
}