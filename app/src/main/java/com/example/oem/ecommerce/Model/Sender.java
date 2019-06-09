package com.example.oem.ecommerce.Model;

import android.app.Notification;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public class Sender {
    public String to;
    public Notification notification;

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }
}
