package com.ytaicommander

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CommanderApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommanderApp() {
    var connected by remember { mutableStateOf(false) }
    var autoReply by remember { mutableStateOf(false) }
    var emailReports by remember { mutableStateOf(true) }
    var polling by remember { mutableStateOf("Every 30 minutes") }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("YT AI COMMANDER")
                            Text("Shorts channel assistant", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Channel connection", style = MaterialTheme.typography.titleLarge)
                            Text(
                                if (connected) "Demo connection state: connected" else "Connect your Google / YouTube account to start.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(onClick = { connected = !connected }, modifier = Modifier.fillMaxWidth()) {
                                Icon(Icons.Default.AccountCircle, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text(if (connected) "Disconnect demo" else "Connect Google (setup required)")
                            }
                            Text("Google OAuth is not configured in this starter project.", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                item { MetricCard("Latest Shorts", "—", "Connect channel to load uploaded videos", Icons.Default.VideoLibrary) }
                item { MetricCard("Total views", "—", "YouTube Analytics API connection required", Icons.Default.Visibility) }
                item { MetricCard("Viral watch", "Not configured", "Trend scoring will compare recent view growth", Icons.Default.TrendingUp) }
                item {
                    Card {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Automation settings", style = MaterialTheme.typography.titleLarge)
                            Text("New Unlisted video detection")
                            Text("Polling: $polling", style = MaterialTheme.typography.bodySmall)
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                OutlinedButton(onClick = { expanded = true }) { Text("Change interval") }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    listOf("Every 15 minutes", "Every 30 minutes", "Every 1 hour", "Manual check").forEach {
                                        DropdownMenuItem(text = { Text(it) }, onClick = { polling = it; expanded = false })
                                    }
                                }
                            }
                            HorizontalDivider()
                            SettingSwitch("AI auto-reply to comments", autoReply) { autoReply = it }
                            SettingSwitch("Daily Gmail reports", emailReports) { emailReports = it }
                            Text(
                                "Auto-replies should be rate-limited and avoid repetitive or misleading messages.",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
                item {
                    Card {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("How it will work", style = MaterialTheme.typography.titleLarge)
                            Text("1. Detect new uploads from your channel's Uploads playlist.")
                            Text("2. Read video metadata and update SEO only after AI review/configuration.")
                            Text("3. Read comments and generate contextual replies.")
                            Text("4. Pull Analytics metrics and email a daily summary.")
                        }
                    }
                }
                item {
                    Text(
                        "Starter UI only • API/OAuth/backend setup is still required before real channel actions.",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(title: String, value: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(value, style = MaterialTheme.typography.headlineSmall)
                Text(detail, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}
