package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import com.example.model.CodexCategory
import com.example.model.CodexEntry
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold

@Composable
fun CodexDialog(
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<CodexCategory?>(null) }
    var selectedEntry by remember { mutableStateOf<CodexEntry?>(CodexEntry.ENTRIES.first()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("codex_dialog")
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
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Енциклопедія",
                            tint = BronzeDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Енциклопедія стародавнього Межиріччя (6 клас)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_codex_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Category Filter Pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        CategoryPill(
                            label = "Всі розділи",
                            isSelected = selectedCategory == null,
                            onClick = { selectedCategory = null }
                        )
                    }
                    items(CodexCategory.values()) { cat ->
                        CategoryPill(
                            label = cat.title,
                            isSelected = selectedCategory == cat,
                            onClick = { selectedCategory = cat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val filtered = CodexEntry.ENTRIES.filter {
                    selectedCategory == null || it.category == selectedCategory
                }

                // Two pane or split list + reader
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left list of topics
                    LazyColumn(
                        modifier = Modifier
                            .weight(0.45f)
                            .height(280.dp)
                    ) {
                        items(filtered) { entry ->
                            val isSelected = entry.id == selectedEntry?.id
                            Surface(
                                color = if (isSelected) SumerianGold.copy(alpha = 0.35f) else AncientParchment,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) BronzePrimary else BronzeDark.copy(alpha = 0.2f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable { selectedEntry = entry }
                            ) {
                                Column(modifier = Modifier.padding(6.dp)) {
                                    Text(
                                        text = entry.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = BronzeDark
                                    )
                                    Text(
                                        text = entry.shortSummary,
                                        fontSize = 9.sp,
                                        color = Color(0xFF6D4C41),
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Right reader pane
                    Surface(
                        color = AncientParchment,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .weight(0.55f)
                            .height(280.dp)
                    ) {
                        if (selectedEntry != null) {
                            Column(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = selectedEntry!!.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = BronzeDark
                                )
                                Text(
                                    text = selectedEntry!!.subtitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BronzePrimary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = selectedEntry!!.fullText,
                                    fontSize = 12.sp,
                                    color = Color(0xFF3E2723),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (isSelected) BronzePrimary else AncientParchmentDark,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else BronzeDark,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
