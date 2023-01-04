package com.example.findingfalcone.presentation.find

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.findingfalcone.R
import com.example.findingfalcone.application.extensions.getViewModel
import com.example.findingfalcone.application.extensions.observe
//import com.example.findingfalcone.databinding.ActivityFindBinding
import com.example.findingfalcone.domain.model.FindResponse
import com.example.findingfalcone.domain.model.Planet
import com.example.findingfalcone.domain.model.Vehicle
import com.example.findingfalcone.presentation.MainActivity
import com.example.findingfalcone.presentation.find.FindViewModel.Loading
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_find.*
import javax.inject.Inject

class FindActivity : AppCompatActivity() {

    companion object {
        private const val PLANETS = "planets"
        private const val VEHICLES = "vehicles"

        fun intent(context: Context, planets: ArrayList<Planet>, vehicles: ArrayList<Vehicle>) =
            Intent(context, FindActivity::class.java).apply {
                putExtra(PLANETS, planets)
                putParcelableArrayListExtra(VEHICLES, vehicles)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: FindViewModel
    private lateinit var tvTryAgain: TextView
//    private lateinit var binding : ActivityFindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        initViews()
        viewModel = getViewModel(viewModelFactory)
        viewModel.message.observe(this, onChange = ::showMessage)
        viewModel.viewState.observe(this, onChange = ::renderView)
        viewModel.loading.observe(this, onChange = ::renderLoading)

        val selectedPlanet =
            requireNotNull(intent.getParcelableArrayListExtra<Planet>(PLANETS)) { "Planets list not provided" }
        val selectedVehicle =
            requireNotNull(intent.getParcelableArrayListExtra<Vehicle>(VEHICLES)) { "Vehicle list not provided" }
        viewModel.init(selectedPlanet, selectedVehicle)

        tvTryAgain.setOnClickListener{
            val intent = Intent(this@FindActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun initViews(){
        tvTryAgain = findViewById(R.id.tvTryAgain)
    }

    private fun renderView(state: FindResponse) {
        when (state) {
            is FindResponse.Success -> resultText.text = getString(R.string.princes_found_on, state.planetName)
            is FindResponse.Failure -> resultText.text = getString(R.string.princes_not_found)
        }
        resultText.visibility = View.VISIBLE
    }

    private fun renderLoading(loading: Loading) {
        when (loading) {
            Loading.Show -> progressLoading.visibility = View.VISIBLE
            Loading.Hide -> progressLoading.visibility = View.GONE
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}