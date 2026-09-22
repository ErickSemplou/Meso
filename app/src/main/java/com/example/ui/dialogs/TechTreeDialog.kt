package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.TechBranch
import com.example.model.Technology
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold

@Composable
fun TechTreeDialog(
    researchedTechIds: Set<String>,
    currentTechId: String?,
    currentTechTurnsRemaining: Int,
    onStartResearch: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedBranch by remember { mutableStateOf(TechBranch.CIVIL) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("tech_tree_dialog")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = "Наука",
                            tint = BronzeDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Дім мудрості та наук",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_tech_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                TabRow(
                    selectedTabIndex = if (selectedBranch == TechBranch.CIVIL) 0 else 1,
                    containerColor = AncientParchmentDark,
                    contentColor = BronzeDark
                ) {
                    Tab(
                        selected = selectedBranch == TechBranch.CIVIL,
                        onClick = { selectedBranch = TechBranch.CIVIL },
                        text = { Text("🏛 Цивільні науки", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                    Tab(
                        selected = selectedBranch == TechBranch.MILITARY,
                        onClick = { selectedBranch = TechBranch.MILITARY },
                        text = { Text("⚔ Військова справа", fontWeight = FontWeight.Bold, fontSize = 11.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val branchTechs = Technology.ALL_TECHS.filter { it.branch == selectedBranch }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(branchTechs) { tech ->
                        val isResearched = researchedTechIds.contains(tech.id)
                        val isResearching = currentTechId == tech.id
                        val canResearch = !isResearched &&
                                !isResearching &&
                                researchedTechIds.containsAll(tech.prerequisites)

                        Surface(
                            color = when {
                                isResearched -> AncientParchmentDark.copy(alpha = 0.6f)
                                isResearching -> SumerianGold.copy(alpha = 0.25f)
                                else -> AncientParchment
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                when {
                                    isResearched -> Color(0xFF2E7D32)
                                    isResearching -> SumerianGold
                                    canResearch -> BronzePrimary
                                    else -> BronzeDark.copy(alpha = 0.25f)
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tech.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = BronzeDark
                                        )
                                        Text(
                                            text = tech.bonusSummary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }

                                    when {
                                        isResearched -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Відкрито",
                                                    tint = Color(0xFF2E7D32),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text("Вивчено", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                                            }
                                        }
                                        isResearching -> {
                                            Text(
                                                text = "Досліджується (${currentTechTurnsRemaining} хід)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BronzeDark
                                            )
                                        }
                                        canResearch -> {
                                            Button(
                                                onClick = { onStartResearch(tech.id) },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = BronzePrimary,
                                                    contentColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.testTag("research_button_${tech.id}")
                                            ) {
                                                Text("Вивчати (${tech.turnsRequired} хід)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        else -> {
                                            Text("Заблоковано", fontSize = 10.sp, color = Color.Gray)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tech.description,
                                    fontSize = 10.sp,
                                    color = BronzeDark,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                // 6th-grade syllabus history fact
                                Text(
                                    text = "💡 ${tech.historyFact}",
                                    fontSize = 9.sp,
                                    color = Color(0xFF4E342E),
                                    lineHeight = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
