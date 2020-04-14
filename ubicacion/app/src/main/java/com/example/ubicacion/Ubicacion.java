package com.example.ubicacion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ubicacion extends AppCompatActivity {

    static final int RESPUESTA_UBICACION = 3;
    static final int REQUEST_CHECK_SETTINGS = 7;

    TextView latitude;
    TextView longitude;
    TextView height;

    private FusedLocationProviderClient mFusedLocationClient;

    private LocationRequest mLocationRequest;
    private Location CallbackmLocationCallback;

    private List<Location> ubicaciones;

    private LocationCallback mLocationCallback;

    private LinearLayout llUbicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        Permisos.requestPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION,
                "necesito la ubicacioooon," +
                        " perro",
                RESPUESTA_UBICACION
        );

        this.ubicaciones = new ArrayList<Location>();
        this.llUbicaciones = findViewById(R.id.arrayUbicaciones);

        ((Button)findViewById(R.id.btnMaps)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps = new Intent(getBaseContext(), MapsActivity.class);
                maps.putExtra( "array", ubicaciones2JSONArray().toString() );
                startActivity(maps);
            }
        });

        latitude = findViewById(R.id.tvLatitude);
        longitude = findViewById(R.id.tvLongitude);
        height = findViewById(R.id.tvElevation);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().
                addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates(); //Todas las condiciones para recibir localizaciones
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
                            Ubicacion.this,
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

        mLocationRequest = createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                Location location = locationResult.getLastLocation();
                Log.i("Latitude" , "Location update in the callback : " + location );

                // que no sea null y que o sea 0 el tam o sea diferente al ultimo que se agrego
                if(location != null && (ubicaciones.size()==0 || !EqualLocations(location, ubicaciones.get(ubicaciones.size()-1))) )
                {
                    ubicaciones.add(location);
                    llUbicaciones.addView( createTV(
                            " lat : " + location.getLatitude()
                            + " lon : " + location.getLongitude()
                            + " alt : " + location.getAltitude()
                    ) );
                    latitude.setText(String.valueOf(location.getLatitude()));
                    longitude.setText(String.valueOf(location.getLongitude()));
                    height.setText(String.valueOf(location.getAltitude()));
                }
            }
        };
    }

    private boolean EqualLocations( Location l1, Location l2 )
    {
        return l1.getLatitude() == l2.getLatitude()
                && l1.getLongitude() == l2.getLongitude()
                && l1.getAltitude() == l2.getAltitude();
    }

    protected JSONArray ubicaciones2JSONArray()
    {
        JSONArray ja = new JSONArray();
        for( Location l : this.ubicaciones )
        {
            JSONObject jo = new JSONObject();
            try {

                jo.put("lat", l.getLatitude() );
                jo.put("lon", l.getLongitude() );
                jo.put("alt", l.getAltitude() );

                ja.put(jo);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return ja;
    }

    protected TextView createTV( String texto )
    {
        TextView tv = new TextView(this);
        tv.setText(texto);
        return tv;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this,
                            "localización habilitada!",
                            Toast.LENGTH_LONG).show();
                    startLocationUpdates(); //Se encendió la localización!!!
                } else {
                    Toast.makeText(this,
                    "Sin acceso a localización, hardware deshabilitado!",
                    Toast.LENGTH_LONG).show();
                }
            return;
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(10000);
        //tasa de refresco en milisegundos
        mLocationRequest.setFastestInterval(5000);
        // máximatasa de refresco
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates() {
        //Verificaciónde permiso!!
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESPUESTA_UBICACION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "oiga, como que si dio permiso", Toast.LENGTH_LONG).show();
                    // permissionwasgranted, continuewithtaskrelatedtopermission
                    startLocationUpdates();
                }
                else {
                    // permissiondenied, disablefunctionalitythatdependsonthispermission.
                    Toast.makeText(this, "Para el funcionamiento correcto de esta aplicacion es necesario activar la ubicacion", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
