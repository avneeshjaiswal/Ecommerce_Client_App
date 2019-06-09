package com.example.oem.ecommerce.Activity;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    EditText edt_number,edt_name,edt_password;
    Button btn_signup;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_name = findViewById(R.id.edt_name);
        edt_number = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_pass);
        btn_signup = findViewById(R.id.signUp);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Common.isInternetConnected(getBaseContext())){
                //setting progress dailog
                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Please Wait.....");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if phone number is already exist
                        if(dataSnapshot.child(edt_number.getText().toString()).exists()){
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this,"Number already exist",Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.dismiss();
                            User user = new User(edt_name.getText().toString(), edt_password.getText().toString());
                            table_user.child(edt_number.getText().toString()).setValue(user);
                            Log.i(TAG,"name: "+user.getName()+" number: "+user.getNumber()+" password:"+user.getPassword());
                            Toast.makeText(SignUpActivity.this,"SignUp successfull",Toast.LENGTH_SHORT).show();
                            finish();
                            Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//FirebaseAuth.getInstance.getCurrentUser()==null---send to loginelse automatic

            }
            else{
                    Toast.makeText(SignUpActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        }
        );

        }

    }

