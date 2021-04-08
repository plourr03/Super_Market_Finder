package com.example.supermarket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

public class MealRaterDialog extends DialogFragment {

    public interface SaveRatingListener {
        void didFinishMealRaterDialog(float liquer,float produce,float cheese);
    }

    public MealRaterDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_mealrater, container);

        getDialog().setTitle("Rate Meal");

        final RatingBar ratingBarLiq = view.findViewById(R.id.ratingBarLiquer);
        final RatingBar ratingBarProduce = view.findViewById(R.id.ratingBarProduce);
        final RatingBar ratingBarCheese = view.findViewById(R.id.ratingBarCheese);
        Button button = view.findViewById(R.id.button_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float liq = ratingBarLiq.getRating();
                float produce = ratingBarProduce.getRating();
                float cheese = ratingBarCheese.getRating();
                saveRating(liq,produce, cheese);
            }
        });


        return view;
    }

    private void saveRating(float liquer,float produce,float cheese) {
        SaveRatingListener activity = (SaveRatingListener) getActivity();
        activity.didFinishMealRaterDialog(liquer,produce,cheese);
        getDialog().dismiss();
    }

}
