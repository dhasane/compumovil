package com.example.compumovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et;
    Button button;
    Button btn;
    Button btn2;
    int veces;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);
        veces = 0;

        // et = findViewById(R.id.loginText);
        // button = findViewById(R.id.loginButton);
        // button.setOnClickListener(new View.OnClickListener() {
            // @Override
            // public void onClick(View v) {
                // // Toast.makeText(v.getContext(), "bienvenido "+ et , Toast.LENGTH_LONG  ).show();
            // }
        // });


//        btn = findViewById(R.id.btn1);
 //       btn.setOnClickListener(new View.OnClickListener() {
  //          @Override
   //         public void onClick(View v) {
    //            veces ++;
     //           Toast.makeText(v.getContext(), "se ha presionado " + veces + " este botón" , Toast.LENGTH_LONG  ).show();
//
 //           }
  //      });

   //     btn2 = findViewById(R.id.btn1);


        Button web = findViewById(R.id.webButton);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( v.getContext() , web_frame.class ));
            }
        });

        Button frame = findViewById(R.id.webButton);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( v.getContext() , web_frame.class ));
//                WebView wv = findViewById();
            }
        });

        Button fl = findViewById(R.id.btnFrameList);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( v.getContext() , frame_list.class ));
            }
        });
    }
}
