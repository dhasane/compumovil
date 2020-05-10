package com.example.ubicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ubicacion.permisos.Permisos;

public class Camara extends AppCompatActivity {

    static final int RESPUESTA_CAMARA = 2;
    static final int REQUEST_IMAGE_CAPTURE = 8;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        Permisos.requestPermission(this,
                Manifest.permission.CAMERA,
                "necesito veeeeer",
                RESPUESTA_CAMARA
        );
        mImageView = findViewById(R.id.imageViewFoto);

        ((Button) findViewById(R.id.buttonTakeFoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }


    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
