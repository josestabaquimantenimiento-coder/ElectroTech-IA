package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.IndustrialHeader
import com.example.ui.screens.AddInterventionSheet
import com.example.ui.screens.AiMemoriesScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.NotebooksScreen
import com.example.ui.screens.RagKnowledgeScreen
import com.example.ui.screens.VisionDiagnosisScreen
import com.example.ui.screens.WebSearchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ElectroTechApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ElectroTechApp(viewModel: MainViewModel) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val showAddSheet by viewModel.showAddInterventionSheet.collectAsState()
    val editingIntervention by viewModel.editingIntervention.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val navItems = listOf(
        NavigationItemData("Cuadernos", Icons.Filled.Folder, Icons.Outlined.Folder, "tab_notebooks"),
        NavigationItemData("Visión", Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt, "tab_vision"),
        NavigationItemData("Manuales", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_rag"),
        NavigationItemData("Memorias", Icons.Filled.Psychology, Icons.Outlined.Psychology, "tab_memories"),
        NavigationItemData("Historial", Icons.Filled.History, Icons.Outlined.History, "tab_history")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            IndustrialHeader(
                title = "ElectroTech AI",
                subtitle = "NotebookLM & Diagnóstico Industrial"
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(0.dp))
            ) {
                navItems.forEachIndexed { index, item ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedTab(index) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 10.sp
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag(item.testTag)
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> NotebooksScreen(viewModel = viewModel)
                    1 -> VisionDiagnosisScreen(
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState
                    )
                    2 -> RagKnowledgeScreen(viewModel = viewModel)
                    3 -> AiMemoriesScreen(viewModel = viewModel)
                    4 -> HistoryScreen(viewModel = viewModel)
                    5 -> WebSearchScreen(viewModel = viewModel)
                }
            }
        }
    }

    if (showAddSheet) {
        AddInterventionSheet(
            viewModel = viewModel,
            existingIntervention = editingIntervention,
            onDismiss = { viewModel.closeInterventionSheet() }
        )
    }
}

data class NavigationItemData(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTag: String
)
