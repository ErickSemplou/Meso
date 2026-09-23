package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.components.RomeCommandBar
import com.example.ui.components.RomeTopBar
import com.example.ui.components.StrategicMapCanvas
import com.example.ui.dialogs.BattleResultDialog
import com.example.ui.dialogs.CampaignGameOverDialog
import com.example.ui.dialogs.ChronicleDialog
import com.example.ui.dialogs.CityManageDialog
import com.example.ui.dialogs.CodexDialog
import com.example.ui.dialogs.DiplomacyDialog
import com.example.ui.dialogs.EventDialog
import com.example.ui.dialogs.RecruitmentDialog
import com.example.ui.dialogs.TechTreeDialog
import com.example.ui.theme.AncientParchment
import com.example.viewmodel.GameViewModel

@Composable
fun CampaignMapScreen(
    viewModel: GameViewModel,
    onReturnToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.gameState.collectAsState()
    val selectedCityId by viewModel.selectedCityId.collectAsState()
    val isMusicMuted by viewModel.isMusicMuted.collectAsState()
    val battleResult by viewModel.battleResultDialog.collectAsState()

    val selectedCity = state.cities.find { it.id == selectedCityId }

    // Dialog visibility states
    var showBuildingDialog by remember { mutableStateOf(false) }
    var showRecruitmentDialog by remember { mutableStateOf(false) }
    var showDiplomacyDialog by remember { mutableStateOf(false) }
    var showTechDialog by remember { mutableStateOf(false) }
    var showCodexDialog by remember { mutableStateOf(false) }
    var showChronicleDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RomeTopBar(
                state = state,
                isMusicMuted = isMusicMuted,
                onToggleMusic = { viewModel.toggleMusic() },
                onOpenCodex = { showCodexDialog = true },
                onOpenChronicle = { showChronicleDialog = true }
            )
        },
        bottomBar = {
            RomeCommandBar(
                selectedCity = selectedCity,
                playerFactionId = state.playerFactionId,
                currentTechId = state.currentTechId,
                currentTechTurnsRemaining = state.currentTechTurnsRemaining,
                onOpenBuildingDialog = { showBuildingDialog = true },
                onOpenRecruitmentDialog = { showRecruitmentDialog = true },
                onOpenDiplomacyDialog = { showDiplomacyDialog = true },
                onOpenTechDialog = { showTechDialog = true },
                onAttackCity = {
                    selectedCityId?.let { viewModel.launchMilitaryCampaign(it) }
                },
                onEndTurn = { viewModel.endTurn() }
            )
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("campaign_map_screen")
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(AncientParchment)
                .padding(2.dp)
        ) {
            // Interactive Rome Total War Campaign Map
            StrategicMapCanvas(
                cities = state.cities,
                selectedCityId = selectedCityId,
                playerFactionId = state.playerFactionId,
                onCitySelected = { viewModel.selectCity(it) }
            )
        }
    }

    // Modal Dialogs
    if (showBuildingDialog && selectedCity != null) {
        CityManageDialog(
            city = selectedCity,
            resources = state.resources,
            onConstructBuilding = { buildingId ->
                viewModel.startBuildingConstruction(selectedCity.id, buildingId)
                showBuildingDialog = false
            },
            onDismiss = { showBuildingDialog = false }
        )
    }

    if (showRecruitmentDialog && selectedCity != null) {
        RecruitmentDialog(
            city = selectedCity,
            resources = state.resources,
            unlockedTechIds = state.researchedTechIds,
            onRecruitUnit = { unitId ->
                viewModel.recruitUnit(selectedCity.id, unitId)
                showRecruitmentDialog = false
            },
            onDismiss = { showRecruitmentDialog = false }
        )
    }

    if (showDiplomacyDialog) {
        DiplomacyDialog(
            playerFactionId = state.playerFactionId,
            relations = state.relations,
            resources = state.resources,
            onSendGift = { viewModel.sendDiplomaticGift(it) },
            onProposePeace = { viewModel.proposePeace(it) },
            onProposeTrade = { viewModel.proposeTrade(it) },
            onDeclareWar = { viewModel.declareWar(it) },
            onDismiss = { showDiplomacyDialog = false }
        )
    }

    if (showTechDialog) {
        TechTreeDialog(
            researchedTechIds = state.researchedTechIds,
            currentTechId = state.currentTechId,
            currentTechTurnsRemaining = state.currentTechTurnsRemaining,
            onStartResearch = { techId ->
                viewModel.startResearch(techId)
                showTechDialog = false
            },
            onDismiss = { showTechDialog = false }
        )
    }

    if (showCodexDialog) {
        CodexDialog(onDismiss = { showCodexDialog = false })
    }

    if (showChronicleDialog) {
        ChronicleDialog(logs = state.chronicleLog, onDismiss = { showChronicleDialog = false })
    }

    // Narrative Historical Event Dialog
    if (state.activeEvent != null) {
        EventDialog(
            event = state.activeEvent!!,
            onOptionSelected = { optionIndex ->
                viewModel.resolveEventOption(optionIndex)
            }
        )
    }

    // Battle Result Dialog
    if (battleResult != null) {
        BattleResultDialog(
            result = battleResult!!,
            onDismiss = { viewModel.dismissBattleResult() }
        )
    }

    // Campaign Over Dialog (50 turns or total conquest/defeat)
    if (state.isGameOver) {
        CampaignGameOverDialog(
            state = state,
            onRestartCampaign = { viewModel.restartCampaign() },
            onReturnToMainMenu = onReturnToMenu
        )
    }
}
