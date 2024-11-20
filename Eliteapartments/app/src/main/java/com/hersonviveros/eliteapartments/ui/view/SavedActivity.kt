package com.hersonviveros.eliteapartments.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivitySavedBinding
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.rengwuxian.materialedittext.MaterialEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBinding
    private val viewModel: PropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observe()

    }

    private fun savedData() {
        val property = PropertyEntity(
            propertyType = binding.spinnerPropertyType.toString(),
            maxGuests = convert(binding.editTextMaxGuests).toInt(),
            beds = convert(binding.editTextBeds).toInt(),
            bathrooms = convert(binding.editTextBaths).toInt(),
            title = convert(binding.editTextTitle),
            description = convert(binding.editTextDescription),
            photos = listOf(),  // Aquí deberías implementar la carga de fotos
            location = "" // Implementa la integración con el mapa
        )
        viewModel.addProperty(property)
    }

    private fun observe() {
        viewModel.validationState.observe(this) { state ->
            if (state == null) return@observe
            when (state) {
                PropertyViewModel.ValidationState.VALID -> {
                    //Si es válido, podemos proceder con el guardado o actualización
                    showToast("La propiedad es válida")
                }

                PropertyViewModel.ValidationState.INVALID -> {
                    //Si los valores no son válidos (por ejemplo, enteros negativos)
                    showToast("Hay datos incorrectos, revise los campos numéricos.")
                }

                PropertyViewModel.ValidationState.EMPTY_FIELDS -> {
                    //Si faltan campos por llenar
                    showToast("Por favor, complete todos los campos.")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun convert(edt: MaterialEditText): String {
        val tex = edt.text.toString()
        if (tex.isEmpty()) {
            edt.error = getString(R.string.error_edt)
        }
        return tex
    }
}