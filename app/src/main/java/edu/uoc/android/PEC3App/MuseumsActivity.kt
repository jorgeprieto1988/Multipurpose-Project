package edu.uoc.android.PEC3App

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import edu.uoc.android.rest.RetrofitFactory
import edu.uoc.android.rest.models.Museums

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_museums.*

/**
 * MuseumsActivity
 * Shows a list of the museums
 */
class MuseumsActivity : AppCompatActivity() {
    /**
     * Declares recycler variables
     */
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_museums)
        viewManager = LinearLayoutManager(this)

         //Call to the rest api to get all the museums info
        val call = RetrofitFactory.museumAPI.museums("1", "5")
        call.enqueue(object : Callback<Museums> {
            override fun onResponse(call : Call<Museums>, response : Response<Museums>) {
                if (response.code() == 200){
                    //showProgress(false)

                    val museums = response.body()!!

                    val elemento = museums.getElements()

                     //Shows progress bar while loading the museums
                    progressBar1.visibility = View.INVISIBLE
                    viewAdapter = AdapterMuseums(elemento)

                    recyclerView = findViewById<RecyclerView>(R.id.recyclerview).apply {
                        // use this setting to improve performance if you know that changes
                        // in content do not change the layout size of the RecyclerView
                        setHasFixedSize(true)

                        // use a linear layout manager
                        layoutManager = viewManager

                        // specify an viewAdapter (see also next example)
                        adapter = viewAdapter
                    }
                }
            }


            override fun onFailure(call: Call<Museums>, t: Throwable){
                Log.d("Error", getString(R.string.data_conexion_error))
            }
        })


    }



}