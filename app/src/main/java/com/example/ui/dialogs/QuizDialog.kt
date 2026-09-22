package com.example.ui.dialogs

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.QuizQuestion
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed
import com.example.viewmodel.QuizResult

@Composable
fun QuizDialog(
    question: QuizQuestion,
    onAnswerSubmitted: (Int) -> QuizResult,
    onDismiss: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var quizResult by remember { mutableStateOf<QuizResult?>(null) }

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("quiz_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(AncientParchmentDark, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Вікторина",
                        tint = BronzeDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Історична вікторина (6 клас)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = BronzeDark
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Question text
                Text(
                    text = question.question,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = BronzeDark,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Options
                question.options.forEachIndexed { index, optionText ->
                    val isChosen = selectedIndex == index
                    val isCorrect = quizResult != null && index == question.correctIndex
                    val isWrong = quizResult != null && isChosen && !quizResult!!.isCorrect

                    val backgroundColor = when {
                        isCorrect -> Color(0xFFC8E6C9) // Green
                        isWrong -> Color(0xFFFFCDD2) // Red
                        isChosen -> SumerianGold.copy(alpha = 0.4f)
                        else -> AncientParchment
                    }

                    val borderColor = when {
                        isCorrect -> Color(0xFF2E7D32)
                        isWrong -> TerracottaRed
                        isChosen -> BronzePrimary
                        else -> BronzeDark.copy(alpha = 0.3f)
                    }

                    Surface(
                        color = backgroundColor,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = quizResult == null) {
                                selectedIndex = index
                                val result = onAnswerSubmitted(index)
                                quizResult = result
                            }
                            .testTag("quiz_option_$index")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "${('А'.code + index).toChar()})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BronzeDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = optionText,
                                fontSize = 13.sp,
                                color = BronzeDark,
                                modifier = Modifier.weight(1f)
                            )
                            if (isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Правильно",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(20.dp)
                                )
                            } else if (isWrong) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = "Неправильно",
                                    tint = TerracottaRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Educational explanation after answering
                if (quizResult != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = AncientParchmentDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = if (quizResult!!.isCorrect) "🎉 Відмінно! Правильна відповідь!" else "💡 Запам'ятай для уроку історії:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (quizResult!!.isCorrect) Color(0xFF2E7D32) else TerracottaRed
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = quizResult!!.explanation,
                                fontSize = 12.sp,
                                color = BronzeDark,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = quizResult!!.rewardText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (quizResult!!.isCorrect) Color(0xFF1B5E20) else BronzeDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SumerianGold,
                            contentColor = BronzeDark
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("quiz_continue_button")
                    ) {
                        Text(text = "Продовжити правління", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
