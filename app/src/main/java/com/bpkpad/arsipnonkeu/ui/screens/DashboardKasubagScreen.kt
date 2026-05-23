package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardKasubagScreen(
    onNavigate: (String) -> Unit = {},
    onCardClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = { PeminjamanTopBar(title = "Peminjaman Arsip") },
        bottomBar = { PeminjamanBottomNavBar(currentRoute = "peminjaman_dashboard", onNavigate = onNavigate) },
        containerColor = BackgroundColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column {
                    Text("Selamat Pagi,", fontSize = 14.sp, color = GreyText)
                    Text("Kasubag BPKPAD", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    Text("Pantau dan kelola persetujuan peminjaman arsip", fontSize = 13.sp, color = GreyText)
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = StatusMenungguBg)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).background(StatusMenungguIcon.copy(0.2f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Inbox, null, tint = StatusMenungguIcon, modifier = Modifier.size(26.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("4 Pengajuan Menunggu", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = StatusMenungguText)
                            Text("Perlu persetujuan Anda segera", fontSize = 12.sp, color = StatusMenungguText.copy(0.8f))
                        }
                        FilledTonalButton(onClick = { onNavigate("peminjaman_daftar") }, colors = ButtonDefaults.filledTonalButtonColors(containerColor = StatusMenungguIcon, contentColor = Color.White), shape = RoundedCornerShape(10.dp)) { Text("Tinjau") }
                    }
                }
            }
            item {
                PeminjamanSectionTitle("Ringkasan")
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { StatsCard("Semua\nTransaksi", "25", Icons.Default.Inventory, StatusDisetujuiBg, StatusDisetujuiIcon) }
                    item { StatsCard("Menunggu", "4", Icons.Default.Schedule, StatusMenungguBg, StatusMenungguIcon) }
                    item { StatsCard("Dipinjam", "8", Icons.Default.SwapHoriz, StatusDipinjamBg, StatusDipinjamIcon) }
                    item { StatsCard("Terlambat", "2", Icons.Default.Warning, StatusTerlambatBg, StatusTerlambatIcon) }
                }
            }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBackground), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Distribusi Per Instansi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
                        Text("Bulan Mei 2026", fontSize = 12.sp, color = GreyText)
                        Spacer(Modifier.height(20.dp))
                        Box(Modifier.size(160.dp).clip(CircleShape).background(StatusDipinjamBg), contentAlignment = Alignment.Center) {
                            Box(Modifier.size(120.dp).clip(CircleShape).background(PrimaryColor.copy(0.4f)))
                            Box(Modifier.size(80.dp).clip(CircleShape).background(StatusMenungguIcon.copy(0.5f)))
                            Box(Modifier.size(50.dp).clip(CircleShape).background(Color.White), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("25", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkText)
                                    Text("Total", fontSize = 10.sp, color = GreyText)
                                }
                            }
                        }
                    }
                }
            }
            item {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBackground), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Tren Bulanan", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                            listOf("Des" to 0.4f, "Jan" to 0.6f, "Feb" to 0.5f, "Mar" to 0.75f, "Apr" to 0.85f, "Mei" to 1f).forEach { (m, f) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(36.dp)) {
                                    Text("${(f * 25).toInt()}", fontSize = 10.sp, color = GreyText)
                                    Box(Modifier.width(24.dp).height((f * 80).dp).clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)).background(if (f >= 1f) PrimaryColor else PrimaryColor.copy(0.5f + f * 0.3f)))
                                    Text(m, fontSize = 10.sp, color = GreyText)
                                }
                            }
                        }
                    }
                }
            }
            item { PeminjamanSectionTitle("Antrean Persetujuan", "Lihat Semua") { onNavigate("peminjaman_daftar") } }
            item { CardPeminjaman("Dinas Pendidikan", "Ahmad Fauzi", "045/SP/2026", "23 Mei", "06 Jun", "Menunggu") { onCardClick("peminjaman_detail") } }
            item { CardPeminjaman("Badan Perencanaan", "Wahyu H", "089/SP/2026", "22 Mei", "05 Jun", "Menunggu") { onCardClick("peminjaman_detail") } }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}
