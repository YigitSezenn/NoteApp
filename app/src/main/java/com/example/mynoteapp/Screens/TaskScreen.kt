package com.example.mynoteapp.Screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.AppSettings.RandomColor
import com.example.mynoteapp.ViewModel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun AddTaskFab(onClick: () -> Unit) {
    var visibility by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
        visibility = true
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (visibility)
            FloatingActionButton(
                onClick = { onClick() },
                modifier = Modifier.padding(16.dp),
                containerColor = AppColors.Background,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Görev Ekle",
                    tint = AppColors.ButtonColor
                )
            }
    }
}

@Composable
fun TaskPopUp(
    noteViewModel: TaskViewModel,
    onDismiss: () -> Unit,
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(AppColors.Background),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Yeni Görev Ekle",
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Görev Başlığı") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Görev Açıklaması") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(AppColors.ButtonColor),
                        onClick = {
                            if (taskTitle.isNotBlank() && taskDescription.isNotBlank()) {
                                noteViewModel.addTask(
                                    taskTitle,
                                    taskDescription,

                                )

                                sharedPreferences.edit().apply {

                                    putString("TaskTitle", taskTitle)
                                    putString("TaskDescription", taskDescription)
                                    apply()
                                }
                                noteViewModel.getTasks { GetList ->
                                    println("Görevler: $GetList")
                                }
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Ekle", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(AppColors.ButtonColor),
                        onClick = { onDismiss() }
                    ) {
                        Text("İptal")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    var showPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)
    val firstAndSurname = sharedPreferences.getString("Firstname", "") ?: ""
    var isLoading by remember { mutableStateOf(true) }
    var tasks by remember { mutableStateOf<List<TaskViewModel.Task>>(emptyList()) }
    var taskCompletionStates by remember { mutableStateOf(mutableMapOf<String, Boolean>()) }

    LaunchedEffect(true) {
        viewModel.getTasks { fetchedTasks ->
            tasks = fetchedTasks
            isLoading = false
            taskCompletionStates = fetchedTasks.associate { it.taskID!! to false }.toMutableMap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = AppColors.ButtonColor, strokeWidth = 8.dp)
            Text("Görevleriniz Yükleniyor...", fontSize = 18.sp, color = Color.Black)
        } else {
            Text("Hoşgeldin, $firstAndSurname", fontSize = 18.sp, color = AppColors.SecondaryText)
            Text("İşte Görevleriniz", fontSize = 15.sp, color = AppColors.SecondaryText)
        }

        LazyColumn {
            items(tasks, key = { it.taskID!! }) { task ->
                var isChecked by remember { mutableStateOf(taskCompletionStates[task.taskID!!] ?: false) }
                val cardColor = remember { RandomColor.getRandomColor() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .clickable {
                            navController.navigate("TaskDetails/${task.taskID}")
                        },
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isChecked) Color.Green else cardColor
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Görev: ${task.taskTitle}", fontSize = 18.sp)
                            Text("Açıklama: ${task.taskDescription}", fontSize = 15.sp)
                        }
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                isChecked = it
                                taskCompletionStates[task.taskID!!] = it
                            }
                        )
                    }
                }
            }
        }
    }
    AddTaskFab { showPopup = true }
    if (showPopup) TaskPopUp(viewModel, onDismiss = { showPopup = false })
}

