package com.bpkpad.arsipnonkeu.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.theme.*

// ─────────────────────────────────────────────────────────────────────────────
// Status Utility
// ─────────────────────────────────────────────────────────────────────────────
data class StatusStyle(val bg: Color, val text: Color, val icon: ImageVector, val label: String)

fun getStatusStyle(status: String): StatusStyle {
    return when (status.lowercase()) {
        "menunggu", "menunggu_persetujuan", "pending" -> StatusStyle(StatusMenungguBg, StatusMenungguText, Icons.Default.Schedule, "Menunggu")
        "disetujui", "approved" -> StatusStyle(StatusDisetujuiBg, StatusDisetujuiText, Icons.Default.CheckCircle, "Disetujui")
        "dipinjam", "borrowed" -> StatusStyle(StatusDipinjamBg, StatusDipinjamText, Icons.Default.SwapHoriz, "Dipinjam")
        "dikembalikan", "returned" -> StatusStyle(StatusDikembalikanBg, StatusDikembalikanText, Icons.Default.AssignmentReturn, "Dikembalikan")
        "ditolak", "rejected" -> StatusStyle(StatusDitolakBg, StatusDitolakText, Icons.Default.Cancel, "Ditolak")
        "dibatalkan", "cancelled" -> StatusStyle(StatusDibatalkanBg, StatusDibatalkanText, Icons.Default.Block, "Dibatalkan")
        "terlambat", "overdue" -> StatusStyle(StatusTerlambatBg, StatusTerlambatText, Icons.Default.Warning, "Terlambat")
        else -> StatusStyle(StatusMenungguBg, StatusMenungguText, Icons.Default.Schedule, status)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Status Chip
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun StatusChip(status: String) {
    val style = getStatusStyle(status)
    Box(
        modifier = Modifier
            .background(style.bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = style.icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = style.text
            )
            Text(
                text = style.label,
                fontSize = 11.sp,
                color = style.text,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Card Peminjaman (Transaction Card)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun CardPeminjaman(
    instansi: String,
    picNama: String,
    nomorSurat: String,
    tanggalPinjam: String,
    tanggalKembali: String,
    status: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Instansi + Status
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
                            .background(PrimaryColor.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = PrimaryColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = instansi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "PIC: $picNama",
                            fontSize = 12.sp,
                            color = GreyText
                        )
                    }
                }
                StatusChip(status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            // Detail row
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("No. Surat", fontSize = 10.sp, color = GreyText)
                    Text(nomorSurat, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tgl Pinjam", fontSize = 10.sp, color = GreyText)
                    Text(tanggalPinjam, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tgl Kembali", fontSize = 10.sp, color = GreyText)
                    Text(tanggalKembali, fontSize = 12.sp, color = DarkText, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Statistics Card
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun StatsCard(
    label: String,
    count: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .height(130.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.6f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(count, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkText)
                Text(label, fontSize = 11.sp, color = GreyText, maxLines = 2, lineHeight = 14.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Section Title
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PeminjamanSectionTitle(title: String, actionText: String? = null, onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )
        if (actionText != null) {
            TextButton(onClick = onAction) {
                Text(actionText, color = PrimaryColor, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PrimaryColor, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Bottom Navigation Bar for Peminjaman Module
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PeminjamanBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceColor,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "peminjaman_dashboard",
            onClick = { onNavigate("peminjaman_dashboard") },
            icon = {
                Icon(
                    if (currentRoute == "peminjaman_dashboard") Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Dashboard"
                )
            },
            label = { Text("Dashboard", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                selectedTextColor = PrimaryColor,
                indicatorColor = PrimaryColor.copy(alpha = 0.12f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "peminjaman_daftar",
            onClick = { onNavigate("peminjaman_daftar") },
            icon = {
                Icon(
                    if (currentRoute == "peminjaman_daftar") Icons.Filled.ListAlt else Icons.Outlined.ListAlt,
                    contentDescription = "Daftar"
                )
            },
            label = { Text("Daftar", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                selectedTextColor = PrimaryColor,
                indicatorColor = PrimaryColor.copy(alpha = 0.12f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "peminjaman_ajukan",
            onClick = { onNavigate("peminjaman_ajukan") },
            icon = {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(PrimaryColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajukan", tint = Color.White, modifier = Modifier.size(24.dp))
                }
            },
            label = { Text("Ajukan", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = PrimaryColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = currentRoute == "peminjaman_riwayat",
            onClick = { onNavigate("peminjaman_riwayat") },
            icon = {
                Icon(
                    if (currentRoute == "peminjaman_riwayat") Icons.Filled.History else Icons.Outlined.History,
                    contentDescription = "Riwayat"
                )
            },
            label = { Text("Riwayat", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                selectedTextColor = PrimaryColor,
                indicatorColor = PrimaryColor.copy(alpha = 0.12f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "peminjaman_laporan",
            onClick = { onNavigate("peminjaman_laporan") },
            icon = {
                Icon(
                    if (currentRoute == "peminjaman_laporan") Icons.Filled.Assessment else Icons.Outlined.Assessment,
                    contentDescription = "Laporan"
                )
            },
            label = { Text("Laporan", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryColor,
                selectedTextColor = PrimaryColor,
                indicatorColor = PrimaryColor.copy(alpha = 0.12f)
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top Bar for Peminjaman Module
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeminjamanTopBar(
    title: String = "Peminjaman Arsip",
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            if (!showBack) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(PrimaryColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
                        Text("BPKPAD Balangan", fontSize = 11.sp, color = GreyText)
                    }
                }
            } else {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText)
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DarkText)
                }
            }
        },
        actions = {
            if (!showBack) {
                IconButton(onClick = onNotificationClick) {
                    BadgedBox(badge = {
                        Badge(containerColor = StatusTerlambatIcon) {
                            Text("3", color = Color.White, fontSize = 10.sp)
                        }
                    }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifikasi", tint = DarkText)
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SurfaceColor,
            scrolledContainerColor = SurfaceColor
        )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Audit Trail Item
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AuditTrailItem(
    aksi: String,
    detail: String,
    user: String,
    timestamp: String,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Timeline indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(PrimaryColor, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp)
                        .background(DividerColor)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(aksi, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DarkText)
            Text(detail, fontSize = 12.sp, color = GreyText, lineHeight = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text("$user • $timestamp", fontSize = 11.sp, color = LightGreyText)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Filter Chip Row
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun FilterChipRow(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PrimaryColor,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = GreyText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = BorderColor,
                    selectedBorderColor = PrimaryColor,
                    enabled = true,
                    selected = isSelected
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Detail Info Row (for detail screen)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = GreyText, modifier = Modifier.weight(1f))
        Text(
            value,
            fontSize = 13.sp,
            color = DarkText,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.5f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Document Item Card (for detail screen document list)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun DocumentItemCard(
    nomorDokumen: String,
    perihal: String,
    tahun: String,
    lokasiRak: String,
    kondisi: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(PrimaryColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.InsertDriveFile, contentDescription = null, tint = PrimaryColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(nomorDokumen, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkText)
                Text(perihal, fontSize = 12.sp, color = GreyText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Tahun $tahun • $lokasiRak", fontSize = 11.sp, color = LightGreyText)
            }
            if (kondisi != null) {
                StatusChip(kondisi)
            }
        }
    }
}

// Legacy CardPeminjaman compatibility (redirect to new version)
@Composable
fun CardPeminjaman(
    title: String,
    nomorSk: String,
    tanggal: String,
    status: String
) {
    CardPeminjaman(
        instansi = title,
        picNama = "PIC",
        nomorSurat = nomorSk,
        tanggalPinjam = tanggal,
        tanggalKembali = "-",
        status = status
    )
}

// Legacy BottomNavBar compatibility
@Composable
fun BottomNavBar(currentRoute: String) {
    PeminjamanBottomNavBar(currentRoute = currentRoute, onNavigate = {})
}
