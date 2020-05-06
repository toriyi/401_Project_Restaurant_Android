package com.example.a401_project_restaurant;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.firebase.firestore.FirebaseFirestore;



import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 */
public class DiscountsPageFragment extends Fragment {

    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Restaurant> restaurants;
    private int[] imgs = {R.drawable.greenleafgourmetchopshop, R.drawable.dulce, R.drawable.cava, R.drawable.honeybird};
    private FirebaseLoader firebaseLoader = FirebaseLoader.getInstance();


    public DiscountsPageFragment() {
        restaurants = firebaseLoader.getRestaurantsWithDiscount();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discounts_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = getView().findViewById(R.id.listView);

        //create adapter to populate list with restaurant data
        MyAdapter adapter = new MyAdapter(this.getActivity(), this.restaurants, this.imgs);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    //switch to detailed discount view for restaurant clicked on
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    ResInfo resInfo = new ResInfo("Discounts Page", restaurants.get(position));
                    fragmentTransaction.replace(R.id.frameLayout, resInfo);
                    fragmentTransaction.commit();
            }
        });

    }



    class MyAdapter extends ArrayAdapter<Restaurant> {
        Context context;
        ArrayList<Restaurant> restaurants;
        int restaurantImgs[];

        MyAdapter(Context c, ArrayList<Restaurant> restaurants, int imgs[]) {
            super(c, R.layout.row, R.id.restaurantTitle, restaurants);

            this.context = c;
            this.restaurants = restaurants;
            this.restaurantImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.restaurantImage);

            //initialize views for a row
            TextView restaurantTitle = row.findViewById(R.id.restaurantTitle);
            TextView restaurantDescription = row.findViewById(R.id.restaurantDescription);
            TextView restaurantRating = row.findViewById(R.id.ratingSign);
            TextView restaurantDiscount = row.findViewById(R.id.discountText);

            //set data for each row
            images.setImageResource(restaurantImgs[position]);
            restaurantTitle.setText((position + 1) + ". " + restaurants.get(position).getName());
            restaurantDescription.setText(restaurants.get(position).getDescription());
            restaurantRating.setText(String.valueOf(restaurants.get(position).getRating()));
            restaurantDiscount.setText(String.valueOf(restaurants.get(position).getDiscountAmount())+ "%");

            return row;
        }
    }

}
