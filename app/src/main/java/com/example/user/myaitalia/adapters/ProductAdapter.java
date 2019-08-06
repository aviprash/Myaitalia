package com.example.user.myaitalia.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.myaitalia.R;
import com.example.user.myaitalia.beans.ProductBean;
import com.example.user.myaitalia.interfaces.DataInterface;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public boolean isAddedToCart;
    public List<ProductBean> productBeanList;
    public Context context;
    public static List<ProductBean> cartList;
    DataInterface dataInterface;

    public ProductAdapter(Context context, List<ProductBean> productBeanList, DataInterface dataInterface){
        this.productBeanList = productBeanList;
        this.context=context;
        this.dataInterface = dataInterface;
        cartList = new ArrayList<>();
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item,viewGroup,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtProductName.setText(productBeanList.get(position).getProductName());
        viewHolder.txtProductPrice.setText("€ "+productBeanList.get(position).getProductPrice()+"");
        viewHolder.imgProduct.setImageResource(productBeanList.get(position).getProductImage());
        viewHolder.toggleCart.setBackgroundResource(productBeanList.get(position).getProductSelectedImage());
        viewHolder.txtToggleCart.setText(productBeanList.get(position).getProductSelectedText());


        viewHolder.toggleCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductBean PB = productBeanList.get(position);
                isAddedToCart = PB.isAddedToCart();

                if(!isAddedToCart){
                    cartList.add(PB);
                    Log.d("MYAITALIA", "1");
                    isAddedToCart = true;
                    PB.setAddedToCart(isAddedToCart);
                    PB.setProductSelectedImage(R.drawable.removeitem);
                    PB.setProductSelectedText("Remove Item");
                    dataInterface.setValues(cartList);
                    viewHolder.toggleCart.setBackgroundResource(productBeanList.get(position).getProductSelectedImage());
                    viewHolder.txtToggleCart.setText(productBeanList.get(position).getProductSelectedText());

                }

                else{
                    cartList.remove(PB);
                    Log.d("MYAITALIA", "0");
                    isAddedToCart = false;
                    PB.setAddedToCart(isAddedToCart);
                    PB.setProductSelectedImage(R.drawable.additem);
                    PB.setProductSelectedText("Add To Cart");
                    dataInterface.setValues(cartList);
                    viewHolder.toggleCart.setBackgroundResource(productBeanList.get(position).getProductSelectedImage());
                    viewHolder.txtToggleCart.setText(productBeanList.get(position).getProductSelectedText());

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return productBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        Button toggleCart;
        public TextView txtProductName, txtProductPrice, txtToggleCart;
        public ImageView imgProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;


            toggleCart = mView.findViewById(R.id.btn_toggle_cart);
            txtProductName=(TextView)mView.findViewById(R.id.txt_prd_name);
            txtProductPrice = mView.findViewById(R.id.txt_prd_price);
            txtToggleCart = mView.findViewById(R.id.txt_toggle_cart);
            imgProduct = mView.findViewById(R.id.img_prd);


        }
    }
}
