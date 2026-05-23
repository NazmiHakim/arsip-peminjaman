package com.bpkpad.arsipnonkeu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.bpkpad.arsipnonkeu.ui.theme.ArsipBPKADTheme
import com.bpkpad.arsipnonkeu.ui.theme.screen.add.NewRecordScreen
import com.bpkpad.arsipnonkeu.ui.theme.screen.archive.ArchiveScreen
import com.bpkpad.arsipnonkeu.ui.theme.screen.dashboard.DashboardScreen
import com.bpkpad.arsipnonkeu.ui.theme.screen.detail.DocumentDetailScreen
import com.bpkpad.arsipnonkeu.ui.theme.screen.search.SearchScreen
import com.bpkpad.arsipnonkeu.ui.screens.*

/**
 * MainActivity - Entry point of the BPKPAD Balangan application.
 *
 * Sets up the Compose content and handles basic navigation between screens.
 * Includes routing for both the base archive module (tim lain) and
 * the Peminjaman Dokumen module (our module).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArsipBPKADTheme {
                var currentRoute by remember { mutableStateOf("dashboard") }
                val backStack = remember { mutableStateListOf<String>() }

                fun navigateTo(route: String) {
                    if (currentRoute != route) {
                        backStack.add(currentRoute)
                        currentRoute = route
                    }
                }

                fun navigateBack() {
                    if (backStack.isNotEmpty()) {
                        currentRoute = backStack.removeLast()
                    }
                }

                // Handle system back button
                BackHandler(enabled = backStack.isNotEmpty()) {
                    navigateBack()
                }

                when (currentRoute) {
                    // ── Base Module Routes (Tim Lain) ────────────
                    "dashboard" -> DashboardScreen(
                        onNavItemSelected = { navigateTo(it) },
                        onArchiveYearClick = { navigateTo("archive") }
                    )
                    "archive" -> ArchiveScreen(
                        onNavItemSelected = { navigateTo(it) },
                        onDocumentClick = { navigateTo("document_detail") }
                    )
                    "search" -> SearchScreen(
                        onNavItemSelected = { navigateTo(it) },
                        onResultClick = { navigateTo("document_detail") }
                    )
                    "new_record" -> NewRecordScreen(
                        onNavItemSelected = { navigateTo(it) }
                    )
                    "profile" -> {
                        DashboardScreen(onNavItemSelected = { navigateTo(it) })
                    }
                    "document_detail" -> DocumentDetailScreen(
                        onBackClick = { navigateBack() }
                    )

                    // ── Peminjaman Module Routes (Our Module) ────
                    "peminjaman_dashboard_arsiparis", "peminjaman_dashboard" -> DashboardArsiparisScreen(
                        onNavigate = { navigateTo(it) },
                        onCardClick = { navigateTo(it) }
                    )
                    "peminjaman_dashboard_kasubag" -> DashboardKasubagScreen(
                        onNavigate = { navigateTo(it) },
                        onCardClick = { navigateTo(it) }
                    )
                    "peminjaman_daftar", "peminjaman_daftar_pengajuan" -> DaftarPengajuanScreen(
                        onBackClick = { navigateBack() },
                        onNavigate = { navigateTo(it) },
                        onCardClick = { navigateTo(it) }
                    )
                    "peminjaman_ajukan" -> AjukanPeminjamanScreen(
                        onBackClick = { navigateBack() },
                        onSubmit = {
                            // After submit, return to previous
                            navigateBack()
                        }
                    )
                    "peminjaman_detail" -> DetailPeminjamanScreen(
                        onBackClick = { navigateBack() }
                    )
                    // Riwayat — completed/closed transactions (US-13)
                    "peminjaman_riwayat" -> RiwayatPeminjamanScreen(
                        onNavigate = { navigateTo(it) },
                        onCardClick = { navigateTo(it) }
                    )
                    // Laporan — export reports to PDF/Excel (US-12, US-18)
                    "peminjaman_laporan" -> LaporanPeminjamanScreen(
                        onNavigate = { navigateTo(it) }
                    )

                    else -> DashboardScreen(onNavItemSelected = { navigateTo(it) })
                }
            }
        }
    }
}
