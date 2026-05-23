package com.bpkpad.arsipnonkeu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bpkpad.arsipnonkeu.ui.components.PeminjamanTopBar
import com.bpkpad.arsipnonkeu.ui.theme.*

/**
 * Ajukan Peminjaman Screen - Create new borrowing transaction form
 *
 * Based on business rules from documentation:
 * - Instansi from dropdown Master Instansi
 * - PIC name, phone (mandatory), Surat number
 * - Camera for surat pengantar photo (mandatory)
 * - Document search from Master Dokumen
 * - Tanggal Pinjam auto-fill (today, read-only)
 * - Tenggat Pengembalian via date picker
 *
 * Figma: node-id=2204-733
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjukanPeminjamanScreen(
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var instansi by remember { mutableStateOf("") }
    var picNama by remember { mutableStateOf("") }
    var picNoHp by remember { mutableStateOf("") }
    var nomorSurat by remember { mutableStateOf("") }
    var tanggalKembali by remember { mutableStateOf("") }
    var hasPhoto by remember { mutableStateOf(false) }
    var hasDocument by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PeminjamanTopBar(
                title = "Ajukan Peminjaman",
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Foto Surat Pengantar (Wajib) ─────────────────────
            FormSectionTitle("Surat Pengantar", isRequired = true)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable { hasPhoto = true },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = if (hasPhoto) PrimaryColor.copy(alpha = 0.05f) else Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(if (hasPhoto) PrimaryColor else BorderColor)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (hasPhoto) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = if (hasPhoto) PrimaryColor else GreyText,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (hasPhoto) "Foto surat berhasil diambil" else "Ketuk untuk memotret surat pengantar",
                        fontSize = 14.sp,
                        color = if (hasPhoto) PrimaryColor else GreyText,
                        fontWeight = if (hasPhoto) FontWeight.SemiBold else FontWeight.Normal
                    )
                    if (!hasPhoto) {
                        Text("Wajib dilampirkan", fontSize = 11.sp, color = StatusTerlambatText)
                    }
                }
            }

            // ── Data Pemohon ─────────────────────────────────────
            FormSectionTitle("Data Pemohon")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Instansi Dropdown
                    FormField(
                        label = "Nama Instansi",
                        value = instansi,
                        onValueChange = { instansi = it },
                        placeholder = "Pilih instansi dari master",
                        isDropdown = true,
                        isRequired = true
                    )
                    // Nama PIC
                    FormField(
                        label = "Nama PIC / Pengambil",
                        value = picNama,
                        onValueChange = { picNama = it },
                        placeholder = "Masukkan nama PIC",
                        isRequired = true
                    )
                    // No HP PIC
                    FormField(
                        label = "No. HP/WA PIC",
                        value = picNoHp,
                        onValueChange = { picNoHp = it },
                        placeholder = "Contoh: 08123456789",
                        isRequired = true
                    )
                    // Nomor Surat
                    FormField(
                        label = "Nomor Surat Pengantar",
                        value = nomorSurat,
                        onValueChange = { nomorSurat = it },
                        placeholder = "Masukkan nomor surat",
                        isRequired = true
                    )
                }
            }

            // ── Data Dokumen ─────────────────────────────────────
            FormSectionTitle("Dokumen yang Dipinjam")
            AnimatedVisibility(
                visible = hasDocument,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                // Show added document
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundColor)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.InsertDriveFile, null, tint = PrimaryColor, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("SP2D No. 0234/SP2D/2023", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkText)
                            Text("Pembayaran Gaji ASN • Rak A, Box 2023", fontSize = 11.sp, color = GreyText)
                        }
                        IconButton(onClick = { hasDocument = false }) {
                            Icon(Icons.Default.Close, null, tint = StatusTerlambatIcon, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
            OutlinedButton(
                onClick = { hasDocument = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    brush = androidx.compose.ui.graphics.SolidColor(PrimaryColor)
                ),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryColor)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Tambah Dokumen dari Master", fontWeight = FontWeight.SemiBold)
            }

            // ── Jadwal Peminjaman ────────────────────────────────
            FormSectionTitle("Jadwal Peminjaman")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    FormField(
                        label = "Tanggal Peminjaman",
                        value = "23 Mei 2026",
                        onValueChange = {},
                        placeholder = "",
                        isReadOnly = true,
                        trailingIcon = Icons.Default.CalendarToday
                    )
                    FormField(
                        label = "Tenggat Pengembalian",
                        value = tanggalKembali,
                        onValueChange = { tanggalKembali = it },
                        placeholder = "Pilih tanggal kembali",
                        isRequired = true,
                        trailingIcon = Icons.Default.CalendarToday
                    )
                }
            }

            // ── Submit Button ────────────────────────────────────
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Icon(Icons.Default.Send, null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Submit Pengajuan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ─── Helper Composables ──────────────────────────────────────────────────────

@Composable
private fun FormSectionTitle(title: String, isRequired: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
        if (isRequired) {
            Text(" *", color = StatusTerlambatIcon, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isRequired: Boolean = false,
    isReadOnly: Boolean = false,
    isDropdown: Boolean = false,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Column {
        Row {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkText)
            if (isRequired) Text(" *", color = StatusTerlambatIcon, fontSize = 13.sp)
        }
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = if (isReadOnly) { _ -> } else onValueChange,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = LightGreyText) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            readOnly = isReadOnly,
            enabled = !isReadOnly,
            trailingIcon = {
                when {
                    isDropdown -> Icon(Icons.Default.ArrowDropDown, null, tint = GreyText)
                    trailingIcon != null -> Icon(trailingIcon, null, tint = GreyText, modifier = Modifier.size(20.dp))
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryColor,
                unfocusedBorderColor = BorderColor,
                disabledBorderColor = BorderColor,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = BackgroundColor
            ),
            singleLine = true
        )
    }
}
