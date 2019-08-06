package com.example.user.myaitalia.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.myaitalia.R;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public List<ProductBean> cartBeanList;
    public Context context;
    DataInterface dataInterface;

    public CartAdapter(Context context, List<ProductBean> cartBeanList, DataInterface dataInterface){
        this.cartBeanList = cartBeanList;
        this.context=context;
        this.dataInterface = dataInterface;
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtProductName.setText(cartBeanList.get(position).getProductName());
        viewHolder.txtProductPrice.setText("€ "+ cartBeanList.get(position).getProductPrice()+"");
        viewHolder.txtProductQuantity.setText(cartBeanList.get(position).getProductQuantity()+"");
        dataInterface.setValues(cartBeanList);

        viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductBean CB = cartBeanList.get(position);
                String Q = CB.getProductQuantity()+"";
                int Quantity = 0;
                try {
                    Quantity = Integer.parseInt(Q);
                }
                catch (Exception e){
                    Log.e("MYAITALIA", e+"");
                }
                if(Quantity < 100){
                    Quantity++;
                }
                CB.setProductQuantity(Quantity);
                viewHolder.txtProductQuantity.setText(" "+CB.getProductQuantity()+" ");
                dataInterface.setValues(cartBeanList);
            }
        });


        viewHolder.btnRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductBean CB = cartBeanList.get(position);
                String Q = CB.getProductQuantity()+"";
                int Quantity = 0;
                try {
                    Quantity = Integer.parseInt(Q);
                }
                catch (Exception e){
                    Log.e("MYAITALIA", e+"");
                }
                if(Quantity > 0){
                    Quantity--;
                }
                viewHolder.txtProductQuantity.setText(Quantity+"");
                CB.setProductQuantity(Quantity);
                viewHolder.txtProductQuantity.setText(" "+CB.getProductQuantity()+" ");
                dataInterface.setValues(cartBeanList);


            }
        });

    }

    @Override
    public int getItemCount() {
        return cartBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button btnAddToCart, btnRemoveFromCart;
        public TextView txtProductName, txtProductPrice;
        EditText txtProductQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

            txtProductQuantity = mView.findViewById(R.id.txt_item_quantity);
            txtProductName=(TextView)mView.findViewById(R.id.txt_prd_name_cart);
            txtProductPrice = mView.findViewById(R.id.txt_prd_price_cart);
            btnAddToCart = mView.findViewById(R.id.btn_add_item);
            btnRemoveFromCart = mView.findViewById(R.id.btn_minus_cart);

        }
    }
}
