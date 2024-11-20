package com.hersonviveros.eliteapartments.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.databinding.ActivityPropertyBinding
import com.hersonviveros.eliteapartments.ui.adapter.PropertyAdapter
import com.hersonviveros.eliteapartments.ui.viewmodel.PropertyViewModel
import com.hersonviveros.eliteapartments.utils.PhotoItemTouchHelperCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    private val viewModel: PropertyViewModel by viewModels()
    private val propertyAdapter: PropertyAdapter by lazy { PropertyAdapter(::moreClickedAdapter) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observer()
        setupPhotoRecyclerView()

    }

    private fun observer() {
        viewModel.allProperties()
        viewModel.propertyAllList.observe(this) { listProperties ->
            if (listProperties.isNotEmpty()) {
                propertyAdapter.setData(listProperties!!)
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }

    }

    private fun setupPhotoRecyclerView() {
        binding.tvProperties.apply {
            binding.tvProperties.layoutManager = LinearLayoutManager(applicationContext)
            binding.tvProperties.adapter = propertyAdapter
            val touchHelper = ItemTouchHelper(PhotoItemTouchHelperCallback(propertyAdapter))
            touchHelper.attachToRecyclerView(binding.tvProperties)
        }
    }

    private fun moreClickedAdapter(propertyEntity: PropertyEntity) {

    }


    private fun setupToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.properties)
        binding.include.toolbar.setNavigationOnClickListener { finish() }
    }
}