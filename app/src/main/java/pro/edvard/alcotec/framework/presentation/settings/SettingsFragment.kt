package pro.edvard.alcotec.framework.presentation.settings

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import pro.edvard.alcotec.R
import pro.edvard.alcotec.business.K
import pro.edvard.alcotec.framework.util.hideKeyboard

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    /**
     * 1. send location
     * 2. get/upload image
     * 3. Handle marker with image
     * */

    companion object {
        const val REQUEST_KEY_DESTINATION = "REQUEST_KEY_DESTINATION"
        const val BUNDLE_KEY_DESTINATION = "BUNDLE_KEY_DESTINATION"
    }

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        handleGo()
        editTextConfig()

    }

    private fun editTextConfig() {

        val lonEditText = settings_outlinedTextField_lon.editText!!

        lonEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getLatLngFromUser()?.let { userLatLng ->
                    println("APP_DEBUG: WAS CALLED")
                    provideDestinationToMap(userLatLng)
                    lonEditText.hideKeyboard()
                    requireActivity().window.decorView.clearFocus()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

    }

    private fun handleGo() {
        settings_button_go.setOnClickListener {
            getLatLngFromUser()?.let { userLatLng: LatLng ->
                provideDestinationToMap(userLatLng)
                settings_outlinedTextField_lat.editText!!.hideKeyboard()
                settings_outlinedTextField_lon.editText!!.hideKeyboard()
                requireActivity().window.decorView.clearFocus()
            }
        }
    }

    private fun getLatLngFromUser(): LatLng? {
        val lat = settings_outlinedTextField_lat.editText!!.text.toString()
        val lon = settings_outlinedTextField_lon.editText!!.text.toString()

        return if (lat.isNotBlank() && lon.isNotBlank()) {
            try {
                LatLng(lat.toDouble(), lon.toDouble())
            } catch (e: Exception) {
                informUser(K.Error.Message.INCORRECT_VALUE)
                null
            }
        } else {
            informUser("Latitude or Longitude ${K.Error.Message.EMPTY_FIELD}")
            null
        }
    }

    private fun provideDestinationToMap(latLng: LatLng) {
//            val destination = LatLng(50.461830, 30.452030)
        setFragmentResult(
            REQUEST_KEY_DESTINATION,
            bundleOf(BUNDLE_KEY_DESTINATION to latLng)
        )
    }

    private fun informUser(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}