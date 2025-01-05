package com.example.firebasecompose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.firebasecompose.ui.theme.FireBaseComposeTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth

        setContent {
            FireBaseComposeTheme {
                LoginScreen(auth) { signInWithGoogle() }
            }
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Test", "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra("currentUserEmail", email)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("Test", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithCredential:success")
                    val userEmail = account?.email
                    navigateToHomeScreen(userEmail)
                } else {
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHomeScreen(userEmail: String?) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("currentUserEmail", userEmail)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "Google Sign-In failed", e)
            }
        }
    }

    @Composable
    fun LoginScreen(auth: FirebaseAuth, onGoogleSignIn: () -> Unit) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val context = LocalContext.current
        val showDialog = remember { mutableStateOf(false) }

        if (showDialog.value) {
            ForgotPasswordDialog(
                onDismiss = { showDialog.value = false },
                onReset = { email ->
                    resetPassword(email, context)
                    showDialog.value = false
                }
            )
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Вход в приложение", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(32.dp))

                TextField(
                    value = username.value,
                    onValueChange = { username.value = it },
                    label = { Text("Логин (Email)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text("Пароль") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        signInWithEmail(username.value, password.value)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Войти")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { showDialog.value = true }) {
                    Text("Забыл пароль?")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = {
                    val intent = Intent(context, RegistrationActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text("Регистрация")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onGoogleSignIn() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Войти через Google")
                }
            }
        }
    }

    @Composable
    fun ForgotPasswordDialog(onDismiss: () -> Unit, onReset: (String) -> Unit) {
        val email = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Сброс пароля") },
            text = {
                Column {
                    Text("Введите ваш email для сброса пароля")
                    TextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text("Email") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = { onReset(email.value) }) {
                    Text("Сбросить")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        )
    }

    private fun resetPassword(email: String, context: Context) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Письмо для сброса пароля отправлено на $email", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Ошибка: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}