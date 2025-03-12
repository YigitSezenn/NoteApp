package com.example.mynoteapp.ViewModel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.PropertyName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewModel {
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Task data class
    data class Task(
        @get:PropertyName("taskID") var taskID: String? = null,
        @get:PropertyName("taskTitle") var taskTitle: String? = null,
        @get:PropertyName("taskDescription") var taskDescription: String? = null,
        @get:PropertyName("taskHour") var taskHour: String? = null,
        @get:PropertyName("taskDate") var taskDate: String? = null
    )

    // Add a new task
    fun addTask(taskTitle: String?, taskDescription: String?) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: run {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        val taskID = database.child("tasks").child(userId).push().key ?: return

        val newTask = Task(
            taskID = taskID,
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            taskHour = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )

        database.child("tasks").child(userId).child(taskID).setValue(newTask)
            .addOnSuccessListener {
                println("Görev başarıyla eklendi!")
            }.addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }
    }

    // Get all tasks
    fun getTasks(callback: (List<Task>) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: run {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        database.child("tasks").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val updatedTasks = mutableListOf<Task>()
                snapshot.children.forEach { taskSnapshot ->
                    try {
                        val task = taskSnapshot.getValue(Task::class.java)
                        task?.let { updatedTasks.add(it) }
                    } catch (e: Exception) {
                        println("Error deserializing task: ${e.message}")
                    }
                }
                callback(updatedTasks)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Hata: ${error.message}")
            }
        })
    }

    // Update a task
    fun updateTask(taskId: String, taskTitle: String, taskDescription: String) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: run {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        val taskRef = database.child("tasks").child(userId).child(taskId)
        val updatedTask = Task(
            taskID = taskId,
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            taskHour = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            taskDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )

        taskRef.setValue(updatedTask)
            .addOnSuccessListener {
                println("Görev başarıyla güncellendi!")
            }.addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }
    }

    // Delete a task
    fun deleteTask(taskId: String) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: run {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        val taskRef = database.child("tasks").child(userId).child(taskId)
        taskRef.removeValue()
            .addOnSuccessListener {
                println("Görev başarıyla silindi!")
            }.addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }
    }
}

