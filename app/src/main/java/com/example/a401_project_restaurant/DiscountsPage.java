package com.example.a401_project_restaurant;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class DiscountsPage extends AppCompatActivity {

    BottomNavigationView navigation;
    FrameLayout frameLayout;

    //Fragments:
    private DiscountsPageFragment discountsPageFragment;
//    private FavoritesFragment favoritesFragment;
    private MapPageFragment mapPageFragment;
    private UserAccountFragment userAccountFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts_page);

        navigation = findViewById(R.id.bottomNavigationView2);
        frameLayout = findViewById(R.id.frameLayout);

        //fragment initialization
        discountsPageFragment = new DiscountsPageFragment();
//        favoritesFragment = new FavoritesFragment();
        mapPageFragment = new MapPageFragment();
        userAccountFragment = new UserAccountFragment();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //Switch to select which case is chosen:

                switch(menuItem.getItemId()){
                    case R.id.navigation_discountpage:
                        InitializeFragment(discountsPageFragment);
                        return true;

                    case R.id.navigation_mappage:
                        InitializeFragment(mapPageFragment);
                        return true;

//                    case R.id.navigation_favoritespage:
//                        InitializeFragment(favoritesFragment);
//                        return true;

                    case R.id.navigation_useraccountpage:
                        InitializeFragment(userAccountFragment);
                        return true;
                }

                return false;
            }
        });
    }

    private void InitializeFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
}
