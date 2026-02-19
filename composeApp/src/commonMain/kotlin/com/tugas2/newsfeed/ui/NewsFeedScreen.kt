package com.tugas2.newsfeed.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tugas2.newsfeed.data.NewsRepository
import com.tugas2.newsfeed.model.Category
import com.tugas2.newsfeed.model.NewsUi
import com.tugas2.newsfeed.presentation.NewsFeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen() {
    val vm = remember { NewsFeedViewModel(NewsRepository()) }
    LaunchedEffect(Unit) { vm.start() }

    val feed by vm.feed.collectAsState()
    val readCount by vm.readCount.collectAsState()
    val lastDetail by vm.lastDetail.collectAsState()

    var selected by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("News Feed Simulator") },
                actions = {
                    TextButton(
                        onClick = {
                            selected = null
                            vm.setCategory(null)
                        }
                    ) { Text("Reset") }
                }
            )
        }
    ) { padding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val contentMaxWidth = if (maxWidth > 560.dp) 560.dp else maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = contentMaxWidth)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {

                // ---- Stats card (rapi & tidak "nempel") ----
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            "Read count: $readCount",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            lastDetail?.takeIf { it.isNotBlank() }
                                ?: "Tap berita untuk mark as read & fetch detail (async).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ---- Filter row (horizontal scroll, tidak kepotong) ----
                Text("Kategori", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))

                CategoryChips(
                    selected = selected,
                    onSelect = { cat ->
                        selected = cat
                        vm.setCategory(cat)
                    }
                )

                Spacer(Modifier.height(12.dp))

                // ---- Feed list ----
                if (feed.isEmpty()) {
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(14.dp)) {
                            Text("Menunggu berita...", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(6.dp))
                            Text(
                                "Flow akan menambahkan berita setiap 2 detik.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(feed, key = { it.id }) { item ->
                            NewsItemCard(
                                item = item,
                                onClick = { vm.markAsRead(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryChips(
    selected: Category?,
    onSelect: (Category?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(end = 8.dp)
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text("All") }
            )
        }

        items(Category.entries) { cat ->
            FilterChip(
                selected = selected == cat,
                onClick = { onSelect(cat) },
                label = { Text(cat.shortLabel()) }
            )
        }
    }
}

private fun Category.shortLabel(): String = when (this) {
    Category.TECH -> "Tech"
    Category.SPORTS -> "Sports"
    Category.BUSINESS -> "Biz"
    Category.ENTERTAINMENT -> "Ent"
}

@Composable
private fun NewsItemCard(
    item: NewsUi,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AssistChip(
                    onClick = { /* chip tidak melakukan apa-apa */ },
                    label = { Text(item.categoryLabel) },
                    enabled = false
                )

                Text(
                    text = item.displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                "Tap untuk mark as read + ambil detail",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
