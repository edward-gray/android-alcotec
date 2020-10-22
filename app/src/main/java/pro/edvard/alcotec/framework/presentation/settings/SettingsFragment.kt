package pro.edvard.alcotec.framework.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import pro.edvard.alcotec.R

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        const val REQUEST_KEY_DESTINATION = "REQUEST_KEY_DESTINATION"
        const val BUNDLE_KEY_DESTINATION = "BUNDLE_KEY_DESTINATION"
    }

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

//        settings_button_go.setOnClickListener {
//            println("APP_DEBUG: Go clicked")
//            val destination = LatLng(50.461830, 30.452030)
//            setFragmentResult(
//                REQUEST_KEY_DESTINATION,
//                bundleOf(BUNDLE_KEY_DESTINATION to destination)
//            )
//        }

        handleGo()

    }

    private fun handleGo() {

        settings_button_go.setOnClickListener {
            val lat = settings_outlinedTextField_lat.editText!!.text.toString()
            val lon = settings_outlinedTextField_lon.editText!!.text.toString()

            if (lat.isNotBlank() && lon.isNotBlank()) {
                println("APP_DEBUG: $lat $lon")
            }
        }
    }

}