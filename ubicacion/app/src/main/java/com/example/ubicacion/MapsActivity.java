package com.example.ubicacion;

import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<JSONObject> ubicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Bundle b = getIntent().getBundleExtra("bundle");
        String arrayString = getIntent().getStringExtra("array");

        this.ubicaciones = new ArrayList<JSONObject>();

        try {
            JSONArray array = new JSONArray(arrayString);
            for (int i=0;i<array.length();i++) {
                this.ubicaciones.add( (JSONObject) array.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        // Add a marker in Sydney and move the camera

        for( JSONObject jo : this.ubicaciones )
        {
            try{
                Log.i("ubicacion",
                        " lat : " + jo.get("lat")
                                + " lon : " + jo.get("lon")
                                + " alt : " + jo.get("alt")
                );
                LatLng marca = new LatLng( jo.getDouble("lat"), jo.getDouble("lon") );

                mMap.addMarker(
                        new MarkerOptions()
                                .position(marca)
                                .icon(
                                        BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                                )
                );
//                        .title("Marker in Sydney"));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(marca));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
