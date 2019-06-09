package com.example.oem.ecommerce.Model;

import java.util.List;

/**
 * Created by avneesh jaiswal on 08-Feb-18.
 */

public class Request {
    private String res_id;
    private String phone;
    private String name;
    private String address;
    private String total;
    private String status;
    private String comment;
    private String payment;
    private String payment_method;
    private String LatLng;

    private List<Order> foods;

    
    public Request(String phone, String name, String address, String total, String status, String comment, String payment, String payment_method, String latLng, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.payment = payment;
        this.payment_method = payment_method;
        LatLng = latLng;
        this.foods = foods;
    }

    public Request() {

    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
