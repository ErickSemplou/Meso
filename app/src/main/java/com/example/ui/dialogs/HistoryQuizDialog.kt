package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.HistoryQuizQuestion
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

@Composable
fun HistoryQuizDialog(
    question: HistoryQuizQuestion,
    quizIndex: Int,
    totalQuizzes: Int = 25,
    onAnswerSubmitted: (isCorrect: Boolean, question: HistoryQuizQuestion) -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFDF9F0),
            border = androidx.compose.foundation.BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 20.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("history_quiz_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(BronzeDark, Color(0xFF4E342E))
                            ),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = SumerianGoldBright,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ІСТОРИЧНИЙ ТЕСТ (6 КЛАС) • $quizIndex/$totalQuizzes",
                        color = SumerianGoldBright,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.5.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question Title Card
                Surface(
                    color = AncientParchmentDark.copy(alpha = 0.45f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = question.question,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E140A),
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Options List
                val labels = listOf("А", "Б", "В", "Г")
                question.options.forEachIndexed { index, optionText ->
                    val isChosen = selectedIndex == index
                    val isCorrectOption = index == question.correctIndex

                    val (bgColor, borderColor, textColor) = when {
                        !isSubmitted && isChosen -> Triple(
                            SumerianGold.copy(alpha = 0.35f),
                            SumerianGold,
                            Color(0xFF1E140A)
                        )
                        !isSubmitted -> Triple(
                            AncientParchment,
                            BronzeDark.copy(alpha = 0.4f),
                            Color(0xFF2E1A0E)
                        )
                        isSubmitted && isCorrectOption -> Triple(
                            Color(0xFFE8F5E9),
                            Color(0xFF2E7D32),
                            Color(0xFF1B5E20)
                        )
                        isSubmitted && isChosen && !isCorrectOption -> Triple(
                            Color(0xFFFFEBEE),
                            TerracottaRed,
                            TerracottaRed
                        )
                        else -> Triple(
                            AncientParchment.copy(alpha = 0.5f),
                            BronzeDark.copy(alpha = 0.2f),
                            Color.Gray
                        )
                    }

                    Surface(
                        color = bgColor,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !isSubmitted) {
                                selectedIndex = index
                            }
                            .testTag("quiz_option_$index")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(borderColor, CircleShape)
                            ) {
                                Text(
                                    text = labels.getOrElse(index) { "$index" },
                                    color = if (isSubmitted && isCorrectOption) Color.White else if (!isSubmitted && isChosen) BronzeDark else Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = optionText,
                                fontSize = 13.sp,
                                fontWeight = if (isChosen || (isSubmitted && isCorrectOption)) FontWeight.Bold else FontWeight.Medium,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSubmitted && isCorrectOption) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isSubmitted && isChosen) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = TerracottaRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Answer explanation and rewards when submitted
                AnimatedVisibility(visible = isSubmitted) {
                    val wasCorrect = selectedIndex == question.correctIndex
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (wasCorrect) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.5.dp,
                                if (wasCorrect) Color(0xFF4CAF50) else SumerianGold,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (wasCorrect) "🎉 ВІДМІННО! ВІДПОВІДЬ ПРАВИЛЬНА" else "💡 ПОМИЛКА, АЛЕ ЦЕ ЧУДОВА НАГОДА НАВЧИТИСЯ",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            color = if (wasCorrect) Color(0xFF1B5E20) else Color(0xFFE65100),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "📜 Пояснення: ${question.explanation}",
                            fontSize = 11.5.sp,
                            color = Color(0xFF2E1A0E),
                            lineHeight = 16.sp
                        )
                        if (wasCorrect) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "🎁 Нагорода: ${question.rewardText}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Button
                if (!isSubmitted) {
                    Button(
                        onClick = {
                            if (selectedIndex != null) {
                                isSubmitted = true
                            }
                        },
                        enabled = selectedIndex != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BronzePrimary,
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SumerianGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("submit_quiz_answer_button")
                    ) {
                        Text(
                            text = "ВІДПОВІСТИ НА ПИТАННЯ",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            val wasCorrect = selectedIndex == question.correctIndex
                            onAnswerSubmitted(wasCorrect, question)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedIndex == question.correctIndex) Color(0xFF2E7D32) else BronzeDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SumerianGoldBright),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("continue_after_quiz_button")
                    ) {
                        Text(
                            text = if (selectedIndex == question.correctIndex) "ПРИЙНЯТИ НАГОРОДУ ТА ПРОДОВЖИТИ" else "ЗРОЗУМІЛО, ПРОДОВЖИТИ ГРУ",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
