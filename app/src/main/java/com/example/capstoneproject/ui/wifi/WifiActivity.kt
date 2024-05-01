package com.example.capstoneproject.ui.wifi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.HotspotAdapter
import com.example.capstoneproject.data.response.ListWifiResponse
import com.example.capstoneproject.data.response.WifiLocation
import com.example.capstoneproject.databinding.ActivityWifiBinding
import com.example.capstoneproject.model.Hotspot
import com.example.capstoneproject.preferences.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.math.floor
import com.example.capstoneproject.data.result.Result
import com.example.capstoneproject.databinding.CustomInfoWindowBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker

class WifiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiBinding
    private lateinit var viewModel: WifiViewModel
    private lateinit var mMap: GoogleMap
    private val MY_LOCATION_REQUEST_CODE = 123
    private var mapFragment: SupportMapFragment? = null
    private var latitudeUser: Double = 0.0
    private var longitudeUser: Double = 0.0
    private var radius: Double = 500.0 // 50000000.0 //dalam meter
    private var zoom: Float = 16f
    private var isUserMarkerSet = false
    private lateinit var circleOptions: CircleOptions
    private var listNearestWifi: MutableList<WifiLocation> = mutableListOf()
    private lateinit var listHotspot: MutableList<Hotspot>
    private var listWifi: MutableMap<String, WifiLocation> = mutableMapOf()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).apply {
        setWaitForAccurateLocation(false)
        setMinUpdateIntervalMillis(1000)
        setMaxUpdateDelayMillis(10000)
    }.build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult?.lastLocation?.let { location ->
                // Handle the latest location here
                val latitude = location.latitude
                val longitude = location.longitude
                updateLocationOnMap(latitude, longitude)
            }
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
//        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style))
        updateOnClickMaps()

        mMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoWindow(marker: Marker): View? {
                return null // Return null to use getInfoContents()
            }

            override fun getInfoContents(marker: Marker): View {
                val wifiData = marker.tag as? WifiLocation
                val infoWindowLayout = LayoutInflater.from(this@WifiActivity).inflate(R.layout.custom_info_window, null)
                val binding = CustomInfoWindowBinding.bind(infoWindowLayout)
                if (wifiData?.ssid.toString() == "Your Location" || wifiData?.ssid.toString() == "Clicked Location") {
                    binding.tvWifiName.text = wifiData?.name
                    binding.tvDescription.visibility = View.GONE
                    binding.tvCity.visibility = View.GONE
                } else {
                    binding.tvWifiName.text = wifiData?.name
                    binding.tvDescription.text = wifiData?.description
                    binding.tvCity.text = "City: " + wifiData?.city
                }
                Log.d("Wifi", wifiData?.name.toString())
                Log.d("Wifi", wifiData?.description.toString())

                return infoWindowLayout
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[WifiViewModel::class.java]

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupToolbar()

        binding.btnCurrentLocation.setOnClickListener {
            mMap.clear()
            getCurrentLocation()
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.getToken().observe(this) { token ->
            val tokens = "Bearer $token"
            viewModel.getListWifi(tokens).observe(this) { result ->
                Log.d("Wifi", "Token: $tokens")
                getListWifi(result)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MAPS", "WIFI ACTIVITY")
                recreate()
            } else {
                Log.d("MAPS", "MAIN ACTIVITY")
                Toast.makeText(this, "Please give access to device location", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }

    private fun updateLocationOnMap(latitude: Double, longitude: Double) {
        latitudeUser = latitude
        longitudeUser = longitude
        Log.d("Wifi", "$latitude $longitude")
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the location permissions are not granted, request them
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_LOCATION_REQUEST_CODE
            )
        } else {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the location permissions are not granted, request them
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_LOCATION_REQUEST_CODE
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Handle the last known location
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    updateLocationOnMap(latitude, longitude)
                }

                listWifi["Your Location"] = WifiLocation(
                    "Your Location",
                    "Your Location",
                    "Your Location",
                    "",
                    "Indonesia",
                    "$latitudeUser $longitudeUser",
                )
                val location = MarkerOptions()
                    .position(LatLng(latitudeUser, longitudeUser))
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                val marker = mMap.addMarker(location)
                marker?.tag = listWifi["Your Location"]
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitudeUser, longitudeUser), zoom))
                createCircleArea(latitudeUser, longitudeUser)

//              Menambahkan lingkaran ke peta
                mMap.addCircle(circleOptions)
                findNearestLocation(LatLng(latitudeUser, longitudeUser))
            }
        }
    }

    private fun getLocation() {
        Log.d("Wifi", "Checking permission")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Wifi", "Permission granted")
//            mMap.clear()
            getCurrentLocation()
        } else {
            // Jika belum diizinkan, meminta izin lokasi
            Log.d("Location", "Requesting permission")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_LOCATION_REQUEST_CODE)
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun getListWifi(result: Result<ListWifiResponse>) {
        result?.let {
            when (it) {
                is Result.Loading -> {
                    viewModel.setLoading(true)
                    println("Loading in Result.Loading")
                }

                is Result.Success -> {
                    viewModel.setLoading(false)
                    Toast.makeText(this, "Get List Wifi Successful", Toast.LENGTH_SHORT).show()
                    println("Loading in Result.Success")
                    println(it)
                    listWifi.clear()
                    it.data.data.forEach { wifi ->
                        wifi.values.forEach { wifiLocation ->
                            listWifi[wifi.keys.toString()] = wifiLocation
                            val ssid = wifiLocation.ssid
                            val name = wifiLocation.name
                            val coordinate = wifiLocation.coordinate
                            Log.d("Wifi", "ID: ${wifi.keys} SSID: $ssid, Name: $name Coordinate: $coordinate")
//                                    Log.d("Wifi", "SSID: $ssid, Name: $name, Description: $description, City: $city, Country: $country, Coordinate: $coordinate")
                        }
                    }
                    getLocation()
                }

                is Result.Error -> {
                    viewModel.setLoading(false)
                    Toast.makeText(this, "Get List Wifi Failed", Toast.LENGTH_SHORT).show()
                    println("Loading in Result.Error")
                    println(it.toString())
                }
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.container.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            binding.container.visibility = View.VISIBLE
        }
    }

    private fun updateOnClickMaps() {
        mMap.setOnMapClickListener { latLng ->
            mMap.clear()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
            createCircleArea(latLng.latitude, latLng.longitude)

            // Menambahkan lingkaran ke peta
            mMap.addCircle(circleOptions)
            val locationClicked = MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("Click Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            val marker = mMap.addMarker(locationClicked)
            marker?.tag = WifiLocation(
                "Clicked Location",
                "Clicked Location",
                "Clicked Location",
                "",
                "Indonesia",
                "0.0 0.0",
            )
            findNearestLocation(LatLng(latLng.latitude, latLng.longitude))
        }
    }

    private fun createCircleArea(lat: Double, lon: Double) {
        circleOptions = CircleOptions()
            .center(LatLng(lat, lon))
            .radius(radius)
            .strokeWidth(3f)
            .strokeColor(Color.parseColor("#FF01A1DD"))
            .fillColor(Color.parseColor("#2501A1DD"))
    }

    // get distances between two location (in kilometer)
    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515 * 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun findNearestLocation(currentLatLng: LatLng) {
//        if (isUserMarkerSet) {
            listWifi["Your Location"] = WifiLocation(
                "Your Location",
                    "Your Location",
                    "Your Location",
                    "",
                    "Indonesia",
                    "$latitudeUser $longitudeUser",
            )
//        }
        listNearestWifi.clear()
        for (data in listWifi) {
            var coordinates = data.value.coordinate
            var (latitude, longitude) = coordinates.split(" ")
            val lat: Double = latitude.toDouble()
            val lon: Double = longitude.toDouble()
            val km = distance(currentLatLng.latitude, currentLatLng.longitude, lat, lon)
            val meter = floor(km * 1000 * 100) / 100
            if (meter <= radius) {
                val location: MarkerOptions
                if (LatLng(lat, lon) == LatLng(latitudeUser, longitudeUser)) {
                    location = MarkerOptions()
                        .position(LatLng(lat, lon))
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                } else {
                    location = MarkerOptions()
                        .position(LatLng(lat, lon))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(data.value.name)
                    listNearestWifi.add(data.value)
                }
                val marker = mMap.addMarker(location)
                marker?.tag = data.value
            }
        }
        setupListWifi(listNearestWifi)
    }

    private fun setupListWifi(listNearestWifi: MutableList<WifiLocation>) {
        binding.rvHotspot.setHasFixedSize(true)
        binding.rvHotspot.layoutManager = LinearLayoutManager(this)
        binding.tvEmptyHotspot.visibility = if (listNearestWifi.isEmpty()) View.VISIBLE else View.GONE
        val adapter = HotspotAdapter(listNearestWifi)
        binding.rvHotspot.adapter = adapter
    }
}