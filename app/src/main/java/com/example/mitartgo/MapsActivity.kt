package com.example.mitartgo

import android.content.Intent
import com.example.mitartgo.R
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.net.URL


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback

    private lateinit var markers: MutableCollection<Marker>
    //temp
    private lateinit var arts : MutableCollection<Map<String,String>>



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {

            var updating = false
            override fun onLocationResult(locationResult: LocationResult?) {
                if (updating) return
                updating = true
                locationResult ?: return
                for (location in locationResult.locations) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
                updating = false
            }
        }
    }



    private fun LocationUpdates() {
        val locationRequest = LocationRequest.create()
        val priority = PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.priority = priority
        locationRequest.interval =  3000

        locationRequest.numUpdates = 99999 //set to Int.MAX_VALUE


        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.maplayout));
        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true

// 2

        mMap.uiSettings.setMapToolbarEnabled(false);
        mMap.uiSettings.isMyLocationButtonEnabled = false;
        mMap.uiSettings.isScrollGesturesEnabled = false
        LocationUpdates()
        gatherArtPieces()
        mMap.setOnMarkerClickListener(this);
    }

    private fun gatherArtPieces(){
        var art = mapOf("image" to "https://listart.mit.edu/sites/default/files/styles/slideshow/public/Bertoia_Altarpiece%20for%20MIT%20Chapel3.jpg", "title" to "Altarpiece for MIT Chapel", "artist" to "Harry Bertoia", "date" to "1955", "medium" to "Brazed steel", "size" to "240 in. (609.6 cm)", "credit" to "Commissioned for Eero Saarinen Chapel, MIT", "location" to "MIT Chapel, 48 Massachusetts Ave, Cambridge, MA 02139", "description" to "Bertoia’s altarpiece screen, or reredos, was commissioned for Eero Saarinen’s early modernist, non-denominational MIT Chapel in 1955. Suspended over the main altar, his cascading, open fret screen of slim metal rods and crossplates scatters light throughout the chapel. Described as one of Bertoia’s most striking works, it is an integral part of the altar. Here, Bertoia has liberated sculpture from its base to usher in the contemporary era of spatial sculpture.")


        //print(art)
        //arts.add(art)
        val name = art["image"]
        val url_value = URL(name)
        var bMap = BitmapFactory.decodeResource(getResources(), R.drawable.chapel)
        var b = Bitmap.createScaledBitmap(bMap,150,150,false)

        var mark = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(42.358420,-71.093680))
                .title(art.get("title"))
                .icon(BitmapDescriptorFactory.fromBitmap(b))

        )

        Log.d("dbg", "Marker: " + mark.title)

        //markers.add(mark)

    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("dbg","User clicked " + p0.title)
        val intent = Intent(this, ArtDetailActivity::class.java)
        startActivity(intent)
        return false
    }
}
