package com.example.mynoteapp.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.AppSettings.RandomColor
import com.example.mynoteapp.ViewModel.NoteViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun NoteDetails(navController: NavController, viewModel: NoteViewModel, noteId: String) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)
    var NoteTitle by remember { mutableStateOf(sharedPreferences.getString("NoteTitle", "") ?: "") }
    var NoteBody by remember { mutableStateOf(sharedPreferences.getString("NoteBody", "") ?: "") }
    var NoteDate by remember { mutableStateOf(sharedPreferences.getString("NoteDate", "") ?: "") }
    var ShowPopup by remember { mutableStateOf(false) }



    LaunchedEffect(noteId) {

        delay(3000)
        viewModel.getNote { notelist ->

            val selectednote = notelist.find { it.NoteID == noteId }
            if (selectednote != null) {
                NoteTitle = selectednote.NoteTitle ?: ""
                NoteBody = selectednote.NoteBody ?: ""
                NoteDate = selectednote.NoteDate ?: ""
            }


        }

    }
    Box(
        Modifier
            .background(AppColors.Background)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter

    ) {


        val cardColor by remember { mutableStateOf(RandomColor.getRandomColor()) }
        Card(
            modifier = Modifier
                .size(450.dp, 150.dp) // 🔥 Boyutu sabit bırak
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(cardColor.copy()),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp) // 🔥 Dikdörtgen yap
        ) {
            Row( // 🔥 İkonlar ve yazıları yan yana hizala
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.2f) // 🔥 İkonları daha küçük alan kaplayacak şekilde ayarla
                        .padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp) // 🔥 Daha küçük yap
                            .clickable {
                                ShowPopup = true

                            }
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // 🔥 İkonlar arası boşluk

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(0.8f) // 🔥 Yazıların daha fazla alan kaplamasını sağla
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Not Başlığı: $NoteTitle",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "Not İçeriği: $NoteBody",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )

                    Text(
                        text = "Not Tarihi: $NoteDate",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }
            }
        }
    }
    if (ShowPopup) {
        NoteUpdate(
            onDismiss = { ShowPopup = false },
            noteViewModel = viewModel

        )
    }
}


@Composable
fun NoteUpdate(
    noteViewModel: NoteViewModel,
    onDismiss: () -> Unit,
) {
    var noteTitle by remember { mutableStateOf("") }
    var noteBody by remember { mutableStateOf("") }
    var noteDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)
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
                    text = "Yeni Not Ekle",
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    label = { Text("Not Başlığı") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = noteBody,
                    onValueChange = { noteBody = it },
                    label = { Text("Not İçeriği") },
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
                        colors = ButtonDefaults.buttonColors(AppColors.ButtonColor), // Modern
                        onClick = {
                            if (noteTitle.isNotBlank() && noteBody.isNotBlank()) {


                             noteViewModel.UpdateNotes(
                                    noteId = sharedPreferences.getString("NoteID", "") ?: "",
                                    noteTitle = noteTitle,
                                    noteBody = noteBody
                             )




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
                        colors = ButtonDefaults.buttonColors(AppColors.ButtonColor), // Modern
                        onClick = {
                            onDismiss()


                        }
                    ) {
                        Text("İptal")
                    }
                }
            }
        }
    }
}
