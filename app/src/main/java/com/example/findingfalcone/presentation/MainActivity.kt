package com.example.findingfalcone.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.findingfalcone.R
import com.example.findingfalcone.application.extensions.getViewModel
import com.example.findingfalcone.application.extensions.observe
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.presentation.MainViewModel.Loading
import com.example.findingfalcone.presentation.MainViewModel.NavigationEvent
import com.example.findingfalcone.presentation.MainViewModel.ViewState
import com.example.findingfalcone.presentation.find.FindActivity
import com.example.findingfalcone.presentation.vehicleselection.VehicleSelectionActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        private const val VEHICLE_SELECT_REQUEST = 123
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModel(viewModelFactory)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.loading.observe(this, onChange = ::renderLoading)
        viewModel.navigation.observe(this, onChange = ::navigate)

        planet1Layout.setOnClickListener { viewModel.onPlanetClicked(planet1Layout.getPlanetName()) }
        planet2Layout.setOnClickListener { viewModel.onPlanetClicked(planet2Layout.getPlanetName()) }
        planet3Layout.setOnClickListener { viewModel.onPlanetClicked(planet3Layout.getPlanetName()) }
        planet4Layout.setOnClickListener { viewModel.onPlanetClicked(planet4Layout.getPlanetName()) }
        planet5Layout.setOnClickListener { viewModel.onPlanetClicked(planet5Layout.getPlanetName()) }
        planet6Layout.setOnClickListener { viewModel.onPlanetClicked(planet6Layout.getPlanetName()) }

        btnFind.setOnClickListener { viewModel.onFindClicked() }

        viewModel.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && resultCode == Activity.RESULT_OK && requestCode == VEHICLE_SELECT_REQUEST) {
            val planet =
                requireNotNull(data.getParcelableExtra<Planet>(VehicleSelectionActivity.PLANET)) { "planet is missing" }
            viewModel.onActivityResult(
                planet,
                data.getParcelableExtra(VehicleSelectionActivity.VEHICLE)
            )
        }
    }

    private fun ViewGroup.getPlanetName(): String = (getChildAt(1) as TextView).text.toString()

    // seems we can find a more elegant solution here and for the planets selection ans assignment, but out of time for now
    private fun renderView(state: ViewState) {
        val planets = state.planetVehicle.keys.toList()
        planet1Layout.visibility = View.VISIBLE
        planet1Name.text = planets[0].name
        state.planetVehicle[planets[0]]?.let { planet1Shuttle.setImageResource(it.getImage()) }

        planet2Layout.visibility = View.VISIBLE
        planet2Name.text = planets[1].name
        state.planetVehicle[planets[1]]?.let { planet2Shuttle.setImageResource(it.getImage()) }

        planet3Layout.visibility = View.VISIBLE
        planet3Name.text = planets[2].name
        state.planetVehicle[planets[2]]?.let { planet3Shuttle.setImageResource(it.getImage()) }

        planet4Layout.visibility = View.VISIBLE
        planet4Name.text = planets[3].name
        state.planetVehicle[planets[3]]?.let { planet4Shuttle.setImageResource(it.getImage()) }

        planet5Layout.visibility = View.VISIBLE
        planet5Name.text = planets[4].name
        state.planetVehicle[planets[4]]?.let { planet5Shuttle.setImageResource(it.getImage()) }

        planet6Layout.visibility = View.VISIBLE
        planet6Name.text = planets[5].name
        state.planetVehicle[planets[5]]?.let { planet6Shuttle.setImageResource(it.getImage()) }

        btnFind.isEnabled = state.isFindEnabled
    }

    private fun renderLoading(loading: Loading) {
        when (loading) {
            Loading.Show -> progressLoading.visibility = View.VISIBLE
            Loading.Hide -> progressLoading.visibility = View.GONE
        }
    }

    private fun navigate(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.ShowVehicleSelection ->
                startActivityForResult(
                    VehicleSelectionActivity.intent(
                        this,
                        planet = event.selectedPlanet,
                        vehicles = event.vehicles
                    ),
                    VEHICLE_SELECT_REQUEST
                )

            is NavigationEvent.ShowResult -> {
                startActivity(FindActivity.intent(this, event.planets, event.vehicles))
                finish()
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}