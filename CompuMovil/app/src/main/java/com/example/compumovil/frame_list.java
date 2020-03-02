package com.example.compumovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class frame_list extends AppCompatActivity {

    String[] paises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,paises);

        ((ListView) findViewById(R.id.frameList)).setAdapter(adapter);
    }

    private void fillString()
    {
        paises = new String[5];
        paises[0] = "colombia";
        paises[1] = "argentina";
        paises[2] = "peru";
        paises[3] = "venezuela";
        paises[4] = "brazil";
    }
}
