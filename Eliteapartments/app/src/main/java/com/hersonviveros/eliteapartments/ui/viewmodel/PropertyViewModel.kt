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

    private val _validationState = MutableLiveData<ValidationState>()
    val validationState: LiveData<ValidationState> get() = _validationState

    private val _propertyList = MutableLiveData<List<PropertyEntity>>()
    val propertyList: LiveData<List<PropertyEntity>> = _propertyList

    private val _typesProperties = MutableLiveData<List<String>>()
    val typesProperties: LiveData<List<String>> = _typesProperties

    enum class ValidationState {
        VALID,
        INVALID,
        EMPTY_FIELDS
    }

    fun listProperties() {
        _typesProperties.value = listOf(
            "Apartamento",
            "Casa",
            "Local comercial",
            "Penthouses",
            "Duplex",
            "Estudio",
            "Casa de Playa",
            "Casa de Campo"
        )
    }

    private fun validateProperty(property: PropertyEntity): Boolean {
        if (property.propertyType.isBlank() || property.title.isBlank() ||
            property.description.isBlank() || property.location.isBlank()
        ) {
            _validationState.value = ValidationState.EMPTY_FIELDS
            return false
        }

        if (property.maxGuests <= 0 || property.beds <= 0 || property.bathrooms <= 0) {
            _validationState.value = ValidationState.INVALID
            return false
        }

        if (property.photos.isEmpty()) {
            _validationState.value = ValidationState.EMPTY_FIELDS
            return false
        }

        _validationState.value = ValidationState.VALID
        return true
    }

    fun addProperty(property: PropertyEntity) {
        viewModelScope.launch {
            if (!validateProperty(property)) return@launch
            repository.insertProperty(property)
            _propertyList.value = repository.getAllProperties()
        }
    }
}
