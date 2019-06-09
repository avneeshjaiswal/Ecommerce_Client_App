package com.example.oem.ecommerce.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Database.Database;
import com.example.oem.ecommerce.Model.Food;
import com.example.oem.ecommerce.Model.Order;
import com.example.oem.ecommerce.Model.Rating;
import com.example.oem.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView food_name,food_price,food_description;
    ImageView imgfoodimage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CounterFab btnCart;
    FloatingActionButton btnRating;
    ElegantNumberButton elegantNumberButton;
    RatingBar ratingBar;
    Button showComment;

    int id;
    String food_id="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference food_detail;
    DatabaseReference food_rating;
    Food currentFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        showComment = findViewById(R.id.showComment);
        showComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetail.this,ShowComment.class);
                intent.putExtra(Common.INTENT_FOOD_ID,food_id);
                startActivity(intent);
            }
        });
        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        food_rating = firebaseDatabase.getReference("Rating");
        food_detail = firebaseDatabase.getReference("Food");

        //initialize view
        elegantNumberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.cart);
        btnRating = findViewById(R.id.btn_rating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        Common.currentUser.getNumber(),food_id, currentFood.getFood_name(),
                        elegantNumberButton.getNumber(),
                        String.valueOf(currentFood.getPrice())
                ));
                Toast.makeText(FoodDetail.this,"Added to cart",Toast.LENGTH_SHORT).show();

            }
        });

        btnCart.setCount(new Database(this).getCountCart(Common.currentUser.getNumber()));
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_description = findViewById(R.id.food_desc);
        imgfoodimage = findViewById(R.id.img_food);

        collapsingToolbarLayout = findViewById(R.id.collapsetoolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapseAppbar);

        //getting food id from intent
        if(getIntent()!= null){
            food_id = getIntent().getStringExtra("FoodId");
        }
        if(!food_id.isEmpty()){
            if(Common.isInternetConnected(getBaseContext())){
                getDetailFood(food_id);
                getRatingFood(food_id);
            }else{
                Toast.makeText(FoodDetail.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void getRatingFood(String food_id) {
        Query foodRating  = food_rating.orderByChild("food_id").equalTo(food_id);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0,sum=0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0){
                    float average = sum/count;
                    ratingBar.setRating(average);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //setting rating dialog box
    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please Select Stars And Give your Feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here.....")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.fbutton_color_carrot)
                .setWindowAnimation(R.style.RatingDialogFadeAnimation)
                .create(FoodDetail.this)
                .show();

    }

    private void getDetailFood(final String food_id) {
        food_detail.child(food_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                //set image
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(imgfoodimage);
                collapsingToolbarLayout.setTitle(currentFood.getFood_name());
                food_price.setText(currentFood.getPrice());
                food_description.setText(currentFood.getDescription());
                food_name.setText(currentFood.getFood_name());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //getting rating and uploading to firebase
        final Rating rating = new Rating(Common.currentUser.getNumber(),food_id,String.valueOf(value),comments);
        food_rating.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodDetail.this, "Thankyou for submiting rating!!!", Toast.LENGTH_SHORT).show();

            }
        });

        //user rate mutiple times
        food_rating.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodDetail.this, "Thankyou for submiting rating!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {
    }
}
