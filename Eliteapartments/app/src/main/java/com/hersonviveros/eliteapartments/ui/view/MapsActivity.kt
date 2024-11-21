package com.hersonviveros.eliteapartments.ui.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.Position
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivityMapsBinding
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.BaseActivity
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT_DETAIL
import com.hersonviveros.eliteapartments.utils.Constants.Companion.TYPE_DETAIL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var entity: PropertyEntity
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0
    private lateinit var position: Position
    private val viewModel: PropertyViewModel by viewModels()
    private var viewType: Int = 0

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
            val bundle: Bundle? = intent.extras
            viewType = bundle?.getInt(DATA_INTENT_DETAIL) ?: 0

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

        if (viewType == TYPE_DETAIL) {

            map.clear()

            val boundsBuilder = LatLngBounds.Builder()

            entity.location.forEach { location ->
                val latLng = LatLng(location.latitud, location.longitud)

                map.addMarker(
                    MarkerOptions()
                        .position(latLng)
                )

                boundsBuilder.include(latLng)
            }
            //Ajustar cámara
            try {
                val bounds = boundsBuilder.build()
                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                map.moveCamera(cameraUpdate)

            } catch (e: IllegalStateException) {
                finish()
                e.printStackTrace()
            }

        } else {
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
                        .title(getString(R.string.ubicacion_propiedad))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )

                // Mover cámara al punto seleccionado
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                // Configuración del listener para el marcador
                map.setOnMarkerClickListener {
                    showLocationSavedDialog()
                    true
                }
            }
        }

    }


    private fun showLocationSavedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.title_saved))

        builder.setMessage(getString(R.string.subtitle_saved))
            .setPositiveButton(getString(R.string.si)) { dialog, _ ->
                // Aquí agregas el código para guardar la ubicación
                saveLocation()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
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
            Toast.makeText(this, getString(R.string.selected_ubication), Toast.LENGTH_SHORT).show()
        }
    }
}