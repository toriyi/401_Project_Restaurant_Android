package com.example.a401_project_restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Favorites extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        ImageView a;
        a = (ImageView)findViewById(R.id.imageView);;
        a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                Intent myIntent = new Intent(Favorites.this, ResInfo.class);
                startActivity(myIntent);
            }
        });
    }
}
