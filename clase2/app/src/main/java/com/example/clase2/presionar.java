package com.example.clase2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class presionar extends AppCompatActivity {

    int reps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presionar);
        reps = 0;

        ( (Button) findViewById( R.id.btnveces ) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reps++;
            }
        });

        ( (Button) findViewById( R.id.btnsig ) ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( v.getContext(), mostrar.class );
                Bundle bd = new Bundle();
                bd.putString( "casilla", findViewById( R.id.editTextAMostrar ).toString() );
                bd.putInt( "veces", reps );
                it.putExtra( "bundle", bd );
                startActivity(it);
            }
        });

    }
}
