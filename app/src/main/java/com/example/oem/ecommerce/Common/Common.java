package com.example.oem.ecommerce.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.oem.ecommerce.Model.User;
import com.example.oem.ecommerce.Remote.APIService;
import com.example.oem.ecommerce.Remote.GoogleRetrofitClient;
import com.example.oem.ecommerce.Remote.IGoogleService;
import com.example.oem.ecommerce.Remote.RetrofitClient;

/**
 * Created by avneesh jaiswal on 08-Feb-18.
 */

public class Common {

    public static User currentUser;

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    private static final String GOOGLE_URL = "https://maps.googleapis.com/";
    public static final String INTENT_FOOD_ID = "FoodId";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
    public static IGoogleService getGoogleMapAPI(){
        return GoogleRetrofitClient.getGoogleClient(GOOGLE_URL).create(IGoogleService.class);
    }

    public static final String DELETE = "Delete";

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static String convertCodeToStatus(String code){
        if(code.equals("0")){
            return "Placed";
        }else if(code.equals("1")){
            return "On my way";
        }else{
            return "Shipped";
        }
    }

    //checking internet connection
    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null){
                for(int i=0;i<info.length;i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
