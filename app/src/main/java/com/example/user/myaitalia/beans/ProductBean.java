package com.example.user.myaitalia.beans;

import com.example.user.myaitalia.R;

public class ProductBean {
    int productID;
    String productName;
    int productPrice;
    int productImage;
    int productQuantity;
    int productSelectedImage = R.drawable.additem;
    String productSelectedText = "Add To Cart";
    boolean isAddedToCart;
    String orderDate ;

    public ProductBean(int productID, String productName, int productPrice,int productQuantity , int productImage, boolean isAddedToCart) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.isAddedToCart = isAddedToCart;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public ProductBean(int productID, String productName, int productPrice, int productQuantity , int productImage, boolean isAddedToCart, String orderDate) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.isAddedToCart = isAddedToCart;
        this.orderDate = orderDate;
    }

    public int getProductSelectedImage() {
        return productSelectedImage;
    }

    public void setProductSelectedImage(int productSelectedImage) {
        this.productSelectedImage = productSelectedImage;
    }

    public String getProductSelectedText() {
        return productSelectedText;
    }

    public void setProductSelectedText(String productSelectedText) {
        this.productSelectedText = productSelectedText;
    }

    public boolean isAddedToCart() {
        return isAddedToCart;
    }

    public void setAddedToCart(boolean addedToCart) {
        isAddedToCart = addedToCart;
    }

    public ProductBean() {
    }

    public double getProductTotal(){
        return productPrice* productQuantity;
    }
    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }



    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }
}
