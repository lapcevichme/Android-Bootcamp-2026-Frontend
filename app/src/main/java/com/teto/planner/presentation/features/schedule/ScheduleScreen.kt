package com.teto.planner.presentation.features.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teto.planner.domain.model.Meeting
import com.teto.planner.presentation.common.SharedCalendar
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onCreateMeeting: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мое расписание",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge) },
                actions = { IconButton(onClick = { onProfileClick() }) {
                    Icon(
                        contentDescription = "Profile",
                        imageVector = Icons.Default.Person
                    )
                } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateMeeting) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Meeting",
                    modifier = Modifier
                )
            }
        }
    ) { innerPadding ->
        Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box(contentAlignment = Alignment.Center) {
                    SharedCalendar(
                        selectedDate = LocalDate.now(),
                        onDateSelected = {},
                        meetingsByDate = mapOf(
                            LocalDate.now() to listOf(
                                Meeting("", "", "", LocalDate.now(), listOf(""))
                            ),
                            LocalDate.now().plusDays(3) to listOf(
                                Meeting("", "", "", LocalDate.now().plusDays(3), listOf(""))
                            )
                        )
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Box(modifier = Modifier.weight(1f).padding(start = 16.dp, end = 16.dp, top = 16.dp).fillMaxSize()) {
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
        ScheduleScreen(
            onCreateMeeting = {},
            onProfileClick = {}
        )
    }
}

@Preview
@Composable
fun TaskCardPreview() {
    MaterialTheme{
        TaskCard()
    }
}