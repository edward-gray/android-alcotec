package pro.edvard.alcotec.framework.presentation.settings

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import pro.edvard.alcotec.framework.hideKeyboard
import pro.edvard.alcotec.framework.presentation.util.FileHandler
import pro.edvard.alcotec.framework.presentation.util.PermissionHandler

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
        const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1
        const val DIRECTORY_NAME = "Alcotec"
        const val AVATAR_FILE_NAME = "avatar.jpeg"
    }

    private lateinit var navController: NavController
    private lateinit var fileHandler: FileHandler
    private lateinit var permissionHandler: PermissionHandler

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuration(view)
    }

    private fun configuration(view: View) {
        navController = Navigation.findNavController(view)
        fileHandler = FileHandler(requireContext())
        permissionHandler = PermissionHandler(requireActivity())

        handleAvatar()
        handleGo()
        editTextConfig()
        avatarButtonConfig()
    }

    private fun editTextConfig() {

        val lonEditText = settings_outlinedTextField_lon.editText!!

        lonEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getLatLngFromUser()?.let { userLatLng ->
                    println("APP_DEBUG: WAS CALLED")
                    provideDestinationToMap(userLatLng)
                }
                lonEditText.hideKeyboard()
                requireActivity().window.decorView.clearFocus()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener true
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

    // AVATAR

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { imageUri ->
                    fileHandler.saveImage(DIRECTORY_NAME, AVATAR_FILE_NAME, imageUri)
                    handleAvatar()
                }
            }
        }

    private fun avatarButtonConfig() {
        settings_button_upload.setOnClickListener {
            if (permissionHandler.isWriteExternalStorageGranted(REQUEST_CODE_WRITE_EXTERNAL_STORAGE)) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startForResult.launch(intent)
            }
        }
    }

    private fun handleAvatar() {
        fileHandler.getFile(DIRECTORY_NAME, AVATAR_FILE_NAME).let { avatarFile ->
            if (avatarFile.exists()) {
                settings_image_avatar.setImageDrawable(Drawable.createFromPath(avatarFile.toString()))
                settings_image_avatar.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                settings_image_avatar.setImageResource(R.drawable.ic_user)
                settings_image_avatar.scaleType = ImageView.ScaleType.FIT_XY
            }
        }
    }

}