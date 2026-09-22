package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.dialogs.CodexDialog
import com.example.ui.screens.CampaignMapScreen
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MesopotamiaTheme
import com.example.viewmodel.GameViewModel

enum class AppScreen {
    SPLASH,
    MAIN_MENU,
    CAMPAIGN_MAP
}

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MesopotamiaTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isMusicMuted.value) {
            viewModel.lyrePlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.lyrePlayer.stop()
    }
}

@Composable
fun MainApp(viewModel: GameViewModel) {
    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
    var showMenuCodex by remember { mutableStateOf(false) }

    val hasSavedGame by viewModel.hasSavedGame.collectAsState()
    val savedGameSummary by viewModel.savedGameSummary.collectAsState()
    val isMusicMuted by viewModel.isMusicMuted.collectAsState()

    when (currentScreen) {
        AppScreen.SPLASH -> {
            SplashScreen(
                onProceed = { currentScreen = AppScreen.MAIN_MENU },
                modifier = Modifier.fillMaxSize()
            )
        }

        AppScreen.MAIN_MENU -> {
            MainMenuScreen(
                hasSavedGame = hasSavedGame,
                savedGameSummary = savedGameSummary,
                isMusicMuted = isMusicMuted,
                onContinueGame = {
                    if (viewModel.loadSavedCampaign()) {
                        currentScreen = AppScreen.CAMPAIGN_MAP
                    }
                },
                onStartNewGame = { factionId ->
                    viewModel.startNewCampaign(factionId)
                    currentScreen = AppScreen.CAMPAIGN_MAP
                },
                onOpenCodex = { showMenuCodex = true },
                onToggleMusic = { viewModel.toggleMusic() },
                modifier = Modifier.fillMaxSize()
            )

            if (showMenuCodex) {
                CodexDialog(onDismiss = { showMenuCodex = false })
            }
        }

        AppScreen.CAMPAIGN_MAP -> {
            CampaignMapScreen(
                viewModel = viewModel,
                onReturnToMenu = { currentScreen = AppScreen.MAIN_MENU },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
