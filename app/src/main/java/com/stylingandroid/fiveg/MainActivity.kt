package com.stylingandroid.fiveg

import android.Manifest.permission.READ_PHONE_STATE
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.stylingandroid.fiveg.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fiveGViewModel: FiveGViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                monitorConnectivity()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            checkSelfPermission(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ->
                monitorConnectivity()
            shouldShowRequestPermissionRationale(READ_PHONE_STATE) -> showRationale()
            else -> requestPermissionLauncher.launch(READ_PHONE_STATE)
        }
        checkSelfPermission(READ_PHONE_STATE)
    }

    private fun showRationale() {
        TODO("Not yet implemented")
    }

    private fun monitorConnectivity() {
        println("Start monitoring Status")
        lifecycleScope.launchWhenStarted {
            fiveGViewModel.statusFlow.collect { status ->
                println("Status: $status")
                if (status is Status.FiveGStatus) {
                    binding.fiveG.text = getString(R.string.is_5g, status.is5G.toString())
                    binding.metered.text =
                        getString(R.string.is_metered, status.isMetered.toString())
                }
            }
        }
    }
}
