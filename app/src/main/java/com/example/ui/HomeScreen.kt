package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ServerEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ServerViewModel,
    onNavigateToServers: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val servers by viewModel.servers.collectAsState()

    var isConnected by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var selectedServer by remember(servers) {
        mutableStateOf(servers.firstOrNull())
    }

    var downloadSpeed by remember { mutableStateOf("0.0 MB/s") }
    var uploadSpeed by remember { mutableStateOf("0.0 MB/s") }
    var showServerSelectDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    // Live speed simulation when connected
    LaunchedEffect(isConnected) {
        if (isConnected) {
            while (true) {
                delay(1200)
                val dl = (800..2800).random() / 100f
                val ul = (400..1800).random() / 100f
                downloadSpeed = String.format(Locale.US, "%.2f MB/s", dl)
                uploadSpeed = String.format(Locale.US, "%.2f MB/s", ul)
            }
        } else {
            downloadSpeed = "0.0 MB/s"
            uploadSpeed = "0.0 MB/s"
        }
    }

    // Pulse transition animation for connection ring
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isConnected || isConnecting) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val spinRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Deep Navy theme colors for top header card (matching V2Pro screenshot)
    val headerDarkColor = Color(0xFF1E2438)
    val headerAccentColor = Color(0xFF2A314A)
    val brandCyanColor = Color(0xFF00E5FF)
    val brandPurpleColor = Color(0xFF7C4DFF)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6FA))
            .verticalScroll(rememberScrollState())
    ) {
        // TOP DARK CARD HEADER WITH LOGO & CONNECTION BUTTON
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF14192B),
                            headerDarkColor,
                            Color(0xFF242B42)
                        )
                    )
                )
                .padding(bottom = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(headerAccentColor)
                            .clickable { onNavigateToServers() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Menu / Servers",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = brandPurpleColor.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, brandPurpleColor.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { showAboutDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "V2PRO VPN",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(headerAccentColor)
                            .clickable { showServerSelectDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Server List",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Brand Emblem Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(brandCyanColor, brandPurpleColor)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "V2PRO VPN",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "LAWKSAWK CITY TUNNEL",
                            color = brandCyanColor,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // CENTRAL POWER / CONNECTION WIDGET (Matching V2Pro screenshot)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(170.dp)
                ) {
                    // Outer glowing animated ring
                    Box(
                        modifier = Modifier
                            .size((160 * pulseScale).dp)
                            .clip(CircleShape)
                            .background(
                                if (isConnected) brandCyanColor.copy(alpha = 0.25f)
                                else if (isConnecting) brandPurpleColor.copy(alpha = 0.3f)
                                else Color.White.copy(alpha = 0.08f)
                            )
                    )

                    // Inner main connection badge
                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(
                                width = 4.dp,
                                brush = if (isConnected) Brush.linearGradient(listOf(brandCyanColor, brandPurpleColor))
                                else if (isConnecting) Brush.linearGradient(listOf(brandPurpleColor, Color.White))
                                else Brush.linearGradient(listOf(Color(0xFFE0E0E0), Color.White)),
                                shape = CircleShape
                            )
                            .clickable {
                                if (isConnecting) return@clickable
                                if (isConnected) {
                                    isConnected = false
                                    Toast
                                        .makeText(context, "VPN Disconnected", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    isConnecting = true
                                    coroutineScope.launch {
                                        delay(1500)
                                        isConnecting = false
                                        isConnected = true
                                        if (selectedServer != null) {
                                            viewModel.toggleFavorite(selectedServer!!) // Touch activity state
                                        }
                                        Toast
                                            .makeText(
                                                context,
                                                "Connected to ${selectedServer?.name ?: "V2Ray Server"}",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                            .testTag("v2pro_connect_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isConnected) Color(0xFF1E2438)
                                        else if (isConnecting) brandPurpleColor
                                        else Color(0xFF2C3246)
                                    )
                                    .then(if (isConnecting) Modifier.rotate(spinRotation) else Modifier),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "Connect",
                                    tint = if (isConnected) brandCyanColor else Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (isConnecting) "CONNECTING" else if (isConnected) "STOP" else "LET'S GO",
                                color = Color(0xFF1E2438),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Connection Status Text
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                if (isConnected) Color(0xFF00E676)
                                else if (isConnecting) Color(0xFFFFD600)
                                else Color(0xFFFF5252)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isConnecting) "Connecting..."
                        else if (isConnected) "Connected & Protected"
                        else "Disconnected",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // COUNTRY / SERVER SELECTION CARD (Matching V2Pro Screenshot layout)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("server_selection_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Flag Badge Icon
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF0F4FF),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = getCountryFlagEmoji(selectedServer?.countryCode ?: "GB"),
                                    fontSize = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = selectedServer?.country ?: "United Kingdom",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E2438)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${selectedServer?.serverAddress ?: "212.102.40.23"} • ${selectedServer?.pingMs ?: 24} ms",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    // Select Server Button
                    Button(
                        onClick = { showServerSelectDialog = true },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F3FA),
                            contentColor = Color(0xFF1E2438)
                        ),
                        modifier = Modifier.testTag("select_server_button")
                    ) {
                        Text(
                            text = "Select Server",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BOTTOM TRAFFIC METRICS (Download & Upload Widgets)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Download Speed Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("download_stat_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Download",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "DOWNLOAD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = downloadSpeed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E2438)
                            )
                        }
                    }
                }

                // Upload Speed Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("upload_stat_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE3F2FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = "Upload",
                                tint = Color(0xFF1565C0),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "UPLOAD",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = uploadSpeed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1E2438)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // SERVER SELECTION DIALOG
    if (showServerSelectDialog) {
        AlertDialog(
            onDismissRequest = { showServerSelectDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Select VPN Location", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    servers.forEach { server ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedServer = server
                                    showServerSelectDialog = false
                                },
                            shape = RoundedCornerShape(14.dp),
                            color = if (server.id == selectedServer?.id) Color(0xFFEAEFFC) else Color(0xFFF8F9FE),
                            border = if (server.id == selectedServer?.id) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3F51B5)) else null
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = getCountryFlagEmoji(server.countryCode),
                                        fontSize = 22.sp,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                    Column {
                                        Text(
                                            text = server.name,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E2438)
                                        )
                                        Text(
                                            text = "${server.country} • ${server.protocol}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White
                                ) {
                                    Text(
                                        text = "${server.pingMs} ms",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (server.pingMs < 50) Color(0xFF2E7D32) else Color(0xFFE65100),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showServerSelectDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Text(
                    text = "About V2PRO VPN",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E2438)
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE8ECF8),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Powered by အောင်ကျော်ခန့်",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF1E2438),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Text(
                        text = "ဖုန်း − 09697940611",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF1565C0)
                    )

                    Text(
                        text = "Lawksawk City,Shan State",
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

// Helper to render Country Flag Emojis from 2-letter Country Codes
private fun getCountryFlagEmoji(countryCode: String): String {
    if (countryCode.length != 2) return "🌐"
    val firstChar = Character.codePointAt(countryCode.uppercase(Locale.US), 0) - 0x41 + 0x1F1E6
    val secondChar = Character.codePointAt(countryCode.uppercase(Locale.US), 1) - 0x41 + 0x1F1E6
    return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
}
