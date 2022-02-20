package edu.uoc.android.PEC3App

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import edu.uoc.android.rest.RetrofitFactory
import edu.uoc.android.rest.models.Museums
import kotlinx.android.synthetic.main.activity_museums.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.NonNull
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var MY_PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION)
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE)



        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true)
                var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location ->
                        val my_location =  LatLng(location.latitude, location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(my_location))
                    }

            }
        }


        //Access the museums API to get the position of
        val call = RetrofitFactory.museumAPI.museums("1", "5")
        call.enqueue(object : Callback<Museums> {
            override fun onResponse(call : Call<Museums>, response : Response<Museums>) {
                if (response.code() == 200){

                    val museums = response.body()!!

                    val elemento = museums.getElements()

                    for (oneElement in elemento) {
                    //getting the localization
                        val latlong = oneElement.localitzacio.split(",".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                        val latitude = java.lang.Double.parseDouble(latlong[0])
                        val longitude = java.lang.Double.parseDouble(latlong[1])
                        val location = LatLng(latitude, longitude)
                        mMap.addMarker(MarkerOptions().position(location).title(oneElement.adrecaNom).icon(BitmapDescriptorFactory.fromResource(R.drawable.museum_icon_resized
                           )))
                    }
                }
            }


            override fun onFailure(call: Call<Museums>, t: Throwable){
                Log.d("Error", "xxxx")
            }
        })
    }

    /**
     * Restart the activity if we allowed permission otherwise will show a toast with an error message
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    finish()
                    startActivity(this.intent)
                }

                else
                {
                    val toast = Toast.makeText(this, getString(R.string.location_permission_error), Toast.LENGTH_SHORT)
                    toast.show()
                }

            }
        }
    }




}
