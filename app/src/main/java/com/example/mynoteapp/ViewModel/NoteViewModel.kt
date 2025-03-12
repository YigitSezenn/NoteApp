package com.example.mynoteapp.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NoteViewModel {
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    data class Notes(
        var NoteID: String? = null,
        var NoteTitle: String? = null,
        var NoteBody: String? = null,
        var NoteDate: String? = null,

        )


    fun addNote(noteTitle: String, noteBody: String) {
        var auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid // Kullanıcı ID'sini al
        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        val noteID = database.child("notes").child(userId).push().key  // Kullanıcıya özel not ID
        if (noteID != null) {
            val newNote = Notes(
                NoteTitle = noteTitle, NoteBody = noteBody, NoteDate = SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss", Locale.getDefault()
                ).format(Date())
            )

            database.child("notes").child(userId).child(noteID).setValue(newNote)
                .addOnSuccessListener {
                    println("Not başarıyla eklendi!")
                }.addOnFailureListener { e ->
                    println("Hata: ${e.message}")
                }
        }


    }

    fun getNote(callback: (List<Notes>) -> Unit) {
        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val turkishTimeZone = TimeZone.getTimeZone("Europe/Istanbul")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = turkishTimeZone
        val noteDate = dateFormat.format(Date())
        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }
        database.child("notes").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedNotes = mutableListOf<Notes>()
                snapshot.children.forEach { noteSnapshot ->
                    val note = noteSnapshot.getValue(Notes::class.java)
                    val noteId = noteSnapshot.key  // 🔥 Firebase'deki Not ID'si

                    if (note != null && noteId != null) {
                        note.NoteID = noteId
                        note.NoteDate=noteDate


                        updatedNotes.add(note)



                    }
                }
  callback(updatedNotes) // 🔥 Notları geri döndür

            }

            override fun onCancelled(error: DatabaseError) {
                println("Firebase hata: ${error.message}")
            }
        })
    }

    fun UpdateNotes(noteId: String, noteTitle: String, noteBody: String) {
        val database = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val turkishTimeZone = TimeZone.getTimeZone("Europe/Istanbul")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = turkishTimeZone
        val noteDate = dateFormat.format(Date())

        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }
        val noteref = database.child("notes").child(userId).child(noteId)
        val updateNote = Notes(
            NoteID = noteId,
            NoteTitle = noteTitle,
            NoteBody = noteBody,
            NoteDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("Europe/Istanbul")
            }.format(Date())

        )

        noteref.setValue(updateNote)
            .addOnSuccessListener {
                println("Not başarıyla güncellendi!")
            }.addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }


    }
    fun DeletedNote (noteId: String,noteTitle: String,noteBody: String)
    {
        val database = FirebaseDatabase.getInstance().reference

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }
        val notereferance = database.child("notes").child(userId).child(noteId)
        val deleteNote= Notes (
            NoteID = noteId,
            NoteTitle = noteTitle,
            NoteBody = noteBody
        )
        notereferance.removeValue()
            .addOnSuccessListener {
                println("Not başarıyla silindi!")
            }.addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }

    }
}




