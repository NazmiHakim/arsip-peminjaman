package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Dashboard Arsiparis Screen
 *
 * Main dashboard for the Arsiparis (Operator) role.
 * Shows greeting, statistics cards, quick actions, recent submissions,
 * and overdue alerts.
 *
 * Figma: node-id=2204-691
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardArsiparisScreen(
    onNavigate: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Peminjaman Arsip",
                onNotificationClick = { /* TODO: show notifications */ }
            )
        },
        bottomBar = {
            PeminjamanBottomNavBar(
                currentRoute = "peminjaman_dashboard",
                onNavigate = onNavigate
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Greeting Section ──────────────────────────────────
            item {
                Column {
                    Text(
                        "Selamat Datang,",
                        fontSize = 14.sp,
                        color = GreyText
                    )
                    Text(
                        "Arsiparis BPKPAD",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Kelola pengajuan dan riwayat peminjaman arsip",
                        fontSize = 13.sp,
                        color = GreyText
                    )
                }
            }

            // ── Quick Action: Ajukan Peminjaman ──────────────────
            item {
                Button(
                    onClick = { onNavigate("peminjaman_ajukan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajukan Peminjaman Baru", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // ── Statistics Cards ─────────────────────────────────
            item {
                PeminjamanSectionTitle("Ringkasan Statistik")
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        StatsCard(
                            label = "Semua\nTransaksi",
                            count = "25",
                            icon = Icons.Default.Inventory,
                            bgColor = StatusDisetujuiBg,
                            iconColor = StatusDisetujuiIcon
                        )
                    }
                    item {
                        StatsCard(
                            label = "Menunggu\nPersetujuan",
                            count = "4",
                            icon = Icons.Default.Schedule,
                            bgColor = StatusMenungguBg,
                            iconColor = StatusMenungguIcon
                        )
                    }
                    item {
                        StatsCard(
                            label = "Sedang\nDipinjam",
                            count = "8",
                            icon = Icons.Default.SwapHoriz,
                            bgColor = StatusDipinjamBg,
                            iconColor = StatusDipinjamIcon
                        )
                    }
                    item {
                        StatsCard(
                            label = "Selesai\nDikembalikan",
                            count = "11",
                            icon = Icons.Default.AssignmentReturn,
                            bgColor = StatusDikembalikanBg,
                            iconColor = StatusDikembalikanIcon
                        )
                    }
                    item {
                        StatsCard(
                            label = "Terlambat\nKembali",
                            count = "2",
                            icon = Icons.Default.Warning,
                            bgColor = StatusTerlambatBg,
                            iconColor = StatusTerlambatIcon
                        )
                    }
                }
            }

            // ── Overdue Alert ────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = StatusTerlambatBg)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = StatusTerlambatIcon,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "2 Dokumen Terlambat!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = StatusTerlambatText
                            )
                            Text(
                                "Ada dokumen yang melewati batas waktu pengembalian",
                                fontSize = 12.sp,
                                color = StatusTerlambatText.copy(alpha = 0.8f)
                            )
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = StatusTerlambatIcon)
                    }
                }
            }

            // ── Quick Actions Grid ───────────────────────────────
            item {
                PeminjamanSectionTitle("Aksi Cepat")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.QrCodeScanner,
                        label = "Scan QR\nPengembalian",
                        color = StatusDipinjamIcon
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Search,
                        label = "Cari\nDokumen",
                        color = PrimaryColor
                    )
                    QuickActionCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Business,
                        label = "Master\nInstansi",
                        color = StatusDikembalikanIcon
                    )
                }
            }

            // ── Pengajuan Terbaru ────────────────────────────────
            item {
                PeminjamanSectionTitle(
                    title = "Pengajuan Terbaru",
                    actionText = "Lihat Semua",
                    onAction = { onNavigate("peminjaman_daftar") }
                )
            }

            // Sample transaction cards
            item {
                CardPeminjaman(
                    instansi = "Dinas Pendidikan",
                    picNama = "Ahmad Fauzi",
                    nomorSurat = "045/SP/DinPend/2026",
                    tanggalPinjam = "23 Mei 2026",
                    tanggalKembali = "06 Jun 2026",
                    status = "Menunggu",
                    onClick = { onCardClick("peminjaman_detail") }
                )
            }
            item {
                CardPeminjaman(
                    instansi = "Dinas Kesehatan",
                    picNama = "Siti Aminah",
                    nomorSurat = "112/SP/DinKes/2026",
                    tanggalPinjam = "20 Mei 2026",
                    tanggalKembali = "03 Jun 2026",
                    status = "Dipinjam",
                    onClick = { onCardClick("peminjaman_detail") }
                )
            }
            item {
                CardPeminjaman(
                    instansi = "Badan Keuangan",
                    picNama = "Rizki Amalia",
                    nomorSurat = "078/SP/BK/2026",
                    tanggalPinjam = "15 Mei 2026",
                    tanggalKembali = "22 Mei 2026",
                    status = "Terlambat",
                    onClick = { onCardClick("peminjaman_detail") }
                )
            }
            item {
                CardPeminjaman(
                    instansi = "Inspektorat",
                    picNama = "Budi Santoso",
                    nomorSurat = "033/SP/Insp/2026",
                    tanggalPinjam = "10 Mei 2026",
                    tanggalKembali = "17 Mei 2026",
                    status = "Dikembalikan",
                    onClick = { onCardClick("peminjaman_detail") }
                )
            }

            // Bottom spacing
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Quick Action Card (grid item for scan QR, search, master)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText,
                lineHeight = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
