package com.example.oem.ecommerce.Service;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Model.Token;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by avneesh jaiswal on 14-Mar-18.
 */

public class FirebaseIdServicce extends FirebaseInstanceIdService{

    public void onTokenRefresh(){
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if(Common.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference tokens = firebaseDatabase.getReference("Tokens");
        Token token = new Token(tokenRefreshed,false);//false because this token send from client app
        tokens.child(Common.currentUser.getNumber()).setValue(token);
    }
}
