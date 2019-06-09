package com.example.oem.ecommerce.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import io.paperdb.Paper;

public class Splash extends AppCompatActivity {

    //private static final String TAG = "SPLASH";
    Button login,register;
    private UserSessionManager pref;
    //EditText edt_number,edt_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Splash.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

       /* //check remember
        String userNumber = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.PWD_KEY);

        if(userNumber!= null && password!=null){
            if(!userNumber.isEmpty()&&!password.isEmpty()){
                Login(userNumber,password);
            }
        }*/
    }

   /* private void Login(final String userNumber, String password) {
        if(Common.isInternetConnected(getBaseContext())){

            //firebase
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference table_user = firebaseDatabase.getReference("User");

            //setting progress dailog
            final ProgressDialog progressDialog = new ProgressDialog(Splash.this);
            progressDialog.setMessage("Please Wait.....");
            progressDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //checking if user is not registered
                    if(dataSnapshot.child(userNumber).exists()) {

                        //getting user information
                        progressDialog.dismiss();
                        User user = dataSnapshot.child(userNumber).getValue(User.class);
                        user.setNumber(userNumber);//setting the phone number
                        String pass = dataSnapshot.child(edt_number.getText().toString()).child("Password").getValue().toString();
                        String name = dataSnapshot.child(edt_number.getText().toString()).child("Name").getValue().toString();
                        Log.d(TAG, "onDataChange: pass "+ pass);

                        if (pass.equals(edt_password.getText().toString())) {
                            //printing data on console
                            Log.i(TAG, " User Name: "+name+" User Password: "+pass);
                            Toast.makeText(Splash.this, " Login Successfull! "+"\n"+" Welcome "+name, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Splash.this,MainActivity.class);
                            Common.currentUser = user;
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Splash.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, " Wrong Attempt by User..");
                        }
                    }else{
                        Toast.makeText(Splash.this, "User is not registered.. please register..", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(Splash.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }*/
}
