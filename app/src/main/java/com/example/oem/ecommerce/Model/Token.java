package com.example.oem.ecommerce.Model;

/**
 * Created by avneesh jaiswal on 14-Mar-18.
 */

public class Token {

    private String token;
    private boolean isServerToken;

    public Token() {
    }

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }
}
