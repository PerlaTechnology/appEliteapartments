package com.hersonviveros.eliteapartments.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivityDetailBinding
import com.hersonviveros.eliteapartments.ui.adapter.ImagesAdapter
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.BaseActivity
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT
import com.hersonviveros.eliteapartments.utils.Constants.Companion.DATA_INTENT_DETAIL
import com.hersonviveros.eliteapartments.utils.Constants.Companion.TYPE_DETAIL
import com.hersonviveros.eliteapartments.utils.PhotoItemTouchHelperCallback
import com.hersonviveros.eliteapartments.utils.RecyclerViewAnimationUtils
import com.hersonviveros.eliteapartments.utils.fromHtml
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val photoAdapter = ImagesAdapter()
    private lateinit var entity: PropertyEntity
    private val viewModel: PropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        configureBundle()
        setupPhotoRecyclerView()
        viewData()

        binding.btnViewMap.setOnClickListener {
            intentGoMap()
        }
        binding.btnDelete.setOnClickListener {
            showDeletePropertyDialog()
        }

    }

    private fun intentGoMap() {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(DATA_INTENT, entity)
        intent.putExtra(DATA_INTENT_DETAIL, TYPE_DETAIL)
        startActivity(intent)
        finish()
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

    private fun viewData() {
        photoAdapter.setData(convertStringListToUriList(entity.photos))

        val title = "<h1>${entity.title}</h1>"
        binding.tvTitle.text = title.fromHtml()

        val details = String.format(
            "<h3>${entity.description}</h3><br>" +
                    "<b>%s:</b> ${entity.propertyType}<br>" +
                    "<b>%s:</b> ${entity.maxGuests}<br>" +
                    "<b>%s:</b> ${entity.beds}<br>" +
                    "<b>%s:</b> ${entity.bathrooms}<br>",
            getString(R.string.tipo),
            getString(R.string.huesped),
            getString(R.string.camas),
            getString(R.string.banos),
        )

        binding.tvSubTitle.text = details.fromHtml()
    }

    private fun showDeletePropertyDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Se eliminara constantemente")

        builder.setMessage("¿Confirma la eliminación de la propiedad?")
            .setPositiveButton("Sí") { dialog, _ ->
                viewModel.deleteProperty(entity)
                dialog.dismiss()
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        // Crear el AlertDialog y mostrarlo
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun convertStringListToUriList(stringList: List<String>): List<Uri> {
        return stringList.mapNotNull {
            val file = File(it)
            if (file.exists()) {
                Uri.fromFile(file)
            } else {
                null
            }
        }
    }


    private fun setupPhotoRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.setLayoutManager(layoutManager)
        binding.rvImages.adapter = photoAdapter
        val touchHelper = ItemTouchHelper(PhotoItemTouchHelperCallback(photoAdapter))
        touchHelper.attachToRecyclerView(binding.rvImages)
        RecyclerViewAnimationUtils.runLayoutAnimation(binding.rvImages)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.detalles)
        binding.include.toolbar.setNavigationOnClickListener { finish() }
    }
}