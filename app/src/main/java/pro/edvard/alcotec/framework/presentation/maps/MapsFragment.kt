package pro.edvard.alcotec.framework.presentation.maps

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pro.edvard.alcotec.R
import pro.edvard.alcotec.framework.presentation.settings.SettingsFragment

@AndroidEntryPoint
class MapsFragment : Fragment(R.layout.fragment_maps) {

    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        val currentLocation = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(currentLocation).title("You"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        subscribers()
    }

    private fun subscribers() {

        setFragmentResultListener(SettingsFragment.REQUEST_KEY_DESTINATION) { _, bundle ->
            val result = bundle.getParcelable<LatLng>(SettingsFragment.BUNDLE_KEY_DESTINATION)
            println("APP_DEBUG: $result")
            result?.let { latLang ->
                mMap.addMarker(MarkerOptions().position(latLang).title("Marker in Sydney"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 16f))
            }

        }
    }



}