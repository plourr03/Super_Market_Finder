package com.example.supermarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter{
    private ArrayList<Resturant> restaurantData;
    private View.OnClickListener mOnClickListener;
    private boolean isDeleting;
    private Context parentContext;

    public class ResturantViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewRestauant;
        public TextView textRating;
        public Button deleteButton;
        public ResturantViewHolder(@NonNull View itemView){
            super(itemView);
            textViewRestauant = itemView.findViewById(R.id.textRestauantName);
            textRating = itemView.findViewById(R.id.textStreet2);
            deleteButton = itemView.findViewById(R.id.buttonDeleteRestauant);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnClickListener);
        }

        public TextView getRestaurantTextView(){
            return textViewRestauant;
        }
        public TextView getTextRating(){
            return textRating;
       }
        public Button getDeleteButton(){
            return deleteButton;
        }
    }
    public RestaurantAdapter(ArrayList<Resturant> names,Context context)
    {
        restaurantData = names;
        parentContext = context;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnClickListener=itemClickListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ResturantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResturantViewHolder cvh = (ResturantViewHolder) holder;
        cvh.getRestaurantTextView().setText(restaurantData.get(position).getResturantName());

        String i;
        if(restaurantData.get(position).getLiquerRating()==0&&restaurantData.get(position).getCheeseRating()==0&&restaurantData.get(position).getProduceRating()==0)
        {
            i="No Rating yet";

        } else {
            float liquerRating = restaurantData.get(position).getLiquerRating();
            float produceRating = restaurantData.get(position).getProduceRating();
            float cheeseRating = restaurantData.get(position).getCheeseRating();
            float j = (liquerRating + produceRating + cheeseRating) / 3;
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            j = Float.valueOf(twoDForm.format(j));
            i = "☆"+Float.toString(j);
        }
        cvh.getTextRating().setText(i);
        if (isDeleting){
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }
    public void setDelete(boolean b){
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Resturant resturant = restaurantData.get(position);
        ResturantDataSource ds =new ResturantDataSource(parentContext);
        try{
            ds.open();
            boolean didDelete = ds.deleteResturant(resturant.getResturantID());
            ds.close();
            if (didDelete){
                restaurantData.remove(position);
                notifyDataSetChanged();
            }
            else {
                Toast.makeText(parentContext,"Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e){
            Toast.makeText(parentContext,"Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public int getItemCount() {
        return restaurantData.size();
    }

}
