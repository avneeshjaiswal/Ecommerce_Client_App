package com.example.oem.ecommerce.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oem.ecommerce.Common.Common;
import com.example.oem.ecommerce.Model.Food;
import com.example.oem.ecommerce.Model.Request;
import com.example.oem.ecommerce.R;
import com.example.oem.ecommerce.View.OrderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import static com.example.oem.ecommerce.Common.Common.convertCodeToStatus;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderView> adapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //firebase setting
        firebaseDatabase = FirebaseDatabase.getInstance();
        requests = firebaseDatabase.getReference("Requests");

        recyclerView  =findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null)
          loadOrders(Common.currentUser.getNumber());
        else
            loadOrders(getIntent().getStringExtra("userPhone"));
    }

    private void loadOrders(String number) {

        Query getOrderByUser = requests.orderByChild("phone").equalTo(number);
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>().setQuery(getOrderByUser,Request.class).build();
        adapter = new FirebaseRecyclerAdapter<Request, OrderView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderView holder, int position, @NonNull Request model) {
                holder.OrderId.setText(adapter.getRef(position).getKey());
                holder.OrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                holder.OrderPhone.setText(model.getPhone());
                holder.OrderAddress.setText(model.getAddress());
            }

            @Override
            public OrderView onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);

                return new OrderView(itemView);

            }
        };
       adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
