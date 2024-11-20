package com.hersonviveros.eliteapartments.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hersonviveros.eliteapartments.data.database.entities.PropertyEntity
import com.hersonviveros.eliteapartments.data.repository.PropertyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val repository: PropertyRepository
) : ViewModel() {

    private val _propertyList = MutableLiveData<List<PropertyEntity>>()
    val propertyList: LiveData<List<PropertyEntity>> = _propertyList

    fun addProperty(property: PropertyEntity) {
        viewModelScope.launch {
            repository.insertProperty(property)
            _propertyList.value = repository.getAllProperties()
        }
    }
}
