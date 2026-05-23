package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Laporan Peminjaman Screen (US-12 & US-18)
 *
 * Export screen for generating monthly lending reports in PDF/Excel.
 * This is NOT a list view — it is a report configuration form.
 *
 * Features:
 * - Month & Year picker
 * - Status filter (optional)
 * - Instansi filter (optional)
 * - Preview summary statistics
 * - Export to PDF button
 * - Export to Excel button
 *
 * Report columns (per spec):
 * No, Tanggal Pinjam, Instansi Peminjam, Nama PIC,
 * Nomor/Perihal Dokumen, Batas Waktu, Tanggal Kembali Aktual, Kondisi Fisik
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanPeminjamanScreen(
    onNavigate: (String) -> Unit = {}
) {
    var selectedMonth by remember { mutableStateOf("Mei") }
    var selectedYear by remember { mutableStateOf("2026") }
    var selectedStatus by remember { mutableStateOf("Semua Status") }
    var selectedInstansi by remember { mutableStateOf("Semua Instansi") }
    var expandedMonth by remember { mutableStateOf(false) }
    var expandedYear by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedInstansi by remember { mutableStateOf(false) }
    var showPreview by remember { mutableStateOf(false) }

    val months = listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember")
    val years = listOf("2024", "2025", "2026")
    val statuses = listOf("Semua Status", "Dikembalikan", "Dipinjam", "Terlambat", "Ditolak", "Dibatalkan")
    val instansiList = listOf("Semua Instansi", "Dinas Pendidikan", "Dinas Kesehatan",
        "Inspektorat", "Dinas Sosial", "BPBD", "Dinas PUPR", "Badan Keuangan",
        "Bappeda", "Dinas Pertanian", "Dinas Perhubungan", "Sekretariat DPRD", "Dinas Lingkungan")

    // Preview dummy stats
    val previewTotal = 25
    val previewDikembalikan = 11
    val previewDipinjam = 8
    val previewTerlambat = 2
    val previewDitolak = 3
    val previewDibatalkan = 1

    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Peminjaman Arsip",
                onNotificationClick = { /* TODO */ }
            )
        },
        bottomBar = {
            PeminjamanBottomNavBar(
                currentRoute = "peminjaman_laporan",
                onNavigate = onNavigate
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Header ───────────────────────────────────────────
            Column {
                Text(
                    "Laporan Peminjaman",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Ekspor rekapitulasi peminjaman ke PDF atau Excel",
                    fontSize = 13.sp,
                    color = GreyText
                )
            }

            // ── Report Format Info ───────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = StatusDisetujuiBg)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, null, tint = StatusDisetujuiIcon, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            "Format Kolom Laporan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            "No • Tanggal Pinjam • Instansi • Nama PIC • Nomor/Perihal Dokumen • Batas Waktu • Tgl Kembali Aktual • Kondisi Fisik",
                            fontSize = 11.sp,
                            color = PrimaryDark.copy(alpha = 0.75f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // ── Filter Configuration Card ────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Section title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, null, tint = PrimaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Konfigurasi Laporan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                    }

                    HorizontalDivider(color = DividerColor)

                    // Month & Year in a row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Month dropdown
                        ExposedDropdownMenuBox(
                            expanded = expandedMonth,
                            onExpandedChange = { expandedMonth = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedMonth,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Bulan", fontSize = 12.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = BorderColor
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMonth,
                                onDismissRequest = { expandedMonth = false }
                            ) {
                                months.forEach { month ->
                                    DropdownMenuItem(
                                        text = { Text(month) },
                                        onClick = {
                                            selectedMonth = month
                                            expandedMonth = false
                                        }
                                    )
                                }
                            }
                        }

                        // Year dropdown
                        ExposedDropdownMenuBox(
                            expanded = expandedYear,
                            onExpandedChange = { expandedYear = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = selectedYear,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tahun", fontSize = 12.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedYear) },
                                modifier = Modifier.menuAnchor(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryColor,
                                    unfocusedBorderColor = BorderColor
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedYear,
                                onDismissRequest = { expandedYear = false }
                            ) {
                                years.forEach { year ->
                                    DropdownMenuItem(
                                        text = { Text(year) },
                                        onClick = {
                                            selectedYear = year
                                            expandedYear = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Status dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedStatus,
                        onExpandedChange = { expandedStatus = it }
                    ) {
                        OutlinedTextField(
                            value = selectedStatus,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Filter Status", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.FilterList, null, tint = GreyText, modifier = Modifier.size(18.dp)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStatus,
                            onDismissRequest = { expandedStatus = false }
                        ) {
                            statuses.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        selectedStatus = status
                                        expandedStatus = false
                                    }
                                )
                            }
                        }
                    }

                    // Instansi dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedInstansi,
                        onExpandedChange = { expandedInstansi = it }
                    ) {
                        OutlinedTextField(
                            value = selectedInstansi,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Filter Instansi", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Business, null, tint = GreyText, modifier = Modifier.size(18.dp)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedInstansi) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryColor,
                                unfocusedBorderColor = BorderColor
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedInstansi,
                            onDismissRequest = { expandedInstansi = false }
                        ) {
                            instansiList.forEach { instansi ->
                                DropdownMenuItem(
                                    text = { Text(instansi) },
                                    onClick = {
                                        selectedInstansi = instansi
                                        expandedInstansi = false
                                    }
                                )
                            }
                        }
                    }

                    // Preview button
                    OutlinedButton(
                        onClick = { showPreview = !showPreview },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryColor),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = androidx.compose.ui.graphics.SolidColor(PrimaryColor)
                        )
                    ) {
                        Icon(Icons.Default.Preview, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (showPreview) "Sembunyikan Pratinjau" else "Pratinjau Data",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ── Preview Section ──────────────────────────────────
            AnimatedVisibility(
                visible = showPreview,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.BarChart, null, tint = PrimaryColor, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Pratinjau: $selectedMonth $selectedYear",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = DarkText
                            )
                        }

                        HorizontalDivider(color = DividerColor)

                        // Stats grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PreviewStatItem(Modifier.weight(1f), "Total", "$previewTotal", PrimaryColor)
                            PreviewStatItem(Modifier.weight(1f), "Kembali", "$previewDikembalikan", StatusDikembalikanIcon)
                            PreviewStatItem(Modifier.weight(1f), "Dipinjam", "$previewDipinjam", StatusDipinjamIcon)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PreviewStatItem(Modifier.weight(1f), "Terlambat", "$previewTerlambat", StatusTerlambatIcon)
                            PreviewStatItem(Modifier.weight(1f), "Ditolak", "$previewDitolak", StatusDitolakIcon)
                            PreviewStatItem(Modifier.weight(1f), "Batal", "$previewDibatalkan", StatusDibatalkanIcon)
                        }

                        HorizontalDivider(color = DividerColor)

                        // Top borrowers
                        Text("Peminjam Terbanyak", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                        TopBorrowerRow("1", "Dinas Pendidikan", "5 transaksi")
                        TopBorrowerRow("2", "Inspektorat", "4 transaksi")
                        TopBorrowerRow("3", "Dinas Kesehatan", "3 transaksi")
                    }
                }
            }

            // ── Export Buttons ────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FileDownload, null, tint = PrimaryColor, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ekspor Laporan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                    }

                    HorizontalDivider(color = DividerColor)

                    // PDF Button
                    Button(
                        onClick = { /* TODO: Generate PDF */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "Ekspor sebagai PDF",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "Laporan_Peminjaman_${selectedMonth}_$selectedYear.pdf",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Excel Button
                    Button(
                        onClick = { /* TODO: Generate Excel */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(Icons.Default.TableChart, null, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "Ekspor sebagai Excel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                "Laporan_Peminjaman_${selectedMonth}_$selectedYear.xlsx",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview Stat Item
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PreviewStatItem(
    modifier: Modifier = Modifier,
    label: String,
    count: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 10.sp, color = color.copy(alpha = 0.8f))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top Borrower Row
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun TopBorrowerRow(rank: String, instansi: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(PrimaryColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(rank, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryColor)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(instansi, fontSize = 13.sp, color = DarkText, modifier = Modifier.weight(1f))
        Text(count, fontSize = 12.sp, color = GreyText, fontWeight = FontWeight.Medium)
    }
}
