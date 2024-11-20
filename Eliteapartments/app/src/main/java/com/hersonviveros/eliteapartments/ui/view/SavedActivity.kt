package com.hersonviveros.eliteapartments.ui.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivitySavedBinding
import com.hersonviveros.eliteapartments.ui.adapter.ImagesAdapter
import com.hersonviveros.eliteapartments.ui.adapter.PhotoItemTouchHelperCallback
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.Constants.Companion.EMPTY
import com.hersonviveros.eliteapartments.utils.Constants.Companion.REQUEST_CODE_READ_MEMORY
import com.hersonviveros.eliteapartments.utils.Constants.Companion.REQUEST_CODE_WRITE_MEMORY
import com.hersonviveros.eliteapartments.utils.Permissions
import com.rengwuxian.materialedittext.MaterialEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBinding
    private val viewModel: PropertyViewModel by viewModels()
    private var selectedItem: String = EMPTY
    private val photoAdapter = ImagesAdapter()

    private val imageUris = mutableListOf<Uri>()

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            if (uris.size > 5) {
                //Notificar al usuario que solo puede seleccionar hasta 5 imágenes
                showToast("SOlo 5 imagenes")
                return@registerForActivityResult
            }
            imageUris.clear()
            imageUris.addAll(uris)
            // Ahora puedes guardar las imágenes o hacer cualquier otra cosa con ellas
            photoAdapter.setData(imageUris)

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observe()
        permission()
        setupPhotoRecyclerView()

        binding.btnSaveProperty.setOnClickListener {
            savedData()
        }

        binding.btnAddPhotos.setOnClickListener {
            openImages()
        }

    }

    private fun setupToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.create_new_properties)
    }

    private fun openImages() {
        Permissions(this).takePermission(REQUEST_CODE_READ_MEMORY) { bool ->
            if (bool) {
                pickImages.launch("image/*")
            }
        }
    }

    private fun savedData() {
        val property = PropertyEntity(
            propertyType = binding.spinnerPropertyType.toString(),
            maxGuests = convertInt(binding.editTextMaxGuests),
            beds = convertInt(binding.editTextBeds),
            bathrooms = convertInt(binding.editTextBaths),
            title = convertStr(binding.editTextTitle),
            description = convertStr(binding.editTextDescription),
            photos = listOf(),  // Aquí deberías implementar la carga de fotos
            location = "" // Implementa la integración con el mapa
        )
        viewModel.addProperty(property)
    }

    private fun observe() {
        viewModel.listProperties()

        viewModel.typesProperties.observe(this) { list ->
            configureSpinner(list)
        }

        viewModel.propertyAllList.observe(this) { listProperties ->
            if (listProperties.isNotEmpty()) {
                //photoAdapter.setData(listProperties!!)
            }
        }

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

    private fun configureSpinner(list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPropertyType.adapter = adapter

        binding.spinnerPropertyType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedItem = parent?.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Opcional: Acción a realizar si no se selecciona ningún elemento
                }
            }
    }

    private fun setupPhotoRecyclerView() {
        binding.rvImages.adapter = photoAdapter

        val touchHelper = ItemTouchHelper(PhotoItemTouchHelperCallback(photoAdapter))
        touchHelper.attachToRecyclerView(binding.rvImages)
    }

    private fun permission() {
        Permissions(this).takePermission(REQUEST_CODE_WRITE_MEMORY) {}
        Permissions(this).takePermission(REQUEST_CODE_READ_MEMORY) {}
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun convertStr(edt: MaterialEditText): String {
        val tex = edt.text.toString()
        if (tex.isEmpty()) {
            edt.error = getString(R.string.error_edt)
        }
        return tex
    }

    private fun convertInt(edt: MaterialEditText): Int {
        var tex = edt.text.toString()
        if (tex.isEmpty()) {
            edt.error = getString(R.string.error_edt)
            tex = "0"
        }
        return tex.toInt()
    }
}