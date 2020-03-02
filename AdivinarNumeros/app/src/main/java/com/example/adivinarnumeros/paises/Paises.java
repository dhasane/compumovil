package com.example.adivinarnumeros.paises;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.adivinarnumeros.R;
import com.example.adivinarnumeros.utils.Pais;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Paises extends AppCompatActivity {

    ArrayList<Pais> paises;

    // tambien podria haber creado mi propio arrayAdapter, pero era bastante mas demorado
    // y ya que quedan de la misma longitud, puedo tomar la posicion en una para ver en la otra
    ArrayList<String> nombresPaises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);
        cargarPaises();

        ListView lv = (ListView) findViewById(R.id.theListView);
        lv.setAdapter(
                new ArrayAdapter<String>(this,
                        R.layout.support_simple_spinner_dropdown_item, nombresPaises
                )
        );
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putString( "nombre", paises.get(position).getNombre() );
                b.putString( "capital", paises.get(position).getCapital() );
                b.putString( "nom_int", paises.get(position).getNom_int() );
                b.putString( "sigla", paises.get(position).getSigla() );

                Intent detalle = new Intent(getBaseContext(), DetallePais.class );
                detalle.putExtra("bundle", b);
                startActivity( detalle );
            }
        });
    }

    void cargarPaises()
    {
        paises = new ArrayList<>();
        nombresPaises = new ArrayList<>();
        try {
            JSONObject json = new JSONObject( loadJson() );
            JSONArray jsa = json.getJSONArray("paises");
            for ( int i = 0 ; i < jsa.length() ; i++ )
            {
                JSONObject jso = jsa.getJSONObject(i);
                paises.add(
                        new Pais(
                                jso.getString("nombre_pais"),
                                jso.getString("capital"),
                                jso.getString("nombre_pais_int"),
                                jso.getString("sigla")
                        )
                );
                nombresPaises.add(
                        jso.getString("nombre_pais")
                );
            }
        }
        catch (JSONException jso)
        {
            jso.printStackTrace();
        }
    }

    String loadJson()
    {
        String json= null;
        try {
            InputStream is = this.getAssets().open("paises.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json= new String(buffer, "UTF-8");

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
