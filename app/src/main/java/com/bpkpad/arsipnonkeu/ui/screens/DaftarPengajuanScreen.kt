package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Daftar Pengajuan Screen - List of all loan transactions
 * With search bar and filter chips for status filtering.
 * Figma: node-id=2262-1425
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DaftarPengajuanScreen(
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Menunggu", "Dipinjam", "Dikembalikan", "Ditolak", "Terlambat")

    // Sample data
    val transactions = listOf(
        TransactionData("Dinas Pendidikan", "Ahmad Fauzi", "045/SP/DinPend/2026", "23 Mei 2026", "06 Jun 2026", "Menunggu"),
        TransactionData("Dinas Kesehatan", "Siti Aminah", "112/SP/DinKes/2026", "20 Mei 2026", "03 Jun 2026", "Dipinjam"),
        TransactionData("Badan Keuangan", "Rizki Amalia", "078/SP/BK/2026", "15 Mei 2026", "22 Mei 2026", "Terlambat"),
        TransactionData("Inspektorat", "Budi Santoso", "033/SP/Insp/2026", "10 Mei 2026", "17 Mei 2026", "Dikembalikan"),
        TransactionData("Dinas Sosial", "Dewi Lestari", "067/SP/DinSos/2026", "08 Mei 2026", "22 Mei 2026", "Dikembalikan"),
        TransactionData("BPBD", "Hendra Wijaya", "091/SP/BPBD/2026", "05 Mei 2026", "19 Mei 2026", "Ditolak"),
        TransactionData("Dinas PUPR", "Andi Pratama", "102/SP/PUPR/2026", "01 Mei 2026", "15 Mei 2026", "Dikembalikan"),
        TransactionData("Dinas Pertanian", "Yusuf Maulana", "055/SP/DisPert/2026", "22 Mei 2026", "05 Jun 2026", "Menunggu"),
    )

    val filtered = if (selectedFilter == "Semua") transactions
        else transactions.filter { it.status.equals(selectedFilter, ignoreCase = true) }

    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Daftar Pengajuan",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari instansi, PIC, atau nomor surat...", fontSize = MaterialTheme.typography.bodyMedium.fontSize) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GreyText) },
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

            Spacer(modifier = Modifier.height(12.dp))

            // Filter chips
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

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    count = filtered.size,
                    key = { index -> filtered[index].nomorSurat }
                ) { index ->
                    val t = filtered[index]
                    Box(modifier = Modifier.animateItem()) {
                        CardPeminjaman(
                            instansi = t.instansi,
                            picNama = t.picNama,
                            nomorSurat = t.nomorSurat,
                            tanggalPinjam = t.tanggalPinjam,
                            tanggalKembali = t.tanggalKembali,
                            status = t.status,
                            onClick = { onCardClick("peminjaman_detail") }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

private data class TransactionData(
    val instansi: String,
    val picNama: String,
    val nomorSurat: String,
    val tanggalPinjam: String,
    val tanggalKembali: String,
    val status: String
)
