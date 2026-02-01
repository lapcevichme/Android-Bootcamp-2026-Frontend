package com.teto.planner.presentation.features.meeting_create

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teto.planner.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingCreateScreen() {
    var query by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Создать встречу",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 16.dp),
                                shape = RoundedCornerShape(8.dp),
                                inputField = {
                                    SearchBarDefaults.InputField(
                                        query = query,
                                        onQueryChange = { newQuery -> query = newQuery },
                                        onSearch = { expanded = false },
                                        expanded = expanded,
                                        onExpandedChange = { isExpanded -> expanded = isExpanded },
                                        placeholder = { Text("Поиск участников") },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription = null
                                            )
                                        },
                                        trailingIcon = {
                                            if (query.isNotEmpty()) {
                                                IconButton(onClick = { query = "" }) {
                                                    Icon(
                                                        Icons.Default.Clear,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        }
                                    )
                                },
                                expanded = expanded,
                                onExpandedChange = { isExpanded -> expanded = isExpanded }
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    items(4) { index ->
                                        ListItem(
                                            headlineContent = { Text("Участник $index") },
                                            leadingContent = {
                                                Icon(
                                                    Icons.Default.Person,
                                                    contentDescription = null
                                                )
                                            },
                                            modifier = Modifier.clickable {
                                                query = "Участник $index"
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    items(3) { index ->
                        ParticipantCard(name = "Имя пользователя", busyness = index + 1)
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    item {
                         Text(
                             text = "Когда?",
                             style = MaterialTheme.typography.titleLarge,
                             color = MaterialTheme.colorScheme.onSurface,
                             modifier = Modifier.padding(8.dp)
                         )
                    }

                    item {
                        FlowRow(modifier = Modifier.fillMaxWidth()) {

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ParticipantCard(name: String, busyness: Int) {
    var borderColor = Color.Green
    when (busyness){
        1 -> { borderColor = Color.Green }
        2 -> { borderColor = Color.Yellow }
        3 -> { borderColor = Color.Red }
    }
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .size(40.dp)
                .border(2.dp, color = borderColor, shape = CircleShape)
                .padding(2.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center){
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.fillMaxSize(0.6f)
                )
            }

            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = "Delete participant",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ParticipantCardPreview() {
   AppTheme {
       ParticipantCard("Oat", 1)
   }
}

@Preview
@Composable
fun MeetingCreateScreenPreview() {
    AppTheme {
        MeetingCreateScreen()
    }
}

