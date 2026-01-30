package com.teto.planner.presentation.features.schedule

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мое расписание",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge) },
                actions = { IconButton(onClick = {}) {
                    Icon(
                        contentDescription = "Profile",
                        imageVector = Icons.Default.Person

                    )
                } }
        )}
    ) { innerPadding ->
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(modifier = Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Календарь")
                }

                Box(modifier = Modifier.weight(1f).padding(horizontal = 16.dp).fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Встречи на 17 Августа",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            item { TaskCard() }
                            item { TaskCard() }
                            item { TaskCard() }
                            item { TaskCard() }
                            item { TaskCard() }
                            item { TaskCard() }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard() {
    Card(modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainer)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = "09:00",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                Text(
                    text = "Утренний созвон",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Участников: 16",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
fun ScheduleScreenPreview() {
    MaterialTheme{
        ScheduleScreen()
    }
}

@Preview
@Composable
fun TaskCardPreview() {
    MaterialTheme{
        TaskCard()
    }
}