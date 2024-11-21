package com.hersonviveros.eliteapartments.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.Position
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivityMapsBinding
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var entity: PropertyEntity
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    private lateinit var position: Position
    private val viewModel: PropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        configureBundle()
        fragmentConfigure()

    }

    private fun configureBundle() {
        try {
            entity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra(
                    DATA_INTENT,
                    PropertyEntity::class.java
                ) as PropertyEntity
            } else {
                intent.getSerializableExtra(DATA_INTENT) as PropertyEntity
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.ubicacion_propiedad)
        binding.include.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun fragmentConfigure() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Configuración inicial del mapa
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isZoomControlsEnabled = true

        // Listener de clics en el mapa
        map.setOnMapClickListener { latLng ->
            // Limpiar marcadores previos
            map.clear()

            // Actualizar coordenadas
            selectedLatitude = latLng.latitude
            selectedLongitude = latLng.longitude

            // Añadir marcador donde el USUARIO hace clic
            map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Ubicación de la Propiedad")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )

            // Mover cámara al punto seleccionado
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

            // Configuración del listener para el marcador
            map.setOnMarkerClickListener { marker ->
                showLocationSavedDialog()
                true
            }
        }
    }


    private fun showLocationSavedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Confirma la ubicación de la propiedad?")

        builder.setMessage("La ubicación no ha sido guardada. ¿Deseas guardarla ahora?")
            .setPositiveButton("Sí") { dialog, _ ->
                // Aquí agregas el código para guardar la ubicación
                saveLocation()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        // Crear el AlertDialog y mostrarlo
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun saveLocation() {
        if (selectedLatitude != 0.0 && selectedLongitude != 0.0) {
            position = Position(
                selectedLatitude,
                selectedLongitude
            )
            entity.location = listOf(position)

            viewModel.createProperty(entity)
            startActivity(Intent(this, PropertyActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Selecciona una ubicación", Toast.LENGTH_SHORT).show()
        }
    }
}