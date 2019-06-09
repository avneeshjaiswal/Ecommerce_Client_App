package com.example.oem.ecommerce.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oem.ecommerce.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText edt_number,edt_password,edt_repassword;
    Button btn_reset;
    private static final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        edt_number = findViewById(R.id.edt_phone);
        edt_password = findViewById(R.id.edt_password);
        edt_repassword = findViewById(R.id.edt_password_1);
        btn_reset = findViewById(R.id.reset);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = edt_password.getText().toString();
                final String repassword = edt_repassword.getText().toString();
                final String number = edt_number.getText().toString();


                //setting progress dailog
                final ProgressDialog progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
                progressDialog.setMessage("Please Wait.....");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //checking if user is not registered
                        if(dataSnapshot.child(edt_number.getText().toString()).exists()) {
                            //getting user information
                            progressDialog.dismiss();
                            if(password.equals(repassword)){
                                table_user.child(number).child("Password").setValue(password);
                                Toast.makeText(ForgetPasswordActivity.this,"Password is updated",Toast.LENGTH_LONG).show();
                                Log.i(TAG, " Password has changed");
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this,"Password is not same",Toast.LENGTH_LONG).show();
                                Log.i(TAG, " Password not matched");
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this,"Number doesnot exist",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Failed to read user", databaseError.toException());
                    }
                });

            }
        });


    }
}
