package com.example.oem.ecommerce.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oem.ecommerce.Model.Food;
import com.example.oem.ecommerce.Model.Order;
import com.example.oem.ecommerce.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodCustomise extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4,btn5,btn6;
    private static final String TAG = "FoodCustomisation";
    EditText add_comnent;
    String[] listvegetable;
    String[] listsauces;
    String[] listspices;
    String[] listbread;
    String[] listnon_veg;
    String[] listdairy;
    Button requestCustomise;
    Editable coment;
    String item1 = "";
    String item2 = "";
    String item3 = "";
    String item4 = "";
    String item5 = "";
    String item6 = "";
    String res_id = "";
    boolean[] checkVegetable,checkSauces,checkSpices,checkBread,checkNon_Veg,checkDairy;
    ArrayList<Integer> mUserItems1 = new ArrayList<>();
    ArrayList<Integer> mUserItems2 = new ArrayList<>();
    ArrayList<Integer> mUserItems3 = new ArrayList<>();
    ArrayList<Integer> mUserItems4 = new ArrayList<>();
    ArrayList<Integer> mUserItems5 = new ArrayList<>();
    ArrayList<Integer> mUserItems6 = new ArrayList<>();
    //Food newFood;
    FirebaseDatabase database;
    DatabaseReference customiseFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_customise);

        btn1 = findViewById(R.id.vegii);
        btn2 = findViewById(R.id.sauce);
        btn3 = findViewById(R.id.spice);
        btn4 = findViewById(R.id.bread);
        btn5 = findViewById(R.id.non_veg);
        btn6 = findViewById(R.id.dairy);

        //setting firebase
        database =   FirebaseDatabase.getInstance();
        customiseFood = database.getReference("Requests");


        requestCustomise = findViewById(R.id.requestCustomise);

        add_comnent = findViewById(R.id.add_coment);

        listvegetable = getResources().getStringArray(R.array.vegetables);
        checkVegetable = new boolean[listvegetable.length];

        listsauces = getResources().getStringArray(R.array.sauces);
        checkSauces = new boolean[listsauces.length];

        listspices = getResources().getStringArray(R.array.spices);
        checkSpices = new boolean[listspices.length];

        listbread = getResources().getStringArray(R.array.bread_base);
        checkBread = new boolean[listbread.length];

        listnon_veg = getResources().getStringArray(R.array.Non_veg);
        checkNon_Veg = new boolean[listnon_veg.length];

        listdairy = getResources().getStringArray(R.array.dairy);
        checkDairy = new boolean[listdairy.length];

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mVegetableBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mVegetableBuilder.setTitle(R.string.dialog_title_1);
                mVegetableBuilder.setMultiChoiceItems(listvegetable, checkVegetable, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems1.contains(position)){
                                mUserItems1.add(position);
                            }
                        }else if(mUserItems1.contains(position)){
                            mUserItems1.remove(position);
                        }

                    }
                });
                mVegetableBuilder.setCancelable(false);
                mVegetableBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems1.size(); i++){
                            item1 = item1+listvegetable[mUserItems1.get(i)];
                            if(i != mUserItems1.size() -1){
                                item1 = item1+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected vegetables: "+item1, Toast.LENGTH_SHORT).show();
                    }
                });

                mVegetableBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mVegetableBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkVegetable.length;i++){
                            checkVegetable[i] = false;
                            mUserItems1.clear();
                        }
                    }
                });

                AlertDialog mDialog = mVegetableBuilder.create();
                mDialog.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mSauceBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mSauceBuilder.setTitle(R.string.dialog_title_1);
                mSauceBuilder.setMultiChoiceItems(listsauces, checkSauces, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems2.contains(position)){
                                mUserItems2.add(position);
                            }
                        }else if(mUserItems2.contains(position)){
                            mUserItems2.remove(position);
                        }

                    }
                });
                mSauceBuilder.setCancelable(false);
                mSauceBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems2.size(); i++){
                            item2 = item2 + listsauces[mUserItems2.get(i)];
                            if(i != mUserItems2.size() -1){
                                item2 = item2+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected Sauces: "+item2, Toast.LENGTH_SHORT).show();
                    }
                });

                mSauceBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mSauceBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkSauces.length;i++){
                            checkSauces[i] = false;
                            mUserItems2.clear();
                        }
                    }
                });

                AlertDialog mDialog = mSauceBuilder.create();
                mDialog.show();


            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mSpiceBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mSpiceBuilder.setTitle(R.string.dialog_title_1);
                mSpiceBuilder.setMultiChoiceItems(listspices, checkSpices, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems3.contains(position)){
                                mUserItems3.add(position);
                            }
                        }else if(mUserItems3.contains(position)){
                            mUserItems3.remove(position);
                        }

                    }
                });
                mSpiceBuilder.setCancelable(false);
                mSpiceBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems3.size(); i++){
                            item3 = item3 + listspices[mUserItems3.get(i)];
                            if(i != mUserItems3.size() -1){
                                item3 = item3+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected spices: "+item3, Toast.LENGTH_SHORT).show();
                    }
                });

                mSpiceBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mSpiceBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkSpices.length;i++){
                            checkSpices[i] = false;
                            mUserItems3.clear();
                        }
                    }
                });

                AlertDialog mDialog = mSpiceBuilder.create();
                mDialog.show();



            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mbreadBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mbreadBuilder.setTitle(R.string.dialog_title_1);
                mbreadBuilder.setMultiChoiceItems(listbread, checkBread, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems4.contains(position)){
                                mUserItems4.add(position);
                            }
                        }else if(mUserItems4.contains(position)){
                            mUserItems4.remove(position);
                        }

                    }
                });
                mbreadBuilder.setCancelable(false);
                mbreadBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems4.size(); i++){
                            item4 = item4 + listsauces[mUserItems4.get(i)];
                            if(i != mUserItems4.size() -1){
                                item4 = item4+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected Spices: "+item4, Toast.LENGTH_SHORT).show();
                    }
                });

                mbreadBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mbreadBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkBread.length;i++){
                            checkBread[i] = false;
                            mUserItems4.clear();
                        }
                    }
                });

                AlertDialog mDialog = mbreadBuilder.create();
                mDialog.show();



            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mnon_vegBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mnon_vegBuilder.setTitle(R.string.dialog_title_1);
                mnon_vegBuilder.setMultiChoiceItems(listnon_veg, checkNon_Veg, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems5.contains(position)){
                                mUserItems5.add(position);
                            }
                        }else if(mUserItems5.contains(position)){
                            mUserItems5.remove(position);
                        }

                    }
                });
                mnon_vegBuilder.setCancelable(false);
                mnon_vegBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems5.size(); i++){
                            item5 = item5 + listnon_veg[mUserItems5.get(i)];
                            if(i != mUserItems5.size() -1){
                                item5 = item5+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected Spices: "+item5, Toast.LENGTH_SHORT).show();
                    }
                });

                mnon_vegBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mnon_vegBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkNon_Veg.length;i++){
                            checkNon_Veg[i] = false;
                            mUserItems5.clear();
                        }
                    }
                });

                AlertDialog mDialog = mnon_vegBuilder.create();
                mDialog.show();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mdairyBuilder = new AlertDialog.Builder(FoodCustomise.this);
                mdairyBuilder.setTitle(R.string.dialog_title_1);
                mdairyBuilder.setMultiChoiceItems(listdairy, checkDairy, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems6.contains(position)){
                                mUserItems6.add(position);
                            }
                        }else if(mUserItems6.contains(position)){
                            mUserItems6.remove(position);
                        }

                    }
                });
                mdairyBuilder.setCancelable(false);
                mdairyBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int i = 0; i < mUserItems6.size(); i++){
                            item6 = item6 + listdairy[mUserItems6.get(i)];
                            if(i != mUserItems6.size() -1){
                                item6 = item6+", ";
                            }
                        }
                        Toast.makeText(FoodCustomise.this,"selected Spices: "+item6, Toast.LENGTH_SHORT).show();
                    }
                });

                mdairyBuilder.setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mdairyBuilder.setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<checkDairy.length;i++){
                            checkDairy[i] = false;
                            mUserItems6.clear();
                        }
                    }
                });

                AlertDialog mDialog = mdairyBuilder.create();
                mDialog.show();
            }
        });

         coment = add_comnent.getText();

        //getting intent
        if (getIntent() != null) {
            res_id = getIntent().getStringExtra("Restaurant_id");
            Log.d("TAG", "id " + res_id);
        }


        requestCustomise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast.makeText(FoodCustomise.this,"Vegetables Selected "+item1, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodCustomise.this,"Sauces Selected "+item2, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodCustomise.this,"Spices Selected "+item3, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodCustomise.this,"Bread Base Selected "+item4, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodCustomise.this,"Non Veg Selected "+item5, Toast.LENGTH_SHORT).show();
                Toast.makeText(FoodCustomise.this,"Dairy Selected "+item6, Toast.LENGTH_SHORT).show();

                Toast.makeText(FoodCustomise.this, ""+coment, Toast.LENGTH_SHORT).show();*/
                //String order_number = String.valueOf(System.currentTimeMillis());
                //customise.child(order_number).setValue(request);

               // newFood = new Food();

                Map<String, String> customise = new HashMap<>();
                if(item1 !=null){
                    customise.put("Vegetables: ",item1);
                }else{
                    customise.put("Vegetables:"," not used");
                }
                if(item2 !=null){
                    customise.put("Sauces:",item2);
                }else{
                    customise.put("Sauces"," not used");
                }
                if(item3 !=null){
                    customise.put("Spices:",item3);
                }else{
                    customise.put("Spices:"," not used");
                }
                if(item4 !=null){
                    customise.put("Bread base",item4);
                }else{
                    customise.put("Bread base:"," not used");
                }
                if(item5 !=null){
                    customise.put("Non Veg:",item5);
                }else{
                    customise.put("Non veg:"," not used");
                }
                if(item6 !=null){
                    customise.put("Dairy:",item6);
                }else{
                    customise.put("Dairy:"," not used");
                }
                if(coment!=null){
                    customise.put("Coment:", String.valueOf(coment));
                }else{
                    customise.put("Coment:"," no coment");
                }

                customise.put("res_id",res_id);
                String order_number = String.valueOf(System.currentTimeMillis());
                //customiseFood.push().setValue(customise);
                customiseFood.child(order_number).setValue(customise);
                Toast.makeText(FoodCustomise.this,"Request has been placed..",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoodCustomise.this, OrderStatus.class);
                startActivity(intent);
            }
        });

     }
}
