package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.*
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Detail Peminjaman Screen
 *
 * Shows complete transaction detail including:
 * - Status badge
 * - Borrower info (instansi, PIC, phone, surat)
 * - Document list
 * - Schedule (tanggal pinjam/kembali)
 * - Audit trail timeline
 * - Action buttons based on status
 *
 * Figma: node-id=2286-674
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPeminjamanScreen(
    onBackClick: () -> Unit = {}
) {
    // Sample data - in real app this comes from ViewModel
    val status = "Dipinjam"

    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Detail Peminjaman",
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Status Header Card ───────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Transaksi #TRX-2026-045", fontSize = 12.sp, color = GreyText)
                            Text("Dinas Pendidikan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkText)
                        }
                        StatusChip(status)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            Text("Dibuat oleh", fontSize = 11.sp, color = GreyText)
                            Text("Arsiparis Ahmad", fontSize = 13.sp, color = DarkText, fontWeight = FontWeight.Medium)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Disetujui oleh", fontSize = 11.sp, color = GreyText)
                            Text("Kasubag Nugroho", fontSize = 13.sp, color = DarkText, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // ── Informasi Peminjam ───────────────────────────────
            SectionHeader("Informasi Peminjam", Icons.Default.Person)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    DetailInfoRow("Nama Instansi", "Dinas Pendidikan Kab. Balangan")
                    DetailInfoRow("Nama PIC", "Ahmad Fauzi")
                    DetailInfoRow("No. HP/WA PIC", "081234567890")
                    DetailInfoRow("No. Surat Pengantar", "045/SP/DinPend/V/2026")
                    DetailInfoRow("Metode Persetujuan", "Online (via Aplikasi)")
                }
            }

            // ── Daftar Dokumen ────────────────────────────────────
            SectionHeader("Dokumen Dipinjam", Icons.Default.Folder)
            DocumentItemCard(
                nomorDokumen = "SP2D No. 0234/SP2D/2023",
                perihal = "Pembayaran Gaji ASN Bulan Desember",
                tahun = "2023",
                lokasiRak = "Rak A, Box 2023"
            )
            DocumentItemCard(
                nomorDokumen = "SP2D No. 0235/SP2D/2023",
                perihal = "Tunjangan Kinerja Q4",
                tahun = "2023",
                lokasiRak = "Rak A, Box 2023"
            )

            // ── Jadwal Peminjaman ────────────────────────────────
            SectionHeader("Jadwal Peminjaman", Icons.Default.CalendarMonth)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    DetailInfoRow("Tanggal Pinjam", "23 Mei 2026")
                    DetailInfoRow("Tenggat Kembali", "06 Juni 2026")
                    DetailInfoRow("Sisa Waktu", "14 hari lagi")
                }
            }

            // ── Foto Surat Pengantar ─────────────────────────────
            SectionHeader("Lampiran Surat", Icons.Default.Image)
            Card(
                modifier = Modifier.fillMaxWidth().height(180.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColor)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Image, null, tint = GreyText, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Foto Surat Pengantar", fontSize = 13.sp, color = GreyText)
                        Text("Ketuk untuk memperbesar", fontSize = 11.sp, color = LightGreyText)
                    }
                }
            }

            // ── Audit Trail ──────────────────────────────────────
            SectionHeader("Riwayat Aktivitas", Icons.Default.Timeline)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AuditTrailItem(
                        aksi = "Dokumen Diserahkan",
                        detail = "Dokumen fisik diserahkan ke PIC peminjam",
                        user = "Arsiparis Ahmad",
                        timestamp = "23 Mei 2026, 10:30"
                    )
                    AuditTrailItem(
                        aksi = "Disetujui Online",
                        detail = "Pengajuan disetujui oleh Kasubag. QR Code di-generate.",
                        user = "Kasubag Nugroho",
                        timestamp = "23 Mei 2026, 09:45"
                    )
                    AuditTrailItem(
                        aksi = "Pengajuan Dikirim",
                        detail = "Transaksi dikirim untuk persetujuan Kasubag",
                        user = "Arsiparis Ahmad",
                        timestamp = "23 Mei 2026, 09:15"
                    )
                    AuditTrailItem(
                        aksi = "Transaksi Dibuat",
                        detail = "Transaksi peminjaman baru dibuat",
                        user = "Arsiparis Ahmad",
                        timestamp = "23 Mei 2026, 09:00",
                        isLast = true
                    )
                }
            }

            // ── Action Buttons ───────────────────────────────────
            Spacer(Modifier.height(4.dp))

            // WhatsApp button (always visible for Arsiparis on active transactions)
            OutlinedButton(
                onClick = { /* Open WhatsApp Intent */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF25D366)),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF25D366))
                )
            ) {
                Icon(Icons.Outlined.Chat, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Hubungi PIC via WhatsApp", fontWeight = FontWeight.SemiBold)
            }

            // Status-dependent action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Perpanjang button
                OutlinedButton(
                    onClick = { /* TODO: perpanjangan */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusDipinjamIcon),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = androidx.compose.ui.graphics.SolidColor(StatusDipinjamIcon)
                    )
                ) {
                    Icon(Icons.Default.Update, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Perpanjang", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }

                // Selesaikan button
                Button(
                    onClick = { /* TODO: selesaikan peminjaman */ },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                ) {
                    Icon(Icons.Default.AssignmentReturn, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Selesaikan", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, tint = PrimaryColor, modifier = Modifier.size(20.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
    }
}
