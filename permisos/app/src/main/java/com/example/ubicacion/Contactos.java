package com.example.ubicacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ubicacion.adapters.ContactsAdapter;
import com.example.ubicacion.permisos.Permisos;

public class Contactos extends AppCompatActivity {

    static final int RESPUESTA_CONTACTOS = 1;


    static final int PERMISSION_CONTACTS_ID = 5;
    String[] mProjection;
    Cursor mCursor;
    ContactsAdapter mContactsAdapter;
    ListView mlistaContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        mlistaContactos = findViewById(R.id.lvListaContactos);

        Permisos.requestPermission(this,
                Manifest.permission.READ_CONTACTS,
                "necesito los contactos," +
                        " perro",
                RESPUESTA_CONTACTOS
        );

        mProjection = new String[]{
                ContactsContract.Profile._ID,
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY
        };

        mContactsAdapter = new ContactsAdapter(this, null, 0);
        mlistaContactos.setAdapter(mContactsAdapter);

        Log.i("prueba", " aaa ber pues");
        initView();
    }

    public void initView(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_GRANTED){
            mCursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, mProjection, null, null, null);
            mContactsAdapter.changeCursor(mCursor);
        }else {

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESPUESTA_CONTACTOS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "oiga, como que si dio permiso", Toast.LENGTH_LONG).show();
                    initView();
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