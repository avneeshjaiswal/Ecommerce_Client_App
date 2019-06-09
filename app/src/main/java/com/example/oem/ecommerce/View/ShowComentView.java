package com.example.oem.ecommerce.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.oem.ecommerce.Model.Rating;
import com.example.oem.ecommerce.R;

/**
 * Created by avneesh jaiswal on 23-Mar-18.
 */

public class ShowComentView extends RecyclerView.ViewHolder {

    public TextView txtUserPhone,txtComent;
    public RatingBar ratingBar;

    public ShowComentView(View itemView) {
        super(itemView);

        txtUserPhone = itemView.findViewById(R.id.txtUserPhone);
        txtComent = itemView.findViewById(R.id.txtComent);
        ratingBar = itemView.findViewById(R.id.ratingBar);
    }
}
