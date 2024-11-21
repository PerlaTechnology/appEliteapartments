package com.hersonviveros.eliteapartments.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.databinding.ActivityMainBinding
import com.hersonviveros.eliteapartments.utils.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCreateProperty.setOnClickListener {
            startActivity(Intent(this, SavedActivity::class.java))
        }

        binding.btnViewProperty.setOnClickListener {
            startActivity(Intent(this, PropertyActivity::class.java))
        }

        binding.btnTheme.setOnClickListener {
           toggleTheme()
        }

    }

}