package com.nipun.examapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

data class Question(
    val text: String,
    val options: List<String>,
    val answer: Int
)

data class Exam(
    val title: String,
    val durationMinutes: Int,
    val questions: List<Question>
)

private val sampleExam = Exam(
    title = "Bank Job Preparation Test",
    durationMinutes = 5,
    questions = listOf(
        Question("What is the capital of Bangladesh?",
            listOf("Dhaka", "Chattogram", "Rajshahi", "Khulna"), 0),
        Question("2 + 5 × 2 = ?",
            listOf("14", "12", "10", "9"), 0),
        Question("Which one is a prime number?",
            listOf("21", "27", "29", "33"), 2),
        Question("What is 25% of 200?",
            listOf("25", "40", "50", "75"), 2),
        Question("Which is a programming language?",
            listOf("Kotlin", "HTML", "HTTP", "DNS"), 0)
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ExamApp()
            }
        }
    }
}

@Composable
fun ExamApp() {
    var screen by remember { mutableStateOf("home") }
    var selected by remember { mutableStateOf(mapOf<Int, Int>()) }
    var result by remember { mutableStateOf(0) }

    when (screen) {
        "home" -> HomeScreen(
            exam = sampleExam,
            onStart = {
                selected = emptyMap()
                screen = "exam"
            }
        )

        "exam" -> ExamScreen(
            exam = sampleExam,
            selected = selected,
            onSelect = { q, option ->
                selected = selected + (q to option)
            },
            onSubmit = {
                result = sampleExam.questions.indices.count {
                    selected[it] == sampleExam.questions[it].answer
                }
                screen = "result"
            }
        )

        "result" -> ResultScreen(
            score = result,
            total = sampleExam.questions.size,
            onHome = { screen = "home" }
        )
    }
}

@Composable
fun HomeScreen(exam: Exam, onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("EXAM APP", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp)) {
                Text(exam.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("${exam.questions.size} questions • ${exam.durationMinutes} minutes")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Exam")
                }
            }
        }
    }
}

@Composable
fun ExamScreen(
    exam: Exam,
    selected: Map<Int, Int>,
    onSelect: (Int, Int) -> Unit,
    onSubmit: () -> Unit
) {
    var secondsLeft by remember { mutableStateOf(exam.durationMinutes * 60) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
        onSubmit()
    }

    val minutes = secondsLeft / 60
    val seconds = secondsLeft % 60

    Column(Modifier.fillMaxSize()) {
        Surface(shadowElevation = 4.dp) {
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exam")
                Text(
                    String.format("%02d:%02d", minutes, seconds),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(exam.questions) { index, question ->
                Card(
                    Modifier.fillMaxWidth().padding(bottom = 14.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "${index + 1}. ${question.text}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(10.dp))

                        question.options.forEachIndexed { optionIndex, option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selected[index] == optionIndex,
                                    onClick = { onSelect(index, optionIndex) }
                                )
                                Text(option)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Submit Exam")
        }
    }
}

@Composable
fun ResultScreen(score: Int, total: Int, onHome: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Exam Completed!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))
        Text("$score / $total", style = MaterialTheme.typography.displayMedium)
        Spacer(Modifier.height(10.dp))
        Text("Correct answers")
        Spacer(Modifier.height(24.dp))
        Button(onClick = onHome) {
            Text("Back to Home")
        }
    }
}
