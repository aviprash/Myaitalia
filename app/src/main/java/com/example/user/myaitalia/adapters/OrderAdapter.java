package com.example.user.myaitalia.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.myaitalia.R;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    public List<ProductBean> cartBeanList;
    public Context context;
    DataInterface dataInterface;

    public OrderAdapter(Context context, List<ProductBean> cartBeanList, DataInterface dataInterface){
        this.cartBeanList = cartBeanList;
        this.context=context;
        this.dataInterface = dataInterface;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_order_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtOrderDate.setText(cartBeanList.get(position).getOrderDate());
        viewHolder.txtOrder.setText(cartBeanList.get(position).getProductQuantity()+" "+cartBeanList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return cartBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView txtOrderDate, txtOrder;


        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            txtOrderDate =(TextView)mView.findViewById(R.id.txt_order_time);
            txtOrder = mView.findViewById(R.id.txt_product_order);
                  }
    }
}
