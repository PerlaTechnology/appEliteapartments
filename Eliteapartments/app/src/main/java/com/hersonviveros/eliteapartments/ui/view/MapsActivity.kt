package com.hersonviveros.eliteapartments.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentConfigure()

    }

    private fun fragmentConfigure(){
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map!!
        val location = LatLng(37.7749, -122.4194) // Ubicación predeterminada
        googleMap.addMarker(MarkerOptions().position(location).title("Ubicación de la propiedad"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

        googleMap.setOnMapClickListener { latLng ->
            // Guardar la nueva ubicación
            val locationString = "${latLng.latitude},${latLng.longitude}"
        }
    }
}