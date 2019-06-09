package com.example.oem.ecommerce.Model;

/**
 * Created by avneesh jaiswal on 07-Feb-18.
 */

public class Order {
    private String UserPhone;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;

    public Order() {
    }

    public Order(String phone, String productId, String productName, String quantity, String price) {
        this.UserPhone = phone;
        this.ProductId = productId;
        this.ProductName = productName;
        this.Quantity = quantity;
        this.Price = price;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String phone) {
        UserPhone = phone;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
