    package com.example.capstoneproject.ui.profile

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.asLiveData
    import androidx.lifecycle.viewModelScope
    import com.example.capstoneproject.data.di.Injection
    import com.example.capstoneproject.data.repository.UserRepository
    import com.example.capstoneproject.data.response.Profile
    import com.example.capstoneproject.model.Package
    import com.example.capstoneproject.preferences.SettingPreferences
    import kotlinx.coroutines.launch

    class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

        private val midtransRepository = Injection.provideRepository()
        private val db = Injection.provideDatabaseFirestore()
        private val listService = mutableListOf<Package>()

        private val _isLastTransaction = MutableLiveData<String>()
        val lastTrasaction: LiveData<String> = _isLastTransaction

        private val _isLoading = MutableLiveData<Boolean>()
        val isLoading: LiveData<Boolean> = _isLoading

        init {
            getAllService()
        }

        fun setLoading(condition: Boolean) {
            _isLoading.value = condition
        }

        fun getTheme(): LiveData<Boolean> = repository.getThemeSetting().asLiveData()

        fun saveTheme(isDarkModeActive : Boolean) {
            viewModelScope.launch {
                repository.saveThemeSetting(isDarkModeActive)
            }
        }

        fun getBiometric(): LiveData<Boolean> = repository.getBiometricSetting().asLiveData()

        fun saveBiometric(isBiometricActive : Boolean) {
            viewModelScope.launch {
                repository.saveBiometricSetting(isBiometricActive)
            }
        }

        fun getAllService() {
            db.collection("services")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        val service = Package(
                            document.get("id").toString(),
                            document.get("name").toString(),
                            document.get("speed").toString().toInt(),
                            document.get("period").toString().toInt(),
                            document.get("price").toString().toInt(),
                        )
                        listService.add(service)
                    }
                }
                .addOnFailureListener {
                    Log.e("MainViewModel", "Error: ${it.message}", it)
                }
        }

        fun getService(idService: String): String {
            return listService.find { it.id == idService }?.name ?: "None"
        }

        fun checkStatusTransaction(idOrder: String) {
            midtransRepository.getStatusTransaction(idOrder) { response ->
                response?.let {
                    _isLastTransaction.value = when (response.transactionStatus.toString()) {
                        "settlement" -> "Success"
                        "pending" -> "Pending"
                        else -> "Expired"
                    }
                    Log.d("HomeViewModel", "Something wrong")
                }
            }
        }

//        fun getProfile(): LiveData<Profile> {
//            return repository.getProfile().asLiveData()
//        }

        fun getUID(): LiveData<String> {
            return repository.getUID().asLiveData()
        }

        fun getFirstname(): LiveData<String> {
            return repository.getFirstname().asLiveData()
        }

        fun getLastname(): LiveData<String> {
            return repository.getLastname().asLiveData()
        }

        fun getEmail(): LiveData<String> {
            return repository.getEmail().asLiveData()
        }

        fun logout() {
            viewModelScope.launch {
                repository.logout()
            }
        }
    }