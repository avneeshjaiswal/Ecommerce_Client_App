package com.example.oem.ecommerce.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.oem.ecommerce.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avneesh jaiswal on 07-Feb-18.
 */

public class Database extends SQLiteAssetHelper {

    private static final String DB_Name = "OrderDetail.db" ;
    private static final int DB_VER= 2 ;
    private Context context;
    public Database(Context context) {
        super(context, DB_Name, null, DB_VER);
        setForcedUpgrade();

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            context.deleteDatabase(DB_Name);
            new Database(context);
        }else
            super.onUpgrade(db, oldVersion, newVersion);
    }

    public List<Order> getCarts(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb  = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","ProductId","ProductName","Quantity","Price"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                //getting more orders from cart db
                result.add(new Order(
                        c .getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price"))));

            }while (c.moveToNext());
        }
        return result;
    }

    //adding details to cart using sqlite
    public void addToCart(Order order){

       /* SQLiteDatabase db1 = getReadableDatabase();
        String query = String.format("Select * from OrderTable");
        db1.execSQL(query);*/

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(UserPhone,ProductId,ProductName,Quantity,Price)VALUES('%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice());

        db.execSQL(query);
        db.close();
    }
    //deleting items from cart using sqlite db
    public void deleteFromCart(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone = '%s'",userPhone);
        db.execSQL(query);
    }

    //Favourites
    public void addToFavourites(String foodId,String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favourite(FoodId,UserPhone) VALUES('%s','%s');",foodId,userPhone);

        db.execSQL(query);
    }

    public void removeFromFavourites(String foodId,String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favourite WHERE FoodId='%s' and UserPhone='%s';",foodId,userPhone);
        db.execSQL(query);
    }

    public int getCountCart(String userPhone) {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserPhone='%s'",userPhone);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                count = cursor.getInt(0);

            }while(cursor.moveToNext());
        }
        return count;
    }

    public boolean checkFoodExists(String foodId, String userPhone){
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String SQLQuery = String.format("SELECT * From OrderDetail WHERE UserPhone = '%s' AND ProductId = '%s'",userPhone,foodId);
        cursor = db.rawQuery(SQLQuery,null);
        if(cursor.getCount()>0){
            flag = true;
        }else{
            flag = false;
        }
        return flag;
    }
    public void updateCart(Order order) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity= %s WHERE Phone ='%s' AND ProductId = '%s'",order.getQuantity(),order.getUserPhone(),order.getProductId());
        db.execSQL(query);
    }

    public void increaseCart(String userPhone, String foodId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity = Quantity+1 WHERE Phone = '%s' AND ProductId  = '%s'",userPhone,foodId);
        db.execSQL(query);
    }

    public void removeFromCart(String productId, String number) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone ='%s' and ProductId = '%s'",number,productId);
        db.execSQL(query);
    }

    public boolean isFavourites(String foodId,String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favourite WHERE FoodId='%s' and UserPhone='%s';",foodId,userPhone);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        return true;
    }

   /* public void updateCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity=%s WHERE ID=%d",order.getQuantity());
        db.execSQL(query);
    }*/

    /*public List<Favorites> getAllFavorites(String userPhone){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb  = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodId","UserPhone","FoodName","FoodPrice","FoodImage","FoodDescription"};
        String sqlTable = "Favorites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[]{userPhone},null,null,null);

        final List<Favorites> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                //getting more orderrs from cart db
                result.add(new Favorites(c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("FoodImage")),
                        c.getString(c.getColumnIndex("FoodDescription")),
                        c.getString(c.getColumnIndex(   "UserPhone"))));

            }while (c.moveToNext());
        }
        return result;
    }*/
}
