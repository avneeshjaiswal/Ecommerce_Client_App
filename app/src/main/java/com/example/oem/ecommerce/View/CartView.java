package com.example.oem.ecommerce.View;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.R;

public class CartView extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener {

    public TextView cart_food_name,cart_food_price;

    private ItemClickListener itemClickListener;

    public RelativeLayout viewBackgroud;
    public LinearLayout viewForeground;

    public ElegantNumberButton btn_quantity;



    public void setCart_food_name(TextView cart_food_name) {
        this.cart_food_name = cart_food_name;
    }

    public CartView(View itemView) {
        super(itemView);
        cart_food_name = itemView.findViewById(R.id.cart_item_name);
        cart_food_price = itemView.findViewById(R.id.cart_item_price);
   //     image_cart_Count = itemView.findViewById(R.id.cart_item_count);
        viewBackgroud = itemView.findViewById(R.id.view_background);
        viewForeground = itemView.findViewById(R.id.viewForeground);
        btn_quantity = itemView.findViewById(R.id.quantity);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}
