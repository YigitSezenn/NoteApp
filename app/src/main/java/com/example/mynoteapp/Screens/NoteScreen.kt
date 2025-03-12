package com.example.mynoteapp.Screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.navigation.NavHostController
import com.example.mynoteapp.AppNavHost.NavigationItem
import com.example.mynoteapp.AppSettings.AppColors
import com.example.mynoteapp.AppSettings.RandomColor
import com.example.mynoteapp.ViewModel.NoteViewModel

import kotlinx.coroutines.delay

@Composable
fun AddNoteFab(onClick: () -> Unit) {
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
    )
    {
        if (visibility)
            FloatingActionButton(
                onClick = {
                    onClick()
                    visibility = true


                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp) // 📌 FAB boyutu
                    .align(Alignment.BottomEnd),
                containerColor = (AppColors.Background), // 📌 Açık yeşil renk (görseldeki gibi)
                shape = CircleShape, // 📌 Tam yuvarlak
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Not Ekle",
                    tint = AppColors.ButtonColor // 📌 İçindeki "+" simgesinin rengi
                )
            }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteScreen(navController: NavHostController, viewModel: NoteViewModel) {

    var showPopup by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(listOf<NoteViewModel.Notes>()) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyNoteAppPrefs", Context.MODE_PRIVATE)
    val firstAndSurname by remember {
        mutableStateOf(sharedPreferences.getString("Firstname", "") ?: "")
    }
    var isLoading by remember { mutableStateOf(true) }


    // ✅ SharedPreferences'dan veri çekme

    LaunchedEffect(Unit) {

        delay(3000)
        viewModel.getNote { GetList ->

            notes = GetList

            isLoading = false


        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(AppColors.Background)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {


                CircularProgressIndicator(
                    color = AppColors.ButtonColor,
                    strokeWidth = 8.dp
                )

            }
            Text("Notlar Yükleniyor...", fontSize = 18.sp, color = Color.Black)
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Hoşgeldin, $firstAndSurname",
                    color = AppColors.SecondaryText,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = " İşte Notların",

                    color = AppColors.SecondaryText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(8.dp)
                )


//                Button(
//                    modifier = Modifier
//                        .height(55.dp)
//                        .width(200.dp),
//                    colors = ButtonDefaults.buttonColors(AppColors.ButtonColor), // Modern Mor
//                    onClick = { showPopup = true }
//                ) {
//                    Text(
//                        "Notunuzu Ekleyin",
//                        color = Color.White
//                    )
//                }
            }
        }


        LazyColumn {
            Modifier.fillMaxSize()
            items(notes) { note -> // Notları listele
                val cardColor by remember { mutableStateOf(RandomColor.getRandomColor()) }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()

                        .padding(6.dp)
                        .clickable {

                            navController.navigate(NavigationItem.NoteDetails.route + "/${note.NoteID}")
                             sharedPreferences.edit().apply {
                                putString("NoteID", note.NoteID)
                                putString("NoteTitle", note.NoteTitle)
                                putString("NoteBody", note.NoteBody)
                                putString("NoteDate", note.NoteDate)
                                apply()
                            }


                        },
                    colors = CardDefaults.cardColors(cardColor),

                    elevation = CardDefaults.cardElevation(8.dp), // Kartın gölgesi (kabartma efekti)

                )


                {


                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = "Not Başlığı: ${note.NoteTitle ?: "Başlık Yok"}",
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Not İçeriği: ${note.NoteBody ?: "İçerik Yok"}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Not Tarihi: ${note.NoteDate ?: "Tarih Yok"}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )


                    }

                }

            }
        }
    }

    AddNoteFab {

        showPopup = true

    }




    if (showPopup) {
        NotePopUp(
            onDismiss = { showPopup = false },
            noteViewModel = viewModel

        )
    }
}


@Composable
fun NotePopUp(
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


                                noteViewModel.addNote(
                                    noteTitle,
                                    noteBody
                                )




                                sharedPreferences.edit().apply {
                                    putString("NoteTitle", noteTitle)
                                    putString("NoteBody", noteBody)
                                    apply()
                                }
                                noteViewModel.getNote { GetList ->
                                    println("Notlar: $GetList")
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








