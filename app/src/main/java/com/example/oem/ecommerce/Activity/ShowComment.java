package com.example.oem.ecommerce.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Model.Rating;
import com.example.oem.ecommerce.R;
import com.example.oem.ecommerce.View.ShowComentView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference food_rating;

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseRecyclerAdapter<Rating,ShowComentView> adapter;
    String FoodId="";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);

        //firebase
        database = FirebaseDatabase.getInstance();
        food_rating = database.getReference("Rating");

        recyclerView = findViewById(R.id.recyclerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //swipe layout
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent() != null){
                    FoodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                }
                if(!FoodId.isEmpty()){
                    //create query
                    Query query = food_rating.orderByChild("foodId").equalTo(FoodId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>().setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowComentView>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowComentView holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtUserPhone.setText(model.getUserPhone());
                            holder.txtComent.setText(model.getComment());
                        }

                        @Override
                        public ShowComentView onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_coment,parent,false);
                            return new ShowComentView(view);
                        }
                    };
                    loadComment(FoodId);
                }
            }
        });
        //thread to load commenton first launch
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

                if (getIntent() != null) {
                    FoodId = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
                }
                if (!FoodId.isEmpty()) {
                    //create query
                    Query query = food_rating.orderByChild("foodId").equalTo(FoodId);

                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>().setQuery(query, Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowComentView>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ShowComentView holder, int position, @NonNull Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtUserPhone.setText(model.getUserPhone());
                            holder.txtComent.setText(model.getComment());
                        }

                        @Override
                        public ShowComentView onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_coment, parent, false);
                            return new ShowComentView(view);
                        }
                    };
                    loadComment(FoodId);
                }
            }});
    }

    private void loadComment(String foodId) {
        adapter.startListening();

        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}
