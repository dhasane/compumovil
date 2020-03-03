package com.example.adivinarnumeros.adivinar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adivinarnumeros.R;

import java.util.Random;

public class Adivinar extends AppCompatActivity {

    EditText et;
    int rndNumber;
    int num_intentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adivinar);

        // numero aleatorio
        int superior = getIntent().getIntExtra("rango_superior", 51);
        rndNumber = (new Random() ).nextInt(superior);

        ((TextView) findViewById(R.id.textView2))
                .setText(getString(R.string.adivina_rango) + Integer.toString(superior));
        ((EditText) findViewById(R.id.editText))
                .setHint(getString(R.string.adivina_rango) + Integer.toString(superior));

        System.out.println("el numero es : " +rndNumber);
        num_intentos = 0;
        ( (TextView) findViewById(R.id.textViewIntentos))
                .setText( getString(R.string.numero_intentos) + num_intentos );

        et = findViewById(R.id.editText);

        (findViewById(R.id.verifica))
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int input_int = Integer.parseInt(et.getText().toString());
                // verificar que input_int si sea int

                if (rndNumber== input_int )
                {
                    Toast.makeText(v.getContext(),
                            getString(R.string.adivina_encontrado), Toast.LENGTH_LONG).show();
                    ( (TextView) findViewById(R.id.hintText)).setText( getString(R.string.adivina_encontrado_hint) );
                }
                else
                {
                    Toast.makeText(v.getContext(), getString(R.string.adivina_equivocacion_toast), Toast.LENGTH_LONG).show();
                    ( (TextView) findViewById(R.id.hintText))
                            .setText( getString(R.string.tu_numero_es) + (rndNumber>input_int? getString(R.string.menor) : getString(R.string.mayor)) );
                    num_intentos ++;

                    ( (TextView) findViewById(R.id.textViewIntentos))
                            .setText( getString(R.string.numero_intentos) + num_intentos );

                    ( (LinearLayout) findViewById(R.id.internalLinearLayout))
                            .addView( createTV( Integer.toString(input_int) ) );
                }
            }
        });
    }

    protected TextView createTV( String texto )
    {
        TextView tv = new TextView(this);
        tv.setText(texto);
        return tv;
    }
}
