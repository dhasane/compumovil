package com.example.ubicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ubicacion.permisos.Permisos;

public class Camara extends AppCompatActivity {

    static final int RESPUESTA_CAMARA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        Permisos.requestPermission(this,
                Manifest.permission.CAMERA,
                "necesito veeeeer," +
                        " perro",
                RESPUESTA_CAMARA
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESPUESTA_CAMARA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "oiga, como que si dio permiso", Toast.LENGTH_LONG).show();
                    // permissionwasgranted, continuewithtaskrelatedtopermission
                }
                else {
                    // permissiondenied, disablefunctionalitythatdependsonthispermission.
                }
                return;
            }
        }
    }
}
