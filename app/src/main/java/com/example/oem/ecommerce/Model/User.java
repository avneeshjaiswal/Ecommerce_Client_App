package com.example.oem.ecommerce.Model;

/**
 * Created by oem on 13-Jan-18.
 */

public class User {
    private String Name;
    private String Number;
    private String Password;
    //private String IsStaff;

    /*public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
*/
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public User(String name, String number, String password) {
        Name = name;
        Number = number;
        Password = password;
  //      IsStaff = "false";
    }
    public User(String name,String password){
        Name = name;
        Password = password;

    }

    public User() {
    }
}
