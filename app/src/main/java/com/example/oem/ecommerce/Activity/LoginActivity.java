package com.example.oem.ecommerce.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Model.User;
import com.example.oem.ecommerce.R;

import com.example.oem.ecommerce.Session.UserSessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.MessageDigestSpi;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    EditText edt_number,edt_password;
    Button login;
    TextView forget_password,new_user;
    private UserSessionManager pref;
    CheckBox remember_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //pref = new UserSessionManager(this);

        //if (pref.checkLogin()){
          //  startActivity(new Intent(getApplicationContext(), MainActivity.class));
           // finish();
       // }

        setContentView(R.layout.activity_login);

        edt_number = findViewById (R.id.edt_phone);
        edt_password = findViewById (R.id.edt_password);
        login = findViewById (R.id.btn_login);
        forget_password = findViewById (R.id.forget);
        new_user = findViewById (R.id.new_user);

//        remember_me = findViewById(R.id.remember_me);

        //init paper
        //Paper.init(this);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   /* if(Common.isInternetConnected(getBaseContext())){

                        //save user and password
                        if(remember_me.isChecked()){
                            Paper.book().write(Common.USER_KEY,edt_number.getText().toString());
                            Paper.book().write(Common.PWD_KEY,edt_password.getText().toString());
                        }*/
                    //setting progress dailog
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Please Wait.....");
                    progressDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //checking if user is not registered
                        if(dataSnapshot.child(edt_number.getText().toString()).exists()) {

                            //getting user information
                            progressDialog.dismiss();
                            User user = dataSnapshot.child(edt_number.getText().toString()).getValue(User.class);
                            user.setNumber(edt_number.getText().toString());//setting the phone number
                            String pass = dataSnapshot.child(edt_number.getText().toString()).child("Password").getValue().toString();
                            String name = dataSnapshot.child(edt_number.getText().toString()).child("Name").getValue().toString();
                            Log.d(TAG, "onDataChange: pass "+ pass);

                            if (pass.equals(edt_password.getText().toString())) {

                                //printing data on console
                                Log.i(TAG, " User Name: "+name+" User Password: "+pass);
                                //pref.createUserLoginSession();
                                Toast.makeText(LoginActivity.this, " Login Successfull! "+"\n"+" Welcome "+name, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                Common.currentUser = user;

                                startActivity(i);
                                finish();
                                table_user.removeEventListener(this);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, " Wrong Attempt by User..");
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "User is not registered.. please register..", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }/*else{
                        Toast.makeText(LoginActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }*/

        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(i);
            }
        });


    }
}
