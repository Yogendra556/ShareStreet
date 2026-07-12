package com.example.sharestreet.presentation.Location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharestreet.ViewModels.AuthViewModel
import com.example.sharestreet.ViewModels.FriendViewModel
import com.example.sharestreet.ViewModels.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllowedFriendsScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    friendViewModel: FriendViewModel = hiltViewModel()
) {
    val currentUser = authViewModel.getCurrentUser()?.uid
    val friendsList by locationViewModel.allowedUsersList.collectAsStateWithLifecycle()

    LaunchedEffect(currentUser) {
        currentUser?.let { locationViewModel.observeAllAllowedUsers(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location Sharing") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Decide who can see your location",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            when {
                currentUser == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Please sign in to manage location sharing.")
                    }
                }
                friendsList.isEmpty() -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No friends found yet.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                else -> {
                    FriendsList(
                        friendsList = friendsList,
                        onAllow = { friendId -> locationViewModel.addAllowedUsers(currentUser, friendId) },
                        onDisallow = { friendId -> locationViewModel.removeAllowedUsers(currentUser, friendId) }
                    )
                }
            }
        }
    }
}

@Composable
fun FriendsList(
    friendsList: List<LocationAccessState>,
    onAllow: (String) -> Unit = {},
    onDisallow: (String) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(friendsList, key = { it.user?.uid ?: it.hashCode() }) { friend ->
            FriendCard(
                friend = friend,
                onAllow = onAllow,
                onDisallow = onDisallow
            )
        }
    }
}

@Composable
fun FriendCard(
    friend: LocationAccessState,
    onAllow: (String) -> Unit = {},
    onDisallow: (String) -> Unit = {}
) {
    val user = friend.user ?: return

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                val initial = user.displayName?.firstOrNull()?.uppercaseChar()
                if (initial != null) {
                    Text(
                        text = initial.toString(),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = user.displayName ?: "Unknown user",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Allow/Disallow toggle
            Switch(
                checked = friend.allowed,
                onCheckedChange = { checked ->
                    if (checked) onAllow(user.uid) else onDisallow(user.uid)
                }
            )
        }
    }
}