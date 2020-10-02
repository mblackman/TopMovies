package app.mblackman.topmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.mblackman.topmovies.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}