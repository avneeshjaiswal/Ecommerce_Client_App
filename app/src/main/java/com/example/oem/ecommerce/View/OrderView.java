package com.example.oem.ecommerce.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.oem.ecommerce.Interface.ItemClickListener;
import com.example.oem.ecommerce.R;

/**
 * Created by avneesh jaiswal on 08-Feb-18.
 */

public class OrderView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView OrderId,OrderStatus,OrderPhone,OrderAddress;
    private ItemClickListener itemClickListener;

    public OrderView(View itemView) {
        super(itemView);

        OrderId = itemView.findViewById(R.id.order_id);
        OrderStatus = itemView.findViewById(R.id.order_status);
        OrderAddress = itemView.findViewById(R.id.order_address);
        OrderPhone = itemView.findViewById(R.id.order_phone);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
