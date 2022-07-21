package com.example.startwars.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.startwars.R
import com.example.startwars.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolBar()
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolbar)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController)

    }

     fun showLoaderApp(show : Boolean = true) {
        binding.rlProgressCircular.isVisible = show
    }

     fun showSnackBarMessage(msg: String = this.getString(R.string.something_went_wrong_error)) {
        Snackbar.make(binding.root, msg , Snackbar.LENGTH_LONG).show()
    }
}