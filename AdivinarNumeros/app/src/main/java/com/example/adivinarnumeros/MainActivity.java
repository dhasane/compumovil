package com.example.adivinarnumeros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adivinarnumeros.adivinar.Adivinar;
import com.example.adivinarnumeros.paises.Paises;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.buttonAdivinanza) )
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = ( (EditText) findViewById(R.id.mainInputText)).getText().toString()  ;
                        if ( !str.isEmpty() )
                        {
                            Intent adivinar = new Intent( getBaseContext(), Adivinar.class);
                            adivinar.putExtra("rango_superior", Integer.parseInt( str ) );
                            startActivity(adivinar);
                        }
                        else
                        {
                            Toast.makeText(v.getContext(),
                                    R.string.toast_correcion, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        ((Button) findViewById(R.id.buttonPaises) )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent( getBaseContext(), Paises.class)
                );
            }
        });
    }
}
