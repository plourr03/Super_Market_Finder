package com.example.supermarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MealRater extends AppCompatActivity implements MealRaterDialog.SaveRatingListener {
    private Resturant currentRestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        initRateButton();
        initSaveButton();
        initAddButton();
        initListButton();
        initHomeButton();
        initMapButton();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
                initRestaurant(extras.getInt("resturantID"));
        }
        else{
            currentRestaurant = new Resturant();
        }

    }
    private void initMapButton(){
        ImageButton ibList =findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealRater.this, SuperMarketMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initRateButton() {
        Button button = findViewById(R.id.buttonRateLiquer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                MealRaterDialog mealRaterDialog = new MealRaterDialog();
                mealRaterDialog.show(fragmentManager, "RateMeal");
            }
        });

    }

    private void initHomeButton(){
        ImageButton ibHome =findViewById(R.id.imageButtonHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealRater.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSaveButton(){
        Button saveButton = findViewById(R.id.buttonSaveRating);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TextView liquer = findViewById(R.id.textViewLiquerDepartment);
                    TextView produce = findViewById(R.id.textViewProduceDepartment);
                    TextView cheese = findViewById(R.id.textViewCheese);
                    String liq = liquer.getText().toString();
                    String pro = produce.getText().toString();
                    String che = cheese.getText().toString();
                    String s=liq.substring(1,4);
                    String o=pro.substring(1,4);
                    String a=che.substring(1,4);
                    float l = Float.parseFloat(s);
                    float p = Float.parseFloat(o);
                    float c = Float.parseFloat(a);
                    currentRestaurant.setLiquerRating(l);
                    currentRestaurant.setProduceRating(p);
                    currentRestaurant.setCheeseRating(c);

                    ResturantDataSource ds = new ResturantDataSource(MealRater.this);
                    try {
                        ds.open();
                        ds.updateRestaurant(currentRestaurant);
                        ds.close();
                        Toast.makeText(getBaseContext(), "updated the rating!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "error updating Restaurant rating", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(MealRater.this, RestaurantListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Please make sure that all ratings are filled out", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void initRestaurant(int resturantID) {
        ResturantDataSource ds = new ResturantDataSource(MealRater.this);
        try{
            ds.open();
            currentRestaurant=ds.getSpecificRestaurant(resturantID);
            ds.close();

        }
        catch (Exception e){
            Toast.makeText(this, "Load Restaurant Failed", Toast.LENGTH_LONG).show();
        }

        TextView restaurantName = findViewById(R.id.textViewSuperName);
        restaurantName.setText(currentRestaurant.getResturantName());

    }



    @Override
    public void didFinishMealRaterDialog(float liquer,float produce,float cheese) {
        TextView liq = findViewById(R.id.textViewLiquerDepartment);
        TextView produced = findViewById(R.id.textViewProduceDepartment);
        TextView cheesed = findViewById(R.id.textViewCheese);
        Button save = findViewById(R.id.buttonSaveRating);
        liq.setText("☆"+String.valueOf(liquer));
        produced.setText("☆"+String.valueOf(produce));
        cheesed.setText("☆"+String.valueOf(cheese));
        save.setVisibility(View.VISIBLE);
    }
    private void initAddButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonAdd);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealRater.this, RestaurantAddClass.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initListButton(){
        ImageButton ibList =findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealRater.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
