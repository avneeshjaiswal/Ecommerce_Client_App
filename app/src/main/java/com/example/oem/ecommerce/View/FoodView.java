package com.example.oem.ecommerce.View;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.R;

/**
 * Created by avneesh jaiswal on 06-Feb-18.
 */

public class FoodView extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView textFoodName;
    public ImageView imgFoodImage,imgFavImage,quickCart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }


    public FoodView(View itemView) {
        super(itemView);

        textFoodName = itemView.findViewById(R.id.food_name);
        imgFoodImage = itemView.findViewById(R.id.food_image);
        imgFavImage = itemView.findViewById(R.id.fav);
        quickCart = itemView.findViewById(R.id.quickCart);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
