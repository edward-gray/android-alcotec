package pro.edvard.alcotec.framework.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import pro.edvard.alcotec.R
import pro.edvard.alcotec.framework.presentation.maps.MapsFragment
import pro.edvard.alcotec.framework.presentation.settings.SettingsFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val manager = requireActivity().supportFragmentManager
        manager.beginTransaction().replace(R.id.main_fragment_settings, SettingsFragment()).commit()
        manager.beginTransaction().replace(R.id.main_fragment_maps, MapsFragment()).commit()
    }

}