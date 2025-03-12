package com.example.mynoteapp.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mynoteapp.ViewModel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun TaskDetails(navController: NavController, viewModel: TaskViewModel, taskId: String ) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyTaskAppPrefs", Context.MODE_PRIVATE)

    var TaskTitle by remember {
        mutableStateOf(sharedPreferences.getString("TaskTitle", "") ?: "")
    }
    var TaskDescription by remember {
        mutableStateOf(sharedPreferences.getString("TaskDescription", "") ?: "")
    }
    var TaskDate by remember {
        mutableStateOf(sharedPreferences.getString("TaskDate", "") ?: "")
    }
    var TaskHour by remember {
        mutableStateOf(sharedPreferences.getString("TaskHour", "") ?: "")
    }
    var ShowPopup by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        delay(3000)
        viewModel.getTasks { taskList ->
            val selectedTask = taskList.find { it.taskID == taskId }
            if (selectedTask != null) {
                TaskTitle = selectedTask.taskTitle ?: ""
                TaskDescription = selectedTask.taskDescription ?: ""
                TaskDate = selectedTask.taskDate ?: ""
                TaskHour = selectedTask.taskHour ?: ""
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Gray)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .size(450.dp, 150.dp)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.LightGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Düzenle",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { ShowPopup = true }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Sil",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                viewModel.deleteTask(taskId)
                                navController.popBackStack()
                            }
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Görev Başlığı: $TaskTitle",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Görev Açıklaması: $TaskDescription",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Görev Tarihi: $TaskDate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Görev Saati: $TaskHour",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }
    }

    if (ShowPopup) {
        TaskUpdate(
            onDismiss = { ShowPopup = false },
            taskViewModel = viewModel,
            navController = navController,
            taskId = taskId
        )
    }
}

@Composable
fun TaskUpdate(
    taskViewModel: TaskViewModel,
    onDismiss: () -> Unit,
    navController: NavController,
    taskId: String
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskDate by remember { mutableStateOf("") }
    var taskHour by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp)),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Görev Güncelle",
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
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        onClick = {
                            if (taskTitle.isNotBlank() && taskDescription.isNotBlank()) {
                                taskViewModel.updateTask(
                                    taskId = taskId,
                                    taskTitle = taskTitle,
                                    taskDescription = taskDescription
                                )
                                navController.popBackStack()
                                onDismiss()
                            }
                        }
                    ) {
                        Text("Güncelle", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        onClick = { onDismiss() }
                    ) {
                        Text("İptal")
                    }
                }
            }
        }
    }
}