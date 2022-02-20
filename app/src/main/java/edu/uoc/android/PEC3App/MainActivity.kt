package edu.uoc.android.PEC3App

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    /**
     * MainActivity
     * Calls to the other activities
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * Call museums activity
         */
        rellay_museums.setOnClickListener{
            val intent = Intent(this@MainActivity, MuseumsActivity::class.java)
            startActivity(intent)
        }
        /**
         * Call Maps activity
         */
        rellay_maps.setOnClickListener{
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }

        /**
         * Call Quizz activity
         */
        rellay_quizzes.setOnClickListener{
            val intent = Intent(this@MainActivity, QuizzActivity::class.java)
            startActivity(intent)
        }

        /**
         * Call Settings activity
         */
        rellay_settings.setOnClickListener{
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
