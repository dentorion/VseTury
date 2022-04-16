package com.entin.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.entin.presentation.R
import com.entin.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal

/**
 * Main Activity
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setupNavigation()
        noInternetChecking()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(binding.root)
    }

    // Setup navigation
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    // No Internet Dialog: Signal
    private fun noInternetChecking() {
        NoInternetDialogSignal.Builder(
            this,
            lifecycle
        ).apply {
            dialogProperties.apply {
                cancelable = false
                noInternetConnectionTitle = resources.getString(R.string.no_network_connection)
                noInternetConnectionMessage =
                    resources.getString(R.string.no_network_connection_check_try)
                showInternetOnButtons = true
                pleaseTurnOnText = resources.getString(R.string.no_network_turn_on)
                wifiOnButtonText = resources.getString(R.string.no_network_wifi)
                mobileDataOnButtonText = resources.getString(R.string.no_network_mobile_data)
                onAirplaneModeTitle = resources.getString(R.string.no_network_connection)
                onAirplaneModeMessage = resources.getString(R.string.no_network_msg_airplane)
                pleaseTurnOffText = resources.getString(R.string.no_network_turn_off)
                airplaneModeOffButtonText = resources.getString(R.string.no_network_airplane)
                showAirplaneModeOffButtons = true
            }
        }.build()
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}