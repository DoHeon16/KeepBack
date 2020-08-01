package com.example.keepback

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    lateinit var googleMap: GoogleMap
    //현재 위치 받아올 때 필요
    var fusedLocationClient: FusedLocationProviderClient?=null
    //업데이트 시 필요
    var locationCallback: LocationCallback?=null
    var locationRequest: LocationRequest?=null
    var loc=LatLng(37.554752,126.970631)
    lateinit var myDBHelper: ReadCSV
    lateinit var adapter: ArrayAdapter<String>
    var metro= mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        initLocation()
        initAdapter()
    }

    private fun initAdapter() {
        myDBHelper=ReadCSV(resources.openRawResource(R.raw.place),resources.openRawResource(R.raw.quantity),this)
        metro.clear()
        metro= myDBHelper.makeAdapter("전체", metro)!!//table, adapter 항목 넘겨서 완성된 adapter 다시 받기
        //역명 힌트 어댑터 등록
        station.setOnClickListener {
            station.setText("")
            station.setTextColor(Color.BLACK)
        }
        if(metro!=null){
            adapter=ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,metro)//adapter 등록
            station.setAdapter(adapter)
            station.addTextChangedListener (object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val str=s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }

        //검색 버튼 누르면
        search.setOnClickListener {
            if(station.text.toString()==""){//입력이 안되어 있는 경우
                Toast.makeText(this,"찾으실 역을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            //검색 클릭 시, 역 이름에 일치하는 내용이면
            if(metro.contains(station.text.toString())){
                //액티비티 이동
                var i= Intent(this,MetroActivity::class.java)
                i.putExtra("station",station.text.toString()) //역 정보 넘기기
                startActivity(i)
            }
            else{
                Toast.makeText(this,"입력한 역이 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initLocation(){//위치정보 사용 시, 사용자 권한 승인을 받아야함.
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED&&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getUserLocation()
            startLocationUpdates()
            initMap()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),100)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100){
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                getUserLocation()
                startLocationUpdates()
                initMap()
            }else{
                Toast.makeText(this,"위치정보 승인 거부",Toast.LENGTH_SHORT).show()
                initMap()
            }
        }
    }

    private fun getUserLocation() { //사용자 현재 위치 얻기
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient?.lastLocation?.addOnSuccessListener {
            loc=LatLng(it.latitude,it.longitude)
        }
    }

    private fun startLocationUpdates() {
        locationRequest=LocationRequest.create()?.apply {
            interval=10000
            fastestInterval=8000
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback=object :LocationCallback(){
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                result?:return //null이면 return
                for(location in result.locations){
                    loc= LatLng(location.latitude,location.longitude)
                    googleMap.clear()
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
                    val option= MarkerOptions()
                    option.position(loc)
                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    val mark=googleMap.addMarker(option)
                    mark.showInfoWindow()
                }

            }
        }

        fusedLocationClient?.requestLocationUpdates(
            locationRequest,locationCallback, Looper.getMainLooper())
    }

    fun initMap(){
        val mapFragment=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{
//            googleMap?.mapType=GoogleMap.MAP_TYPE_NORMAL
            googleMap=it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,16.0f))
            googleMap.setMaxZoomPreference(18.0f)
            googleMap.setMinZoomPreference(10.0f)
//            val option= MarkerOptions()
//            option.position(loc)
//            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
//            val mark=googleMap.addMarker(option)
//            mark.showInfoWindow()
        }
    }

    fun stopLocationUpdate(){
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdate()
    }

    override fun onResume() {
        super.onResume()
        initLocation()
    }


}
