package pro.edvard.alcotec.framework.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import club.appster.randy.framework.util.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import pro.edvard.alcotec.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var networkConnection: NetworkConnection
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handling Network Connection
        networkConnection = NetworkConnection(this)
        handleNetworkConnection()

        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.main_container) as NavHostFragment? ?: return
        navController = host.navController

    }

    private fun handleNetworkConnection() {
        networkConnection.observe(this, { isConnected ->
            isConnected?.let {
                handleInternetUI(isConnected)
            } ?: handleInternetUI(false)
        })
    }

    private fun handleInternetUI(available: Boolean) {
        if (available) {
            println("DEBUG: INTERNET CONNECTED")
            internet_container.visibility = View.GONE
            main_container.visibility =  View.VISIBLE
        } else {
            println("DEBUG: INTERNET DISCONNECTED")
            internet_container.visibility = View.VISIBLE
            main_container.visibility =  View.GONE
        }
    }

}