package com.hersonviveros.eliteapartments.ui.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivitySavedBinding
import com.hersonviveros.eliteapartments.ui.adapter.ImagesAdapter
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.BaseActivity
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT
import com.hersonviveros.eliteapartments.utils.Constants.Companion.EMPTY
import com.hersonviveros.eliteapartments.utils.Permissions
import com.hersonviveros.eliteapartments.utils.RecyclerViewAnimationUtils
import com.hersonviveros.eliteapartments.utils.convertInt
import com.hersonviveros.eliteapartments.utils.convertStr
import com.hersonviveros.eliteapartments.utils.setupDragDrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@Suppress("LABEL_NAME_CLASH")
@AndroidEntryPoint
class SavedActivity : BaseActivity() {

    private lateinit var binding: ActivitySavedBinding
    private val viewModel: PropertyViewModel by viewModels()
    private var selectedItem: String = EMPTY
    private lateinit var permissionsHandler: Permissions
    private lateinit var photoAdapter: ImagesAdapter
    private lateinit var property: PropertyEntity
    private var imageUris = mutableListOf<Uri>()

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
            resultImages(uris)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionsHandler = Permissions(this)
        setupToolbar()
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

    private fun updateListImages() {
        photoAdapter = ImagesAdapter { updateList ->
            imageUris.clear()
            imageUris.addAll(updateList)
        }
        binding.rvImages.adapter = photoAdapter
        binding.rvImages.setupDragDrop(photoAdapter)
    }

    private fun resultImages(uris: List<Uri>) {
        imageUris.clear()
        if (uris.size < 5) {
            showToast(getString(R.string.sube_fotos))
        }  else {
            updateListImages()
        }
        imageUris.addAll(uris)

        photoAdapter.setData(imageUris)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.create_new_properties)
        binding.include.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun openImages() {
        permissionsHandler.checkAndRequestPermission(
            Permissions.PermissionType.READ_STORAGE
        ) { isGranted ->
            if (isGranted) {
                // Perform storage write operation
                pickImages.launch("image/*")
            } else {
                showToast(getString(R.string.faltan_permisos))
            }
        }

    }

    private fun savedData() {
        if (imageUris.size < 5) {
            showToast(getString(R.string.sube_fotos))
            return
        }
        val uriStrings = convertUriListToStringList(imageUris)
        property = PropertyEntity(
            propertyType = selectedItem,
            maxGuests = binding.editTextMaxGuests.convertInt(this),
            beds = binding.editTextBeds.convertInt(this),
            bathrooms = binding.editTextBaths.convertInt(this),
            title = binding.editTextTitle.convertStr(this),
            description = binding.editTextDescription.convertStr(this),
            photos = uriStrings,
            location = listOf()
        )
        viewModel.validProperty(property)
    }

    private fun observe() {
        viewModel.listProperties()

        viewModel.typesProperties.observe(this) { list ->
            configureSpinner(list)
        }

        viewModel.validationState.observe(this) { state ->
            if (state == null) return@observe
            when (state) {
                PropertyViewModel.ValidationState.VALID -> {
                    showToast(getString(R.string.propiedad_valida))

                    val intent = Intent(this, MapsActivity::class.java)
                    intent.putExtra(DATA_INTENT, property)
                    startActivity(intent)
                    finish()
                }

                PropertyViewModel.ValidationState.INVALID -> {
                    showToast(getString(R.string.datos_numeros_incorrectos))
                }

                PropertyViewModel.ValidationState.EMPTY_FIELDS -> {
                    showToast(getString(R.string.complete_todos))
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
                }
            }
    }

    private fun setupPhotoRecyclerView() {
        photoAdapter = ImagesAdapter{}
        binding.rvImages.adapter = photoAdapter
        RecyclerViewAnimationUtils.runLayoutAnimation(binding.rvImages)
    }

    private fun convertUriListToStringList(uriList: List<Uri?>): List<String> {
        return uriList.mapNotNull {
            saveImageToInternalStorage(it!!)
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        val fileName = "property_${System.currentTimeMillis()}.jpg"

        val file = File(filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return file.absolutePath
    }

    private fun permission() {
        permissionsHandler.checkAndRequestPermission(
            Permissions.PermissionType.WRITE_STORAGE
        ) {}
        permissionsHandler.checkAndRequestPermission(
            Permissions.PermissionType.READ_STORAGE
        ) {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}