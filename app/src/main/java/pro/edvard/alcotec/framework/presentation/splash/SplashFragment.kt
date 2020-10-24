package pro.edvard.alcotec.framework.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import pro.edvard.alcotec.R
import pro.edvard.alcotec.business.K

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var navController : NavController
    private var viewIsBackFromBackground = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        start()
    }

    override fun onResume() {
        super.onResume()
        if (viewIsBackFromBackground) {
            navController.navigate(R.id.action_splashFragment_to_mainFragment)
        }
    }

    override fun onStop() {
        super.onStop()
        viewIsBackFromBackground = true
    }

    private fun start() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!viewIsBackFromBackground) {
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
            }
        }, K.Time.SPLASH_DELAY_TIME)
    }

}