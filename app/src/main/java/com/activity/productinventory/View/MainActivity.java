package com.activity.productinventory.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.activity.productinventory.Adapter.ProductAdapter;
import com.activity.productinventory.Model.Product;
import com.activity.productinventory.ViewModel.ProductViewModel;
import com.activity.productinventory.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnItemClicked {

    private ActivityMainBinding binding;
    List<Product> productList = new ArrayList<>();
    ProductAdapter adapter;
    ProductViewModel viewModel;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot());
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        context = getApplicationContext();

        loadData();

        binding.add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProduct.class );
                startActivity( intent );
            }
        } );

        binding.history.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "Currently, this feature is not accessible.", Toast.LENGTH_SHORT ).show();
            }
        } );
        binding.search.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( MainActivity.this, "Currently, this feature is not accessible.", Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    private void loadData() {
        binding.recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        binding.recyclerView.setHasFixedSize( true );
        adapter = new ProductAdapter(this, productList );
        binding.recyclerView.setAdapter( adapter );
        viewModel = new ViewModelProvider(this).get( ProductViewModel.class );
        viewModel.getProductLiveData().observe( this, new Observer<List<Product>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<Product> product) {
                productList = product;
                adapter.updateAdapter( product );
            }
        } );
    }


    public static Context getContextOfApplication() {
        return context;
    }

    @Override
    public void itemDelete(int position, String name) {
        viewModel.deleteData( name );
    }

    @Override
    public void itemUpdate(int position, String name) {
        Product product = productList.get( position );
        Intent intent = new Intent(MainActivity.this, UpdateProduct.class);
        intent.putExtra( "item", product );
        intent.putExtra( "itemName", name );
        startActivity( intent );
    }
}