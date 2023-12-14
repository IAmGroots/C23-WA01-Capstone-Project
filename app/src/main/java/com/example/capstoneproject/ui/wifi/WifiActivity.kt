package com.example.capstoneproject.ui.wifi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstoneproject.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.adapter.HotspotAdapter
import com.example.capstoneproject.databinding.ActivityWifiBinding
import com.example.capstoneproject.model.Hotspot
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

class WifiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiBinding
    private val viewModel by viewModels<WifiViewModel>()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val MY_LOCATION_REQUEST_CODE = 123
    private var mapFragment: SupportMapFragment? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var radius: Double = 500.0
    private var zoom: Float = 16f
    private var isUserMarkerSet = false
    private lateinit var circleOptions: CircleOptions
    private var listNearestHotspot: MutableList<Hotspot> = mutableListOf()
    private lateinit var listHotspot: MutableList<Hotspot>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupToolbar()

        viewModel.listHotspot.observe(this) { data ->
            listHotspot = data.toMutableList()
        }

        binding.btnCurrentLocation.setOnClickListener {
            getCurrentLocation()
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

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        getLocation()
        updateOnClickMaps()
    }

    private fun getCurrentLocation() {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                zoom
            )
        )

        mMap.clear()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), zoom))
        createCircleArea(latitude, longitude)

        // Menambahkan lingkaran ke peta
        mMap.addCircle(circleOptions)
        findNearestLocation(LatLng(latitude, longitude))
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Handle the last known location
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    createCircleArea(latitude, longitude)

                    // Menambahkan lingkaran ke peta
                    mMap.addCircle(circleOptions)

                    findNearestLocation(LatLng(latitude, longitude))

                    if (!isUserMarkerSet) {
                        // Menambahkan marker posisi pengguna yang baru
                        val userLocation = LatLng(latitude, longitude)
                        val markerOptions = MarkerOptions()
                            .position(userLocation)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        mMap.addMarker(markerOptions)
                        isUserMarkerSet = true
                    }

                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(latitude, longitude),
                            zoom
                        )
                    )
                }
            }

        } else {
            // Jika belum diizinkan, meminta izin lokasi
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_LOCATION_REQUEST_CODE
            )
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
            mMap.addMarker(locationClicked)
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
        if (isUserMarkerSet) {
            listHotspot.add(
                Hotspot(
                    0,
                    "Your Location",
                    "",
                    "",
                    latitude,
                    longitude
                )
            )
        }
        listNearestHotspot.clear()
        for (data in listHotspot) {
            val km = distance(currentLatLng.latitude, currentLatLng.longitude, data.lat, data.lon)
            val meter = floor(km * 1000 * 100) / 100
            if (meter <= radius) {
                val location: MarkerOptions
                if (LatLng(data.lat, data.lon) == LatLng(latitude, longitude)) {
                    location = MarkerOptions()
                        .position(LatLng(data.lat, data.lon))
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                } else {
                    location = MarkerOptions()
                        .position(LatLng(data.lat, data.lon))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(data.name)
                    listNearestHotspot.add(data)
                }
                mMap.addMarker(location)
            }
        }
        setupListHotspot(listNearestHotspot)
    }

    private fun setupListHotspot(listNearestHotspot: MutableList<Hotspot>) {
        binding.rvHotspot.setHasFixedSize(true)
        binding.rvHotspot.layoutManager = LinearLayoutManager(this)
        binding.tvEmptyHotspot.visibility = if (listNearestHotspot.isEmpty()) View.VISIBLE else View.GONE
        val adapter = HotspotAdapter(listNearestHotspot)
        binding.rvHotspot.adapter = adapter
    }
}