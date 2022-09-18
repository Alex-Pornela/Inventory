package com.activity.productinventory.ViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.activity.productinventory.Database.DBHelper;
import com.activity.productinventory.Model.Product;
import com.activity.productinventory.View.MainActivity;

import java.util.List;


public class ProductViewModel extends ViewModel implements DBHelper.ViewData {

    DBHelper repository;
    MutableLiveData<List<Product>> productLiveData = new MutableLiveData<>();

    public ProductViewModel(){
        repository = new DBHelper( MainActivity.getContextOfApplication(), this);
        repository.getAllData();
    }

    public LiveData<List<Product>> getProductLiveData(){
        return productLiveData;
    }

    public void insertData(Product product){
        repository.insertProduct( product );
    }

    public void deleteData(String name){
        repository.deleteProduct( name );
    }

    public void updateData(Product product,String name){
        repository.updateProduct( product, name );
    }


    @Override
    public void onProductAdded(List<Product> productList) {
        productLiveData.postValue(productList);
    }
}
