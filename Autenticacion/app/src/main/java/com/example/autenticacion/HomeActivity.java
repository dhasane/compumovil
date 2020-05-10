package com.example.autenticacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

//    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();

//        database = FirebaseDatabase.getInstance();

        mAuth.getUid();

//        ((Button) findViewById(R.id.btnSaveUserInfo)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyUser usr = new
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.menuCierre){
            // Firebase sign out
            mAuth.signOut();
            Intent intent = new Intent(
                    HomeActivity.this, MainActivity.class
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
//        else if (itemClicked == R.id.menuSettings){
////Abrir actividad para configuración etc
//        }
        return super.onOptionsItemSelected(item);
    }
}
