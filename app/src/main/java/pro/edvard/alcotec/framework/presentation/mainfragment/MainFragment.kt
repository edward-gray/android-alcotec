package pro.edvard.alcotec.framework.presentation.mainfragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import pro.edvard.alcotec.R
import pro.edvard.alcotec.framework.presentation.maps.MapsFragment
import pro.edvard.alcotec.framework.presentation.settings.SettingsFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuration(view)
    }

    private fun configuration(view: View) {
        navController = Navigation.findNavController(view)
        fragmentsConfig()
        handleOnBackPressed()
    }

    private fun fragmentsConfig() {
        val manager = requireActivity().supportFragmentManager
        manager.beginTransaction().replace(R.id.main_fragment_settings, SettingsFragment()).commit()
        manager.beginTransaction().replace(R.id.main_fragment_maps, MapsFragment()).commit()
    }

    private fun handleOnBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }



}