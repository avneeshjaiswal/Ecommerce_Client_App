package com.example.oem.ecommerce.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.ViewGroup;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Database.Database;
import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Model.Banner;
import com.example.oem.ecommerce.Model.RestaurantNameWithImage;

import com.example.oem.ecommerce.Model.Token;
import com.example.oem.ecommerce.R;

import com.example.oem.ecommerce.Session.UserSessionManager;
import com.example.oem.ecommerce.View.RestaurantView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Slider;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference restaurant;
    //TextView txtFullName;
    RecyclerView recyclerView;
    private UserSessionManager pref;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RestaurantNameWithImage,RestaurantView> adapter;
    FloatingActionButton fab;
    //CounterFab fab ;
   /* //slider
    HashMap<String,String> image_list;
    SliderLayout sliderLayout;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // pref = new UserSessionManager(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        restaurant = firebaseDatabase.getReference("Restaurant");

        recyclerView = findViewById(R.id.recycler_restaurant);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        Log.d(TAG, "onCreate: restaurant list " + restaurant.toString());

       fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Cart.class);
                startActivity(i);
            }
        });

      // fab.setCount(new Database(this).getCountCart(Common.currentUser.getNumber()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        //setting user name
//        View headerView = navigationView.getHeaderView(1);
//        txtFullName = findViewById(R.id.user_name);
        //txtFullName.setText();

        //Loading the restaurant name with image

        //layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        /*if(!Common.isInternetConnected(this)){

            loadRestaurant();
        }else{
            Toast.makeText(MainActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();

        }*/

        updateToken(FirebaseInstanceId.getInstance().getToken());

        //setting slider
        //setUpSlider();
        loadRestaurant();



    }

  /*  @Override
    protected void onResume() {

        super.onResume();
        fab.setCount(new Database(this).getCountCart(Common.currentUser.getNumber()));
        if(adapter!=null){
            adapter.startListening();
        }
    }*/

   /* private void setUpSlider() {
        sliderLayout = findViewById(R.id.slider);
        image_list = new HashMap<>();

        final DatabaseReference banner = firebaseDatabase.getReference("Banner");
        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Banner banner = postSnapshot.getValue(Banner.class);

                    image_list.put(banner.getName()+"@"+banner.getId(),banner.getImage());
                }
                for(String key:image_list.keySet()){
                    String[] keySplit = key.split("@");
                    String nameOfFood = keySplit[0];
                    String idOfFood = keySplit[1];

                    //create slider
                    final TextSliderView  textSliderView = new TextSliderView(getBaseContext());
                    textSliderView.description(nameOfFood).image(image_list.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    Intent i = new Intent(MainActivity.this,FoodDetail.class);
                                    i.putExtras(textSliderView.getBundle());
                                    startActivity(i);
                                }
                            });
                    //add extra bundle
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId",idOfFood);

                    sliderLayout.addSlider(textSliderView);
                    banner.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);



    }
*/
    private void updateToken(String token) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference tokens = firebaseDatabase.getReference("Tokens");
        Token data = new Token(token,false);//false because this token send from client app
        tokens.child(Common.currentUser.getNumber()).setValue(data);
    }

    private void loadRestaurant() {

        Log.d(TAG, "loadRestaurant: restaurant is called");
        FirebaseRecyclerOptions<RestaurantNameWithImage> options = new FirebaseRecyclerOptions.Builder<RestaurantNameWithImage>()
                .setQuery(restaurant,RestaurantNameWithImage.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<RestaurantNameWithImage, RestaurantView>(options) {
           @Override
           protected void onBindViewHolder(@NonNull RestaurantView holder, int position, @NonNull RestaurantNameWithImage model) {
                holder.textRestaurantName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imgRestaurantImage);
               final RestaurantNameWithImage clickItem = model;
               holder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {
                       //Toast.makeText(MainActivity.this,adapter.getRef(position).getKey() + " : "+clickItem.getName(),Toast.LENGTH_SHORT).show();
                       Intent intent= new Intent(MainActivity.this,FoodList.class);
                       intent.putExtra("Restaurant_id",adapter.getRef(position).getKey());
                       startActivity(intent);
                   }
               });
           }

           @Override
           public RestaurantView onCreateViewHolder(ViewGroup parent, int viewType) {
               View itemView =  LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list,parent,false);
               return new RestaurantView(itemView);
           }
       };
        adapter.startListening();
       recyclerView.setAdapter(adapter);

    }


    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //pref.clearSession();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
            return true;
        }*//*else if(id == R.id.refresh){
            //refresh the screen7
            loadRestaurant();
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this,MainActivity.class);
        } else if (id == R.id.nav_orders) {
            Intent i = new Intent(MainActivity.this, OrderStatus.class);
            startActivity(i);

        }  else if (id == R.id.food_customise) {
            Intent i = new Intent(MainActivity.this,FoodCustomise.class);
            startActivity(i);

        } else if (id == R.id.nav_logout) {
               // pref.clearSession();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else if (id == R.id.change_password) {
            showChangePasswordDialog();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangePasswordDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setMessage("Please fill the information");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View layout_pwd = layoutInflater.inflate(R.layout.change_password,null);

        final MaterialEditText edtPassword = (MaterialEditText)layout_pwd.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword  = (MaterialEditText)layout_pwd.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRePassword  = (MaterialEditText)layout_pwd.findViewById(R.id.edtRepeatePassword);

        alertDialog.setView(layout_pwd);

        //button
        alertDialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                final android.app.AlertDialog waitingDialog  = new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                //check old password
                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword())){
                    //check new and repeate password
                    if(edtNewPassword.getText().toString().equals(edtRePassword.getText().toString())){
                        Map<String,Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("Password",edtNewPassword.getText().toString());

                        //making update in firebase
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getNumber()).updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                Toast.makeText(MainActivity.this,"Password was Update",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        waitingDialog.dismiss();
                        Toast.makeText(MainActivity.this,"New password doesn't match",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadRestaurant();
        //setUpSlider();
    }
}