package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.ui.components.FactionFilterBar
import com.example.ui.components.FloatingResourceDeltas
import com.example.ui.components.RomeCommandBar
import com.example.ui.components.RomeTopBar
import com.example.ui.components.StrategicMapCanvas
import com.example.ui.components.TurnLogOverlay
import com.example.ui.dialogs.BattleResultDialog
import com.example.ui.dialogs.CampaignGameOverDialog
import com.example.ui.dialogs.ChronicleDialog
import com.example.ui.dialogs.CityManageDialog
import com.example.ui.dialogs.CodexDialog
import com.example.ui.dialogs.DiplomacyDialog
import com.example.ui.dialogs.EventDialog
import com.example.ui.dialogs.FactionsOverviewDialog
import com.example.ui.dialogs.HistoryQuizDialog
import com.example.ui.dialogs.LawsDialog
import com.example.ui.dialogs.MegaProjectsDialog
import com.example.ui.dialogs.RecruitmentDialog
import com.example.ui.dialogs.TacticalBattleDialog
import com.example.ui.dialogs.TechTreeDialog
import com.example.ui.dialogs.UnitStatsModalDialog
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.SumerianGold
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
    val unitModalState by viewModel.unitModalState.collectAsState()

    val selectedCity = state.cities.find { it.id == selectedCityId }

    // Interactive Faction Selection Filter
    var selectedFactionFilterId by remember { mutableStateOf<String?>(null) }

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
                onOpenChronicle = { showChronicleDialog = true },
                onOpenTurnLogs = { viewModel.openTurnLogs() },
                onOpenFactionsOverview = { viewModel.openFactionsOverview() },
                onOpenMegaProjects = { viewModel.openMegaProjects() },
                onOpenLaws = { viewModel.openLawsDialog() }
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
                onEndTurn = { viewModel.endTurn() },
                onArmyUnitTapped = { factionId, unitTypeId, cityName, count ->
                    viewModel.openArmyUnitDetails(factionId, unitTypeId, cityName, count)
                }
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
            // Interactive Rome Total War Campaign Map Canvas with Seasonal Visuals
            StrategicMapCanvas(
                cities = state.cities,
                selectedCityId = selectedCityId,
                playerFactionId = state.playerFactionId,
                onCitySelected = { viewModel.selectCity(it) },
                turn = state.turn,
                botCampaignSourceCityId = state.botCampaignSourceCityId,
                botCampaignTargetCityId = state.botCampaignTargetCityId,
                filteredFactionId = selectedFactionFilterId,
                onArmyUnitTapped = { factionId, unitTypeId, cityName, count ->
                    viewModel.openArmyUnitDetails(factionId, unitTypeId, cityName, count)
                }
            )

            // Top Overlay: Interactive Faction Filter Buttons
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
            ) {
                FactionFilterBar(
                    state = state,
                    selectedFactionId = selectedFactionFilterId,
                    onFactionSelected = { factionId ->
                        selectedFactionFilterId = factionId
                    },
                    onOpenFactionsOverview = { viewModel.openFactionsOverview() },
                    onOpenDiplomacy = { showDiplomacyDialog = true }
                )
            }
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

    // Grade 6 Historical Educational Test Question Dialog (every 2nd turn, 25 tests total)
    state.activeQuizQuestion?.let { quiz ->
        HistoryQuizDialog(
            question = quiz,
            quizIndex = (state.answeredQuizzesCount + 1).coerceAtMost(25),
            totalQuizzes = 25,
            onAnswerSubmitted = { isCorrect, q ->
                viewModel.resolveHistoryQuiz(isCorrect, q)
            }
        )
    }

    // Tactical Battle Dialog (Interactive Rome: Total War style battle screen)
    state.activeBattle?.let { battle ->
        TacticalBattleDialog(
            battle = battle,
            onExecuteBattle = { tacticId ->
                viewModel.executeTacticalBattle(tacticId)
            },
            onAutoResolve = {
                viewModel.autoResolveTacticalBattle()
            },
            onDismiss = {
                viewModel.dismissTacticalBattle()
            }
        )
    }

    // Laws and Reforms Dialog (Urukagina, Ur-Nammu, Hammurabi)
    if (state.showLawsDialog) {
        LawsDialog(
            activeLaws = state.activeLaws,
            resources = state.resources,
            onEnactLaw = { lawId ->
                viewModel.enactLaw(lawId)
            },
            onRepealLaw = { lawId ->
                viewModel.repealLaw(lawId)
            },
            onDismiss = {
                viewModel.closeLawsDialog()
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

    if (state.showFactionsOverview) {
        FactionsOverviewDialog(
            gameState = state,
            onClose = { viewModel.closeFactionsOverview() },
            onOpenDiplomacy = { showDiplomacyDialog = true }
        )
    }

    if (state.showMegaProjects) {
        MegaProjectsDialog(
            gameState = state,
            onClose = { viewModel.closeMegaProjects() },
            onContribute = { wonderId, grain, clay, silver ->
                viewModel.contributeToWonder(wonderId, grain, clay, silver)
            }
        )
    }

    // Slide-in Turn Log Overlay
    TurnLogOverlay(
        isVisible = state.showTurnLogOverlay,
        turn = state.turn,
        yearBCE = state.yearBCE,
        logs = state.lastTurnLogs,
        onClose = { viewModel.closeTurnLogs() }
    )

    // Room Persistent Unit Stats Modal Dialog
    unitModalState?.let { modalData ->
        UnitStatsModalDialog(
            unit = modalData.unit,
            faction = modalData.faction,
            availableFactionUnits = modalData.availableFactionUnits,
            cityName = modalData.cityName,
            regimentCount = modalData.regimentCount,
            onSelectUnit = { newUnit ->
                viewModel.selectUnitInModal(newUnit)
            },
            onDismiss = {
                viewModel.dismissUnitModal()
            }
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
