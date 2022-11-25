package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.benlinux.realestatemanager.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        // Set async google map
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        // When map is loaded
        mGoogleMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }
}