package edu.uoc.android.PEC3App

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Quizz activity
 * Shows a list of questions with a recyclerview and retrieved from firestore
 */
class QuizzActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        val db = FirebaseFirestore.getInstance()
        val preguntas= mutableListOf<Question>()

        //Connection to the database
        db.collection("preguntas")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (pregunta in task.result!!) {
                        Log.d("Datos", pregunta.get("choice1").toString())

                        preguntas.add(Question(pregunta.get("choice1").toString(),
                            pregunta.get("choice2").toString(),
                            pregunta.get("image").toString(),
                            pregunta.get("rightchoice") as Long,
                            pregunta.get("title").toString()))
                    }
                    viewManager = LinearLayoutManager(this)

                    viewAdapter = AdapterQuizzes(preguntas)

                    recyclerView = findViewById<RecyclerView>(R.id.recyclerview_quizz).apply {
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        setHasFixedSize(true)

                        // use a linear layout manager
                        layoutManager = viewManager

                        // specify an viewAdapter (see also next example)
                        adapter = viewAdapter
                    }

                } else {
                    Log.w("Error", getString(R.string.data_conexion_error), task.exception)
                }
            }
    }
}
