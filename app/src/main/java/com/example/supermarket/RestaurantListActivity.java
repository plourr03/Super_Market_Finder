package com.example.supermarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RestaurantListActivity extends AppCompatActivity {
    ArrayList<Resturant> resturants;
    private RestaurantAdapter restaurantAdapter;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            int resturantID = resturants.get(position).getResturantID();
            Intent intent = new Intent(RestaurantListActivity.this, MealRater.class);
            intent.putExtra("resturantID",resturantID);
            startActivity(intent);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restauant_list);
        initListButton();
        intiAddresturantsButton();
        initDeleteSwitch();
        initHomeButton();
        initAddButton();
        initRateButton();
        initMapButton();
        ResturantDataSource ds = new ResturantDataSource(this);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.getInt("clicked rating")==-999){
                Toast.makeText(this,"Please tap a Restaurant to rate", Toast.LENGTH_LONG).show();
            }
        }
        try {
            ds.open();
            resturants = ds.getRestaurants();
            ds.close();
            RecyclerView resturantsList = findViewById(R.id.rvRestaurants);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            resturantsList.setLayoutManager(layoutManager);
            restaurantAdapter = new RestaurantAdapter(resturants,this);
            restaurantAdapter.setOnItemClickListener(onItemClickListener);
            resturantsList.setAdapter(restaurantAdapter);
        }
        catch (Exception e) {
            Toast.makeText(this,"Error retrieving resturants", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
         ResturantDataSource ds = new ResturantDataSource(this);

        try {
            ds.open();
            resturants = ds.getRestaurants();
            ds.close();
            if (resturants.size()>0) {
                RecyclerView resturantsList = findViewById(R.id.rvRestaurants);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                resturantsList.setLayoutManager(layoutManager);
                restaurantAdapter = new RestaurantAdapter(resturants, this);
                restaurantAdapter.setOnItemClickListener(onItemClickListener);
                resturantsList.setAdapter(restaurantAdapter);
            }
            else
            {
                Intent intent = new Intent(RestaurantListActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this,"Error retrieving resturants", Toast.LENGTH_LONG).show();
        }

    }
    private void initAddButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonAdd);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantAddClass.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton(){
        ImageButton ibList =findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, SuperMarketMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initRateButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonRate);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("clicked rating",-999);
                startActivity(intent);
            }
        });
    }



    public void initDeleteSwitch() {
        Switch s =findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean status = buttonView.isChecked();
                restaurantAdapter.setDelete(status);
                restaurantAdapter.notifyDataSetChanged();
            }
        });
    }
    private void initHomeButton(){
        ImageButton ibHome =findViewById(R.id.imageButtonHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void intiAddresturantsButton(){
        Button newresturants = findViewById(R.id.buttonAddResturants);
        newresturants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this,RestaurantAddClass.class);
                startActivity(intent);
            }
        });
    }
    private void initListButton(){
        ImageButton ibList =findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}