package com.activity.productinventory.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.activity.productinventory.Model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Product.db";
    public static final String TABLE_TEST = "ProductDetails";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PIC = "image";
    public static final String COLUMN_ONE = "unit";
    public static final String COLUMN_TWO = "price";
    public static final String COLUMN_Three = "date";
    public static final String COLUMN_Four = "inventory";
    public static final String COLUMN_Five = "inventoryprice";
    Product product;
    ViewData viewData;

    public DBHelper(Context context, ViewData viewData) {
        super(context, DATABASE_NAME, null, 1 );
        this.viewData = viewData;
    }


    @Override
    public void onCreate(SQLiteDatabase DB) {
        String query = "CREATE TABLE " + TABLE_TEST + "(" +
                COLUMN_NAME + " TEXT PRIMARY KEY," +
                COLUMN_PIC + " TEXT," +
                COLUMN_ONE + " TEXT," +
                COLUMN_TWO + " FLOAT(4,2)," + COLUMN_Three + " TEXT," +
                COLUMN_Four + " INTEGER," + COLUMN_Five + " FLOAT(4,2)" + ");";
        DB.execSQL( query );
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL( "DROP TABLE if exists " + TABLE_TEST );
    }

    public void insertProduct(Product product){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_NAME, product.getName() );
        contentValues.put( COLUMN_PIC, product.getImage() );
        contentValues.put( COLUMN_ONE, product.getUnit() );
        contentValues.put( COLUMN_TWO,  product.getPrice() );
        contentValues.put( COLUMN_Three,  product.getDate());
        contentValues.put( COLUMN_Four,  product.getInventory() );
        contentValues.put( COLUMN_Five, product.getInventoryPrice());
        DB.insert( "ProductDetails", null, contentValues );

    }

    public void getAllData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        String table = "SELECT * FROM " +TABLE_TEST;
        Cursor cursor = DB.rawQuery( table,null );
        List<Product> productList = new ArrayList<>();

        while (cursor.moveToNext()){
            try {
                String name = cursor.getString(0);
                String img = cursor.getString(1);
                String unit = cursor.getString(2);
                double price = cursor.getDouble(3);
                String date = cursor.getString(4);
                int inventory = cursor.getInt(5);
                double inventoryPrice = cursor.getDouble(6);
                product = new Product(name,img,unit,price,date,inventory,inventoryPrice);
                productList.add( product );
                viewData.onProductAdded( productList );

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        cursor.close();
    }

    public void deleteProduct(String itemName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEST, "name=?", new String[]{itemName});
    }

    public void updateProduct(Product product,String name){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_NAME, product.getName() );
        contentValues.put( COLUMN_PIC, product.getImage() );
        contentValues.put( COLUMN_ONE, product.getUnit() );
        contentValues.put( COLUMN_TWO,  product.getPrice() );
        contentValues.put( COLUMN_Three,  product.getDate());
        contentValues.put( COLUMN_Four,  product.getInventory() );
        contentValues.put( COLUMN_Five, product.getInventoryPrice());
        DB.update(TABLE_TEST, contentValues, "name=?", new String[]{name});
    }

    public interface ViewData{
        void onProductAdded(List<Product>productList);
    }
}
