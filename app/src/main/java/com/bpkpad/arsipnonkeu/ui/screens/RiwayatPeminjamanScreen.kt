package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Riwayat Peminjaman Screen (US-13)
 *
 * Shows the HISTORY of all completed/closed transactions.
 * Unlike DaftarPengajuan (which shows active/pending), this screen
 * focuses on finished transactions: Dikembalikan, Ditolak, Dibatalkan.
 *
 * Features:
 * - Date range filter (Dari – Sampai)
 * - Status filter chips (Semua, Dikembalikan, Ditolak, Dibatalkan)
 * - Search by instansi name
 * - Summary statistics header
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatPeminjamanScreen(
    onNavigate: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    var showDateFilter by remember { mutableStateOf(false) }
    var dateFromText by remember { mutableStateOf("01 Mei 2026") }
    var dateToText by remember { mutableStateOf("23 Mei 2026") }

    val filters = listOf("Semua", "Dikembalikan", "Ditolak", "Dibatalkan")

    // Historical (completed) transactions
    val allHistory = listOf(
        HistoryData("Inspektorat", "Budi Santoso", "033/SP/Insp/2026", "10 Mei 2026", "17 Mei 2026", "16 Mei 2026", "Dikembalikan"),
        HistoryData("Dinas Sosial", "Dewi Lestari", "067/SP/DinSos/2026", "08 Mei 2026", "22 Mei 2026", "20 Mei 2026", "Dikembalikan"),
        HistoryData("Dinas PUPR", "Andi Pratama", "102/SP/PUPR/2026", "01 Mei 2026", "15 Mei 2026", "14 Mei 2026", "Dikembalikan"),
        HistoryData("BPBD", "Hendra Wijaya", "091/SP/BPBD/2026", "05 Mei 2026", "19 Mei 2026", "-", "Ditolak"),
        HistoryData("Dinas Perhubungan", "Rini Susanti", "044/SP/Dishub/2026", "03 Mei 2026", "17 Mei 2026", "-", "Dibatalkan"),
        HistoryData("Bappeda", "Slamet Riyadi", "021/SP/Bappeda/2026", "25 Apr 2026", "09 Mei 2026", "08 Mei 2026", "Dikembalikan"),
        HistoryData("Dinas Lingkungan", "Mega Putri", "088/SP/DLH/2026", "20 Apr 2026", "04 Mei 2026", "04 Mei 2026", "Dikembalikan"),
        HistoryData("Sekretariat DPRD", "Yanto Supriyadi", "015/SP/Setwan/2026", "15 Apr 2026", "29 Apr 2026", "-", "Ditolak"),
    )

    val filtered = allHistory
        .filter { item ->
            if (selectedFilter == "Semua") true
            else item.status.equals(selectedFilter, ignoreCase = true)
        }
        .filter { item ->
            if (searchQuery.isBlank()) true
            else item.instansi.contains(searchQuery, ignoreCase = true) ||
                    item.picNama.contains(searchQuery, ignoreCase = true) ||
                    item.nomorSurat.contains(searchQuery, ignoreCase = true)
        }

    // Stats
    val totalDikembalikan = allHistory.count { it.status == "Dikembalikan" }
    val totalDitolak = allHistory.count { it.status == "Ditolak" }
    val totalDibatalkan = allHistory.count { it.status == "Dibatalkan" }

    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Peminjaman Arsip",
                onNotificationClick = { /* TODO */ }
            )
        },
        bottomBar = {
            PeminjamanBottomNavBar(
                currentRoute = "peminjaman_riwayat",
                onNavigate = onNavigate
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // ── Header ───────────────────────────────────────────
            item {
                Column {
                    Text(
                        "Riwayat Peminjaman",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Catatan lengkap seluruh transaksi yang sudah selesai",
                        fontSize = 13.sp,
                        color = GreyText
                    )
                }
            }

            // ── Summary Stats Row ────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        count = "$totalDikembalikan",
                        label = "Dikembalikan",
                        bgColor = StatusDikembalikanBg,
                        textColor = StatusDikembalikanText,
                        icon = Icons.Default.AssignmentReturn
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        count = "$totalDitolak",
                        label = "Ditolak",
                        bgColor = StatusDitolakBg,
                        textColor = StatusDitolakText,
                        icon = Icons.Default.Cancel
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        count = "$totalDibatalkan",
                        label = "Dibatalkan",
                        bgColor = StatusDibatalkanBg,
                        textColor = StatusDibatalkanText,
                        icon = Icons.Default.Block
                    )
                }
            }

            // ── Search ───────────────────────────────────────────
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "Cari instansi, PIC, atau nomor surat...",
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GreyText)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryColor,
                        unfocusedBorderColor = BorderColor,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // ── Date Range Toggle ────────────────────────────────
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDateFilter = !showDateFilter },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder(enabled = true).copy(
                        brush = androidx.compose.ui.graphics.SolidColor(BorderColor)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.DateRange, null, tint = PrimaryColor, modifier = Modifier.size(20.dp))
                            Column {
                                Text("Rentang Tanggal", fontSize = 12.sp, color = GreyText)
                                Text(
                                    "$dateFromText — $dateToText",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = DarkText
                                )
                            }
                        }
                        Icon(
                            if (showDateFilter) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = GreyText
                        )
                    }
                }

                // Expandable date picker fields
                AnimatedVisibility(
                    visible = showDateFilter,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Dari Tanggal", fontSize = 12.sp, color = GreyText)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = dateFromText,
                                onValueChange = { dateFromText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = PrimaryColor, modifier = Modifier.size(18.dp)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = BorderColor
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Sampai Tanggal", fontSize = 12.sp, color = GreyText)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = dateToText,
                                onValueChange = { dateToText = it },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                leadingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = PrimaryColor, modifier = Modifier.size(18.dp)) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = BorderColor
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { showDateFilter = false },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                            ) {
                                Icon(Icons.Default.FilterAlt, null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Terapkan Filter", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // ── Filter Chips ─────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChipRow(
                        filters = filters,
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it }
                    )
                }
            }

            // ── Result Count ─────────────────────────────────────
            item {
                Text(
                    "${filtered.size} transaksi ditemukan",
                    fontSize = 12.sp,
                    color = GreyText,
                    fontWeight = FontWeight.Medium
                )
            }

            // ── History Cards ────────────────────────────────────
            if (filtered.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.SearchOff,
                                null,
                                tint = GreyText,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Tidak ada riwayat ditemukan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkText
                            )
                            Text(
                                "Coba ubah filter atau kata kunci pencarian",
                                fontSize = 12.sp,
                                color = GreyText,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    count = filtered.size,
                    key = { index -> filtered[index].nomorSurat }
                ) { index ->
                    val item = filtered[index]
                    HistoryCard(
                        instansi = item.instansi,
                        picNama = item.picNama,
                        nomorSurat = item.nomorSurat,
                        tanggalPinjam = item.tanggalPinjam,
                        tanggalKembaliRencana = item.tanggalKembaliRencana,
                        tanggalKembaliAktual = item.tanggalKembaliAktual,
                        status = item.status,
                        onClick = { onCardClick("peminjaman_detail") }
                    )
                }
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// History Card — differs from CardPeminjaman by showing actual return date
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HistoryCard(
    instansi: String,
    picNama: String,
    nomorSurat: String,
    tanggalPinjam: String,
    tanggalKembaliRencana: String,
    tanggalKembaliAktual: String,
    status: String,
    onClick: () -> Unit = {}
) {
    val style = getStatusStyle(status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(style.bg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(style.icon, null, tint = style.text, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(instansi, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
                        Text("PIC: $picNama", fontSize = 12.sp, color = GreyText)
                    }
                }
                StatusChip(status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            // Details — 2 rows for history
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("No. Surat", fontSize = 10.sp, color = GreyText)
                    Text(nomorSurat, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tgl Pinjam", fontSize = 10.sp, color = GreyText)
                    Text(tanggalPinjam, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Rencana Kembali", fontSize = 10.sp, color = GreyText)
                    Text(tanggalKembaliRencana, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Aktual Kembali", fontSize = 10.sp, color = GreyText)
                    Text(
                        tanggalKembaliAktual,
                        fontSize = 12.sp,
                        color = if (tanggalKembaliAktual == "-") GreyText else PrimaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Summary Mini Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SummaryMiniCard(
    modifier: Modifier = Modifier,
    count: String,
    label: String,
    bgColor: Color,
    textColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, null, tint = textColor, modifier = Modifier.size(18.dp))
            Column {
                Text(count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                Text(label, fontSize = 10.sp, color = textColor.copy(alpha = 0.8f))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Data class
// ─────────────────────────────────────────────────────────────────────────────
private data class HistoryData(
    val instansi: String,
    val picNama: String,
    val nomorSurat: String,
    val tanggalPinjam: String,
    val tanggalKembaliRencana: String,
    val tanggalKembaliAktual: String,
    val status: String
)
