/*
package com.example.oem.ecommerce.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Model.RestaurantInfo;
import com.example.oem.ecommerce.R;
import com.squareup.picasso.Picasso;

*/
/**
 * Created by avneesh jaiswal on 16-Jan-18.
 *//*


public class RestaurantInfoView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView res_name,res_address,res_dev_charges,res_avg_time_dil,res_min_order,res_payment_option;
    public ImageView res_image;
    private ItemClickListener itemClickListener;
    View mView;
    Context mContext;



    public RestaurantInfoView(View itemView) {
        super(itemView);


        mView = itemView;
        mContext = itemView.getContext();

        itemView.setOnClickListener(this);
    }

    public void bindRestaurant(RestaurantInfo restaurantInfo){
        res_name = itemView.findViewById(R.id.res_name_id);
        res_address = itemView.findViewById(R.id.res_add_id);
        res_dev_charges = itemView.findViewById(R.id.res_dev_charges_id);
        res_avg_time_dil = itemView.findViewById(R.id.res_avg_del_time_id);
        res_min_order = itemView.findViewById(R.id.res_min_order_id);
        res_payment_option = itemView.findViewById(R.id.res_payment_option);
        res_image = itemView.findViewById(R.id.res_image_id);

        Picasso.with(mContext)
                .load(restaurantInfo.getRestaurant_image())
                .centerCrop()
                .into(res_image);

        res_name.setText(restaurantInfo.getName());
        res_address.setText(restaurantInfo.getAddress());
        res_dev_charges.setText(restaurantInfo.getDev_Charges());
        res_avg_time_dil.setText(restaurantInfo.getAvg_Dev_Time());
        res_min_order.setText(restaurantInfo.getMin_Order());
        res_payment_option.setText(restaurantInfo.getPayment_Option());

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
*/
