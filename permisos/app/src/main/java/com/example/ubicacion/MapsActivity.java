package com.example.ubicacion;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ubicacion.permisos.Permisos;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static final int RESPUESTA_UBICACION = 3;
    static final int REQUEST_CHECK_SETTINGS = 7;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private GoogleMap mMap;
    private Marker markerPersona;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightSensorListener;

    private Geocoder mGeocoder ;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        location();

        sensor();

        this.tv = findViewById(R.id.tvMapas);

        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    String addressString = tv.getText().toString();
                    LatLng position = String2Location(addressString, 2);

                    if(mMap != null && position != null)
                    {
                        agregarUbicacion(position);
                    }
                }
                return false;
            }
        });
    }

    // inicializa lo relacionado a la iluminacion
    private void sensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lightSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap != null) {

                    if (event.values[0] < 5000) {
                        Log.i("MAPS", "DARK MAP " + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.dark_style_map));
                    } else {
                        Log.i("MAPS", "LIGHT MAP " + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.light_syle_map));
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }

    // inicializa lo relacionado a la ubicacion
    private void location() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Permisos.requestPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                "necesito la ubicacioooon,",
                RESPUESTA_UBICACION
        );

        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Location location = locationResult.getLastLocation();
                Log.i("Latitude" , "Location update in the callback : " + location );
                setNewMarker(location);
            }
        };

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().
                addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {// Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(
                                    MapsActivity.this,
                                    REQUEST_CHECK_SETTINGS
                            );
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        } break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. No way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

        mGeocoder = new Geocoder(getBaseContext());
    }

    private void setNewMarker(Location location) {
        if( mMap != null && location != null)
        {
            LatLng marca = new LatLng( location.getLatitude(), location.getLongitude() );
            Toast.makeText(this, "cambia la ubicacion a lat:" + marca.latitude + " lon:" + marca.longitude , Toast.LENGTH_LONG).show();
            if (markerPersona != null) markerPersona.remove();
            markerPersona = mMap.addMarker(
                    new MarkerOptions()
                            .position(marca)
                            .icon(
                                    BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            )
            );
            JSONObject jo = new JSONObject();
            try{
                jo.put("lat", location.getLatitude());
                jo.put("lon", location.getLongitude());
                jo.put("alt", location.getAltitude());
                writeJSONObject( jo );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void agregarUbicacion(LatLng marca){
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marca));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marca, 14));
            try{
                Toast.makeText(this, "cambia la ubicacion a lat:" + marca.latitude + " lon:" + marca.longitude , Toast.LENGTH_LONG).show();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(marca)
                                .title(
                                        // consigue la ubicacion con geocoder, pasandole las coordenadas
                                        mGeocoder.getFromLocation(
                                                marca.latitude, marca.longitude, 1
                                        ).toString()
                                )
                                .icon(
                                        BitmapDescriptorFactory
                                                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
                                )
                );
            } catch (IOException e ) {
                e.printStackTrace();
            }
        }
    }

    private LatLng String2Location(String addressString, int maxResults){
        if (!addressString.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(addressString, maxResults);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    return new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                } else {
                    Toast.makeText(MapsActivity.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {Toast.makeText(MapsActivity.this, "La dirección esta vacía", Toast.LENGTH_SHORT).show();}
        return null;
    }

    private void writeJSONObject(JSONObject jo){
        Writer output = null;
        String filename= "locations.json";
        try {
            File file = new File(getBaseContext().getExternalFilesDir(null), filename);
            Log.i("FILE", "guardando " + jo.toString() + " en : " + file);
            output = new BufferedWriter(new FileWriter(file));
            output.write(jo.toString());
            output.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(10000);
        // tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000);
        // máxima tasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates() {
        //Verificaciónde permiso!!
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightSensorListener);
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Permisos.requestPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                "necesito la ubicacioooon,",
                RESPUESTA_UBICACION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESPUESTA_UBICACION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissionwasgranted, continuewithtaskrelatedtopermission
                    startLocationUpdates();
                    Toast.makeText(this, "ya se donde estoy!!", Toast.LENGTH_LONG).show();
                }
                else {
                    // permissiondenied, disablefunctionalitythatdependsonthispermission.
                    Toast.makeText(this, "Para el funcionamiento correcto de esta aplicacion es necesario activar la ubicacion", Toast.LENGTH_LONG).show();
                }
                return;
            }
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

        mMap.moveCamera(CameraUpdateFactory.zoomTo(10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                agregarUbicacion(latLng);
            }
        });
        LatLng bog = new LatLng(4.6256, -74.0718);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bog));

    }
}
