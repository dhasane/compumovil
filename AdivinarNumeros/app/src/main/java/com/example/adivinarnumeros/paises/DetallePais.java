package com.example.adivinarnumeros.paises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adivinarnumeros.R;

public class DetallePais extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Bundle b = getIntent().getBundleExtra("bundle");

        String nom = b.getString( "nom_int", "");

        ((TextView) findViewById(R.id.detalleNombrePais))
                .setText( b.getString( "nombre", "") );
        ((TextView) findViewById(R.id.detalleCapital))
                .setText( b.getString("capital", "") );
        ((TextView) findViewById(R.id.detalleNomInt))
                .setText( nom );
        ((TextView) findViewById(R.id.detalleSigla))
                .setText( b.getString("sigla", "") );

        ((ImageView) findViewById(R.id.imagenDetallePais))
                .setImageResource(
                        getResources().getIdentifier(
                                nom.toLowerCase(),
                                "drawable",
                                getPackageName()
                                )
                );
    }
}
