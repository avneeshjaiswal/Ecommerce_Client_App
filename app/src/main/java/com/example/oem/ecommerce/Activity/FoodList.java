package com.example.oem.ecommerce.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Database.Database;
import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Model.Food;
import com.example.oem.ecommerce.Model.Order;
import com.example.oem.ecommerce.Model.RestaurantNameWithImage;
import com.example.oem.ecommerce.R;

import com.example.oem.ecommerce.View.FoodView;
import com.example.oem.ecommerce.View.RestaurantView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    private static final String TAG = "FoodList";
    RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    String Restaurant_id = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference foodlist;
    FirebaseRecyclerAdapter<Food, FoodView> adapter;

    //favourties
    Database localDB;

    //adding searching functionality
    FirebaseRecyclerAdapter<Food, FoodView> searchAdapter;
    MaterialSearchBar materialSearchBar;
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase connection
        firebaseDatabase = FirebaseDatabase.getInstance();
        foodlist = firebaseDatabase.getReference("Food");
        //Log.d(TAG,"list"+foodlist.toString());

        //localDb
        localDB = new Database(this);

        recyclerView = findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);
        /*layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
*/
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //getting intent
        if (getIntent() != null) {
            Restaurant_id = getIntent().getStringExtra("Restaurant_id");
        }
        if (!Restaurant_id.isEmpty()) {
            Log.d(TAG, "res_id " + Restaurant_id);
            loadFood(Restaurant_id);
        }

        //search bar setting
        materialSearchBar = findViewById(R.id.search_bar);
        materialSearchBar.setHint("Enter Food Name");
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        //adding text changing listener
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text , we change the suggest list
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //setting search action listener
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed then restore original adapter
                if (!enabled) {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish then show result of search adapter
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
       /* //register service
        Intent service = new Intent(FoodList.this, ListenOrder.class);
        startActivity(service);*/

    }

    private void loadSuggest() {
        //function to search food list
        foodlist.orderByChild("res_id").equalTo(Restaurant_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Food items = postSnapshot.getValue(Food.class);
                            suggestList.add(items.getFood_name());//add name to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void startSearch(CharSequence text) {
        Query searchByName = foodlist.orderByChild("food_name").equalTo(text.toString());
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodView holder, int position, @NonNull Food model) {
                holder.textFoodName.setText(model.getFood_name());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imgFoodImage);

                final Food local = model;
                holder.setItemClickListener(new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + local.getFood_name(), Toast.LENGTH_SHORT).show();
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", searchAdapter.getRef(position).getKey());//sending food id to other activity
                        startActivity(foodDetail);
                    }
                });
            }

            @Override
            public FoodView onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

                return new FoodView(itemView);
            }
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);

    }


    private void loadFood(String restaurant_id) {
        Query load = foodlist.orderByChild("res_id").equalTo(restaurant_id);

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(load,Food.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Food, FoodView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodView holder, final int position, final Food model) {
                Log.d(TAG, "food_name : " + model.getFood_name() + " Food_image : " + model.getFood_name());

                holder.textFoodName.setText(model.getFood_name());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imgFoodImage);

                //quick cart
                holder.quickCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isExists = new Database(getBaseContext()).checkFoodExists(adapter.getRef(position).getKey(), Common.currentUser.getNumber());
                        if (!isExists) {
                            new Database(getBaseContext()).addToCart(new Order(
                                    Common.currentUser.getNumber(),
                                    adapter.getRef(position).getKey(),

                                    model.getFood_name(),
                                    "1",
                                    model.getPrice()
                            ));


                        }else{new Database(getBaseContext()).increaseCart(Common.currentUser.getNumber(),adapter.getRef(position).getKey());}
                        Toast.makeText(FoodList.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                });

                //add favourites
                if (localDB.isFavourites(adapter.getRef(position).getKey(),Common.currentUser.getNumber())) {
                    holder.imgFavImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                }

                //click to change state of favorite
                holder.imgFavImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!localDB.isFavourites(adapter.getRef(position).getKey(),Common.currentUser.getNumber())){
                            localDB.addToFavourites(adapter.getRef(position).getKey(),Common.currentUser.getNumber());
                            holder.imgFavImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this,"Added to favorite",Toast.LENGTH_SHORT).show();
                        }else{
                            localDB.removeFromFavourites(adapter.getRef(position).getKey(),Common.currentUser.getNumber());
                            holder.imgFavImage.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this,"Removed from favorite",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final Food clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + clickItem.getFood_name(), Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, "onCreate: food list "+local);
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                        foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());//sending food id to other activity
                        startActivity(foodDetail);
                    }


                });
            }


            @Override
            public FoodView onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodView(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
//        searchAdapter.stopListening();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFood(Restaurant_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.startListening();
        }
    }
}