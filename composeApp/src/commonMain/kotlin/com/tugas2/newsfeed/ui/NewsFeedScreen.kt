package com.tugas2.newsfeed.ui

import androidx.compose.foundation.BorderStroke
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen() {
    val vm = remember { NewsFeedViewModel(NewsRepository()) }
    LaunchedEffect(Unit) { vm.start() }

    val feed by vm.feed.collectAsState()
    val readCount by vm.readCount.collectAsState()
    val lastDetail by vm.lastDetail.collectAsState()

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Bottom sheet state
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    var selectedNews by remember { mutableStateOf<NewsUi?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Awi Live News",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    TextButton(
                        onClick = {
                            selectedCategory = null
                            vm.setCategory(null)
                        }
                    ) { Text("Reset") }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // biar rapi juga kalau layar besar
            val contentMaxWidth = if (maxWidth > 560.dp) 560.dp else maxWidth

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = contentMaxWidth)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header “koran” (garis tipis)
                NewspaperDivider()

                Spacer(Modifier.height(10.dp))

                StatsNewspaperCard(
                    readCount = readCount,
                    hintOrDetail = lastDetail
                )

                Spacer(Modifier.height(14.dp))

                Text("Kategori", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                CategoryRow(
                    selected = selectedCategory,
                    onSelect = { cat ->
                        selectedCategory = cat
                        vm.setCategory(cat)
                    }
                )

                Spacer(Modifier.height(12.dp))
                NewspaperDivider()
                Spacer(Modifier.height(12.dp))

                if (feed.isEmpty()) {
                    EmptyNewspaperCard()
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(feed, key = { it.id }) { item ->
                            NewsNewspaperCard(
                                item = item,
                                onClick = {
                                    vm.markAsRead(item.id)
                                    selectedNews = item
                                    showSheet = true
                                    scope.launch { sheetState.expand() }
                                }
                            )
                        }
                    }
                }
            }

            // Bottom Sheet
            if (showSheet && selectedNews != null) {
                ModalBottomSheet(
                    onDismissRequest = { showSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    SheetContent(
                        item = selectedNews!!,
                        detailText = lastDetail
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StatsNewspaperCard(
    readCount: Int,
    hintOrDetail: String?
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                "Read count: $readCount",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                hintOrDetail?.takeIf { it.isNotBlank() }
                    ?: "Tap berita untuk membuka detail (bottom sheet).",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryRow(
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
private fun NewsNewspaperCard(
    item: NewsUi,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryPill(item.categoryLabel)
                Spacer(Modifier.width(10.dp))
                Text(
                    item.displayTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Tap untuk mark as read + ambil detail",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategoryPill(text: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun SheetContent(
    item: NewsUi,
    detailText: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {
        // judul sheet seperti “kolom detail koran”
        Text(
            "Berita Detail",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(10.dp))
        NewspaperDivider()
        Spacer(Modifier.height(12.dp))

        Text(
            item.displayTitle,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(10.dp))

        Text(
            detailText?.takeIf { it.isNotBlank() }
                ?: "Mengambil detail berita... (async)",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun EmptyNewspaperCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.surface
    ) {
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
}

@Composable
private fun NewspaperDivider() {
    Divider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outline
    )
}
