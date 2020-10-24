package pro.edvard.alcotec.framework.presentation.maps

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pro.edvard.alcotec.R
import pro.edvard.alcotec.business.K
import pro.edvard.alcotec.framework.presentation.settings.SettingsFragment
import pro.edvard.alcotec.framework.presentation.util.FileHandler


@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private lateinit var mMap: GoogleMap
    private lateinit var fileHandler: FileHandler
    private lateinit var currentLocation: LatLng
    private lateinit var markerBitMap: Bitmap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        // Alcotec Office
        currentLocation = LatLng(50.461830, 30.452030)
        setMarkerAndZoom(currentLocation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        configuration()
    }

    private fun configuration() {
        fileHandler = FileHandler(requireContext())
        markerBitMap = getCustomBitmap()
        subscribers()
    }

    private fun subscribers() {

        setFragmentResultListener(SettingsFragment.REQUEST_KEY_DESTINATION) { _, bundle ->
            val result = bundle.getParcelable<LatLng>(SettingsFragment.BUNDLE_KEY_DESTINATION)
            result?.let { latLang ->
                currentLocation = latLang
                setMarkerAndZoom(latLang)
            }
        }

        setFragmentResultListener(SettingsFragment.REQUEST_KEY_AVATAR_UPDATED) { _, bundle ->
            val avatarIsUpdated = bundle.getBoolean(SettingsFragment.BUNDLE_KEY_AVATAR_UPDATED)
            if (avatarIsUpdated) {
                markerBitMap = getCustomBitmap()
                setMarkerAndZoom(currentLocation)
            }
        }

    }

    private fun getCustomBitmap(): Bitmap {
        val conf = Bitmap.Config.ARGB_8888
        val avatarFile =
            fileHandler.getFile(SettingsFragment.DIRECTORY_NAME, SettingsFragment.AVATAR_FILE_NAME)

        val marker = BitmapFactory.decodeResource(resources, R.drawable.img_marker)
        val scaledMarker = Bitmap.createScaledBitmap(marker, 140, 150, false)

        return Drawable.createFromPath(avatarFile.path)?.toBitmap(100, 100, conf)?.let { avatarBitmap ->

            val customBitmap = Bitmap.createBitmap(140, 150, conf)
            val paint = Paint()

            val canvas1 = Canvas(customBitmap)
            canvas1.drawBitmap(scaledMarker, 0f, 0f, paint)

            val shader = BitmapShader(avatarBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            val canvas2 = Canvas(customBitmap)
            paint.isAntiAlias = true
            paint.shader = shader

            canvas2.translate(20f, 10f)
            canvas2.drawCircle(
                avatarBitmap.width / 2f, avatarBitmap.height / 2f,
                50f, paint
            )
            customBitmap
        } ?: scaledMarker
    }

    private fun setMarkerAndZoom(latLang: LatLng) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions()
                .position(latLang)
                .icon(BitmapDescriptorFactory.fromBitmap(markerBitMap))
                .anchor(0.5f, 1f)
        )
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, K.Map.ZOOM_IN_VALUE))

    }

}