package com.example.oem.ecommerce.View;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.oem.ecommerce.Activity.Cart;
import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Database.Database;
import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Model.Order;
import com.example.oem.ecommerce.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by avneesh jaiswal on 07-Feb-18.
 */




public class CartAdapter extends RecyclerView.Adapter<CartView>{
    public  Cart cart;
    private List<Order> listData = new ArrayList<>();
    private Context context;
    public TextView totalPrice;
    public CartAdapter(List<Order> listData,Cart cart){
        this.listData = listData;
        this.cart = cart;

    }
    @Override
    public CartView onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartView(itemView);
    }

    @Override
    public void onBindViewHolder(CartView holder, final int position) {
           /* TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
            holder.image_cart_Count.setImageDrawable(drawable);*/

           holder.btn_quantity.setNumber(listData.get(position).getQuantity());
           holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
               @Override
               public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                   Order order = listData.get(position);
                   order.setQuantity(String.valueOf(newValue));
                   //new Database(cart).updateCart(order);

                   int total = 0;
                   int quantity;
                   int price;
                   List<Order> orders = new Database(cart).getCarts(Common.currentUser.getNumber());
                   for (Order item : orders) {
                       /*quantity = Integer.parseInt(order.getQuantity());
                       price = Integer.parseInt(order.getPrice());*/
                       total = total + (Integer.parseInt(order.getPrice()) * (Integer.parseInt(order.getQuantity())) );
                   }
                   // Locale locale = new Locale("en", "IN");
                   //NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                   cart.totalPrice.setText(String.valueOf(total));
               }
           });

        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        Float price =  (Float.parseFloat(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.cart_food_price.setText(fmt.format(price));
        holder.cart_food_name.setText(listData.get(position).getProductName());




    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item,int position){
        listData.add(position,item);
        notifyItemInserted(position);
    }
}


