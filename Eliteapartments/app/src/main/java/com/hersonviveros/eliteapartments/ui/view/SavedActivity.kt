package com.hersonviveros.eliteapartments.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivitySavedBinding
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBinding
    private val viewModel : PropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun savedData(){
        val property = PropertyEntity(
            propertyType = binding.spinnerPropertyType.toString(),
            maxGuests = binding.editTextMaxGuests.text.toString().toInt(),
            beds = binding.editTextBeds.text.toString().toInt(),
            bathrooms = binding.editTextBaths.text.toString().toInt(),
            title = binding.editTextTitle.text.toString(),
            description = binding.editTextDescription.text.toString(),
            photos = listOf(),  // Aquí deberías implementar la carga de fotos
            location = "" // Implementa la integración con el mapa
        )
        viewModel.addProperty(property)
    }
}