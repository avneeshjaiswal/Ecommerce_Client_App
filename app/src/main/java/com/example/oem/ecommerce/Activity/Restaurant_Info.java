/*
package com.example.oem.ecommerce.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Model.RestaurantInfo;

import com.example.oem.ecommerce.R;
import com.example.oem.ecommerce.View.RestaurantInfoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Picasso;

public class Restaurant_Info extends AppCompatActivity {

    public TextView res_name,res_address,res_dev_charges,res_avg_time_dil,res_min_order,res_payment_option;
    public ImageView res_image;
    Button btnOrderNow;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference restaurant_info;
    String Res_id="";
    FirebaseRecyclerAdapter<RestaurantInfo,RestaurantInfoView> adapter;
    RecyclerView recyclerView ;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        restaurant_info = firebaseDatabase.getReference("Restaurant");

        res_name = findViewById(R.id.res_name_id);
        res_address = findViewById(R.id.res_add_id);
        res_dev_charges = findViewById(R.id.res_dev_charges_id);
        res_avg_time_dil = findViewById(R.id.res_avg_del_time_id);
        res_min_order = findViewById(R.id.res_min_order_id);
        res_payment_option = findViewById(R.id.res_payment_option);
        res_image = findViewById(R.id.res_image_id);
        btnOrderNow = findViewById(R.id.order_now_id);



        recyclerView = findViewById(R.id.recycler_restaurant_detail);

        layoutManager = new LinearLayoutManager(this);

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Restaurant_Info.this,FoodListDetail.class);
                startActivity(intent);
            }
        });

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if(getIntent() != null){
            Res_id = getIntent().getStringExtra("Restaurant_id");
        }
        if(!Res_id.isEmpty() && Res_id != null){
            loadRestaurantInfo(Res_id);
        }
    }
    private void loadRestaurantInfo(String Rest_id) {
        adapter = new FirebaseRecyclerAdapter<RestaurantInfo, RestaurantInfoView>(RestaurantInfo.class,R.layout.activity_restaurant_info, RestaurantInfoView.class,restaurant_info.orderByChild("Restaurant_id").equalTo(Rest_id)) {
            @Override
            protected void populateViewHolder(RestaurantInfoView viewHolder, RestaurantInfo model, int position) {
               */
/* viewHolder.res_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getRestaurant_image()).into(viewHolder.res_image);
                viewHolder.res_address.setText(model.getAddress());
                viewHolder.res_avg_time_dil.setText(model.getAvg_Dev_Time());
                viewHolder.res_min_order.setText(model.getMin_Order());
                viewHolder.res_dev_charges.setText(model.getDev_Charges());
                viewHolder.res_payment_option.setText(model.getPayment_Option());

                final RestaurantInfo clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Restaurant_Info.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();

                    }
                });
*//*

               viewHolder.bindRestaurant(model);



            }
        };
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
*/
