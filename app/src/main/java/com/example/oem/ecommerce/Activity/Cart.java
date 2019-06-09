package com.example.oem.ecommerce.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Common.Config;
import com.example.oem.ecommerce.Database.Database;
import com.example.oem.ecommerce.Helper.RecyclerItemTouchHelper;
import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.Interface.RecycleritemTouchHelperListener;

import com.example.oem.ecommerce.Model.MyResponse;
import com.example.oem.ecommerce.Model.Notification;
import com.example.oem.ecommerce.Model.Order;
import com.example.oem.ecommerce.Model.Request;
import com.example.oem.ecommerce.Model.Sender;
import com.example.oem.ecommerce.Model.Token;

import com.example.oem.ecommerce.R;
import com.example.oem.ecommerce.Remote.APIService;
import com.example.oem.ecommerce.Remote.IGoogleService;
import com.example.oem.ecommerce.View.CartAdapter;

import com.example.oem.ecommerce.View.CartView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.SnackBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Cart extends AppCompatActivity implements RecycleritemTouchHelperListener {

    private static final int PAYPAL_REQUEST_CODE = 9999;
    private static final int LOCATION_REQUEST_CODE = 999;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    public TextView totalPrice;
    Button placeOrder;

    IGoogleService mGoogleMapService;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    APIService mServices;
    Place shipAddress;
    private int total;
    //paypal payment
    static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(Config.PAYPAL_CLIENT_ID);
    String address, comment;

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init paypal service
        Intent intent = new Intent(Cart.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);


        //init services
        mServices = Common.getFCMService();
        //init
        mGoogleMapService = Common.getGoogleMapAPI();

        //setting firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //setting recyclerView
        recyclerView = findViewById(R.id.cart_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);

        //swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        totalPrice = findViewById(R.id.total);
        placeOrder = findViewById(R.id.placeOrder);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart.size() > 0) {
                    showAlertDialog();
                } else {
                    Toast.makeText(Cart.this, "Your cart is empty..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Final Step");
        alertDialog.setMessage("Enter your address");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View order_address_comment = layoutInflater.inflate(R.layout.order_address_comment, null);


        //final MaterialEditText edtAddress = order_address_comment.findViewById(R.id.edtAddress);
        PlaceAutocompleteFragment edtAddress = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_auto_complete);
        //hide search icon before fragment
        edtAddress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        //setting hint for autocomplete text
        ((EditText) edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter your address");
        //setting text size
        ((EditText) edtAddress.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

        Log.d("Cart.java", "Places " + edtAddress);
        final MaterialEditText edtComment = order_address_comment.findViewById(R.id.edtComment);

        final RadioButton rdiCOD = order_address_comment.findViewById(R.id.COD);
        final RadioButton rdiPayPal = order_address_comment.findViewById(R.id.paypal);

        //get address from placeAutocompleteFragment
        edtAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                shipAddress = place;

                Log.d("Cart.java", "Ship Address " + shipAddress);

            }

            @Override
            public void onError(Status status) {

                Log.e("Error", status.getStatusMessage());
            }
        });
        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //paypal to payment
                //get address and comment from dialog box
                address = shipAddress.getAddress().toString();
                comment = edtComment.getText().toString();

                if(!rdiCOD.isChecked() && !rdiPayPal.isChecked()){
                    Toast.makeText(Cart.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.place_auto_complete)).commit();
                    return;
                }else if(rdiPayPal.isChecked()){
                     /*String formatAmount = totalPrice.getText().toString()
                        .replace("@string/Rs","")
                        .replace(",",",");*/

                    PayPalPayment payPalPayment = new PayPalPayment(BigDecimal.valueOf(total).movePointLeft(2),
                            "USD",
                            "Chives Order",
                            PayPalPayment.PAYMENT_INTENT_SALE);
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
                    startActivityForResult(intent, PAYPAL_REQUEST_CODE);
                }else if(rdiCOD.isChecked()){
                    Request request = new Request(
                            Common.currentUser.getNumber(),
                            Common.currentUser.getName(),
                            address,
                            totalPrice.getText().toString(),
                            "0",
                            comment,
                            "Cash On Delivery",
                            "Unpaid"
                            , String.format("%s,%s", shipAddress.getLatLng().latitude, shipAddress.getLatLng().longitude),
                            cart
                    );


                    //submit to firebase
                    //we will be using System.CurrentMilli to key
                    String order_number = String.valueOf(System.currentTimeMillis());

                    requests.child(order_number).setValue(request);

                    // now deleting cart
                    new Database(getBaseContext()).deleteFromCart(Common.currentUser.getNumber());

                    sendNotificationOrder(order_number);

                    Toast.makeText(Cart.this, "Thankyou , Your Order Has been Placed!!", Toast.LENGTH_SHORT).show();
                    finish();

                }



                /*//remove fragment
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.place_auto_complete)).commit();
*/
                //deletecart
                new Database(getBaseContext()).deleteFromCart(Common.currentUser.getNumber());

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                //remove fragment
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.place_auto_complete)).commit();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*super.onActivityResult(requestCode, resultCode, data);*/
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetail = confirmation.toJSONObject().toString(4);
                        JSONObject jsonObject = new JSONObject(paymentDetail);
                        Request request = new Request(
                                Common.currentUser.getNumber(),
                                Common.currentUser.getName(),
                                address,
                                totalPrice.getText().toString(),
                                "0",
                                comment,
                                "Paypal",
                                jsonObject.getJSONObject("response").getString("state")//state from json
                                , String.format("%s,%s", shipAddress.getLatLng().latitude, shipAddress.getLatLng().longitude),
                                cart
                        );


                        //submit to firebase
                        //we will be using System.CurrentMilli to key
                        String order_number = String.valueOf(System.currentTimeMillis());

                        requests.child(order_number).setValue(request);

                        // now deleting cart
                        new Database(getBaseContext()).deleteFromCart(Common.currentUser.getNumber());

                        sendNotificationOrder(order_number);

                        Toast.makeText(Cart.this, "Thankyou , Your Order Has been Placed!!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Payment Cancel", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(Cart.this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sendNotificationOrder(final String order_number) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("isServerToken").equalTo(true);// get all node with isServerToken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot posrSnapShot : dataSnapshot.getChildren()) {
                    Token serverToken = posrSnapShot.getValue(Token.class);

                    //create raw payload to send
                    Notification notification = new Notification("Chives", "Your have new Order" + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mServices.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(Cart.this, "Thankyou , Your Order Has been Placed!!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Cart.this, "Failed!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void loadListFood() {
        cart = new Database(this).getCarts(Common.currentUser.getNumber());
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        //calculate total price
        total = 0;
        int quantity;
        int price;
        for (Order order : cart) {
            quantity = Integer.parseInt(order.getQuantity());
            price = Integer.parseInt(order.getPrice());
            total = total + (price * quantity);
        }
        Locale locale = new Locale("en", "IN");
        //NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        totalPrice.setText(String.valueOf(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {
        //remove order by its position from list<order>
        cart.remove(position);
        //delete all old data from sqlite database
        new Database(this).deleteFromCart(Common.currentUser.getNumber());
        //update new data from list<order> to sqlite
        for (Order item : cart)
            new Database(this).addToCart(item);
        //refresh
        loadListFood();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
            if(viewHolder instanceof  CartView){
                String name = ((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductName();
                final Order deleteItem =((CartAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
                final int deleteIndex = viewHolder.getAdapterPosition();

                adapter.removeItem(deleteIndex);
                new Database(getBaseContext()).removeFromFavourites(deleteItem.getProductId(),Common.currentUser.getNumber());
                int total = 0;
                int quantity;
                int price;
                List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getNumber());
                for (Order item : orders) {
                       /*quantity = Integer.parseInt(order.getQuantity());
                       price = Integer.parseInt(order.getPrice());*/
                    total = total + (Integer.parseInt(item.getPrice()) * (Integer.parseInt(item.getQuantity())) );
                }
                // Locale locale = new Locale("en", "IN");
                //NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                totalPrice.setText(String.valueOf(total));
                //making snack bar
                Snackbar snackBar = Snackbar.make(rootLayout, name + " removed from cart!",Snackbar.LENGTH_LONG);
                snackBar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(deleteItem,deleteIndex);
                        new Database(getBaseContext()).addToCart(deleteItem);
                        adapter.removeItem(deleteIndex);
                        new Database(getBaseContext()).removeFromCart(deleteItem.getProductId(),Common.currentUser.getNumber());
                        int total = 0;
                        List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getNumber());
                        for (Order item : orders) {
                       /*quantity = Integer.parseInt(order.getQuantity());
                       price = Integer.parseInt(order.getPrice());*/
                            total = total + (Integer.parseInt(item.getPrice()) * (Integer.parseInt(item.getQuantity())) );
                        }

                    }
                });
                snackBar.setActionTextColor(Color.YELLOW);
                snackBar.show();
            }
    }
}

