package com.example.oem.ecommerce.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.R;

/**
 * Created by avneesh jaiswal on 15-Jan-18.
 */

public class RestaurantView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textRestaurantName;
    public ImageView  imgRestaurantImage;

    private ItemClickListener itemClickListener;

    public RestaurantView(View itemView) {
        super(itemView);

        textRestaurantName = itemView.findViewById(R.id.res_name);
        imgRestaurantImage = itemView.findViewById(R.id.res_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
