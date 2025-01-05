package com.example.firebasecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TechnologyDetailActivity : ComponentActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val technologyName = intent.getStringExtra("technologyName") ?: "Unknown Technology"
        val technologyDescription = intent.getStringExtra("technologyDescription") ?: "No description"
        val technologyVersion = intent.getStringExtra("technologyVersion") ?: "No version"
        val technologyReleaseYear = intent.getStringExtra("technologyReleaseYear") ?: "Unknown year"

        setContent {
            FireBaseComposeTheme {
                Surface(color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxHeight()) {
                    TechnologyDetailScreen(
                        name = technologyName,
                        description = technologyDescription,
                        version = technologyVersion,
                        releaseYear = technologyReleaseYear
                    )
                }
            }
        }
    }

    @Composable
    fun TechnologyDetailScreen(
        name: String,
        description: String,
        version: String,
        releaseYear: String
    ) {
        var editedName by remember { mutableStateOf(name) }
        var editedDescription by remember { mutableStateOf(description) }
        var editedVersion by remember { mutableStateOf(version) }
        var editedReleaseYear by remember { mutableStateOf(releaseYear) }

        Column(modifier = Modifier.padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Name: $name", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Description: $description", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Version: $version", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Release Year: $releaseYear", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = editedName,
                onValueChange = { editedName = it },
                label = { Text("Edit Name") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = editedDescription,
                onValueChange = { editedDescription = it },
                label = { Text("Edit Description") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = editedVersion,
                onValueChange = { editedVersion = it },
                label = { Text("Edit Version") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = editedReleaseYear,
                onValueChange = { editedReleaseYear = it },
                label = { Text("Edit Release Year") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    db.collection("technologies").document(name).set(Technology(editedName, editedDescription, editedVersion, editedReleaseYear))
                }) {
                    Text("Update")
                }
                Button(onClick = {
                    db.collection("technologies").document(name).delete()
                }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Delete")
                }
            }
        }
    }
}
