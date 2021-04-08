package com.example.supermarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RestaurantAddClass extends AppCompatActivity  {
    private Resturant currentResturant;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_resturant);
        initListButton();
        initToggleButton();
        initHomeButton();
        initAddButton();
        initRateButton();
        initMapButton();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            initRestauant(extras.getInt("resturantID"));
        }
        else{
            currentResturant = new Resturant();
        }
        setForEditing(false);

        initTextChangeEvent();
        initSaveButton();
        hideKeyboard();
    }
    private void initRestauant(int id){
        ResturantDataSource ds = new ResturantDataSource(RestaurantAddClass.this);
        try{
            ds.open();
            currentResturant=ds.getSpecificRestaurant(id);
            ds.close();
        }
        catch (Exception e){
            Toast.makeText(this, "Load ResturantFailed", Toast.LENGTH_LONG).show();
        }
        EditText editName = findViewById(R.id.editTextEditName);
        EditText editAddress = findViewById(R.id.editStreet);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);

        editName.setText(currentResturant.getResturantName());
        editAddress.setText(currentResturant.getStreetAddress());
        editCity.setText(currentResturant.getCity());
        editState.setText(currentResturant.getState());
        editZipCode.setText(currentResturant.getZipCode());
    }

    private void initListButton(){
        ImageButton ibList =findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantAddClass.this, RestaurantListActivity.class);
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
                Intent intent = new Intent(RestaurantAddClass.this, SuperMarketMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initToggleButton(){
        final ToggleButton editToggle = (ToggleButton)findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForEditing(editToggle.isChecked());
            }
        });
    }
    private void initSaveButton(){
        Button saveButton = findViewById(R.id.buttonYas);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasSuccessful;
                hideKeyboard();
                ResturantDataSource ds = new ResturantDataSource(RestaurantAddClass.this);
                try {
                    ds.open();
                    if (currentResturant.getResturantID() == -1){
                        wasSuccessful = ds.insertResturant(currentResturant);
                        if (wasSuccessful){
                            int newId = ds.getLastRestaurantID();
                            currentResturant.setResturantID(newId);
                        }
                    }
                    else{
                        wasSuccessful = ds.updateRestaurant(currentResturant);
                    }
                    ds.close();
                }
                catch (Exception e){
                    wasSuccessful = false;
                }
                if (wasSuccessful){
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }
    private void initAddButton(){
        ImageButton ibAdd =findViewById(R.id.imageButtonAdd);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantAddClass.this, RestaurantAddClass.class);
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
                Intent intent = new Intent(RestaurantAddClass.this, RestaurantListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("clicked rating",-999);
                startActivity(intent);
            }
        });
    }

    private void initHomeButton(){
        ImageButton ibHome =findViewById(R.id.imageButtonHome);
        ibHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantAddClass.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void setForEditing(boolean enabled) {
        EditText editName = findViewById(R.id.editTextEditName);
        EditText editAddress = findViewById(R.id.editStreet);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        Button buttonSave = findViewById(R.id.buttonYas);
        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if(enabled){
            editName.requestFocus();
        }
    }
    private void initTextChangeEvent(){
        final EditText etResturantName = findViewById(R.id.editTextEditName);
        etResturantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentResturant.setResturantName(etResturantName.getText().toString());

            }
        });
        final EditText etStreetAddress = findViewById(R.id.editStreet);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentResturant.setStreetAddress(etStreetAddress.getText().toString());
            }
        });
        final EditText etCity = findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentResturant.setCity(etCity.getText().toString());
            }
        });
        final EditText etState = findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentResturant.setState(etState.getText().toString());
            }
        });
        final EditText etZip = findViewById(R.id.editZipcode);
        etZip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentResturant.setZipCode(etZip.getText().toString());
            }
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText EditName = findViewById(R.id.editTextEditName);
        imm.hideSoftInputFromWindow(EditName.getWindowToken(),0);
        EditText editAddress = findViewById(R.id.editStreet);
        imm.hideSoftInputFromWindow(editAddress.getWindowToken(),0);
        EditText EditCity = findViewById(R.id.editCity);
        imm.hideSoftInputFromWindow(EditCity.getWindowToken(),0);
        EditText editState = findViewById(R.id.editState);
        imm.hideSoftInputFromWindow(editState.getWindowToken(),0);
        EditText editZip = findViewById(R.id.editZipcode);
        imm.hideSoftInputFromWindow(editZip.getWindowToken(),0);
    }

}
