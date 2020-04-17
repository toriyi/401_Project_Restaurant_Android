package com.example.a401_project_restaurant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

//    BottomNavigationView navigation;
//    FrameLayout frameLayout;

    //Fragments:
//    private DiscountsPageFragment discountsPageFragment;
//    private FavoritesFragment favoritesFragment;
//    private MapPageFragment mapPageFragment;
//    private UserAccountFragment userAccountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main);
//        navigation = findViewById(R.id.bottomNavigationView2);
//        frameLayout = findViewById(R.id.frameLayout);
//
//        //fragment initialization
//        discountsPageFragment = new DiscountsPageFragment();
//        favoritesFragment = new FavoritesFragment();
//        mapPageFragment = new MapPageFragment();
//        userAccountFragment = new UserAccountFragment();
//
//        InitializeFragment(discountsPageFragment);
//
//        System.out.println("Hello!!!!!");
//
//        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                //Switch to select which case is chosen:
//
//                switch(menuItem.getItemId()){
//                    case R.id.navigation_discountpage:
//                        InitializeFragment(discountsPageFragment);
//                        return true;
//
//                    case R.id.navigation_mappage:
//                        InitializeFragment(mapPageFragment);
//                        return true;
//
//                    case R.id.navigation_favoritespage:
//                        InitializeFragment(favoritesFragment);
//                        return true;
//
//                    case R.id.navigation_useraccountpage:
//                        InitializeFragment(userAccountFragment);
//                        return true;
//                }
//
//                return false;
//            }
//        });
    }

    /** Called when the user taps the Send button */
    public void signUp(View view) {
        Intent intent = new Intent(this, DiscountsPage.class);
        startActivity(intent);
    }

//    private void InitializeFragment(Fragment fragment)
//    {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//
//    }
}
