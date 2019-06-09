package com.example.oem.ecommerce.Model;

/**
 * Created by avneesh jaiswal on 15-Jan-18.
 */

public class RestaurantNameWithImage {
    private String Name;
    private String Image;

    public RestaurantNameWithImage(){

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public RestaurantNameWithImage(String res_name, String res_image){
        Name = res_name;
        Image = res_image;

    }
}
