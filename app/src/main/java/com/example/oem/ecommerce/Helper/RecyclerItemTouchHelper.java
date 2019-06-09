package com.example.oem.ecommerce.Helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.oem.ecommerce.Interface.RecycleritemTouchHelperListener;
import com.example.oem.ecommerce.View.CartView;
import com.stepstone.apprating.C;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback{

    private RecycleritemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecycleritemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null){
            listener.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroudView = ((CartView)viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroudView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroudView = ((CartView)viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroudView,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            View foregroudView = ((CartView)viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(foregroudView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroudView = ((CartView)viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c,recyclerView,foregroudView,dX,dY,actionState,isCurrentlyActive);
    }
}
