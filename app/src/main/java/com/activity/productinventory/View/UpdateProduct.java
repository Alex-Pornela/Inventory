package com.activity.productinventory.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.activity.productinventory.Model.Product;
import com.activity.productinventory.R;
import com.activity.productinventory.ViewModel.ProductViewModel;
import com.activity.productinventory.databinding.ActivityUpdateProductBinding;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateProduct extends AppCompatActivity {

    private ActivityUpdateProductBinding binding;
    Product product;
    private String expiryDate;
    private Uri selectedImageUri;
    ProductViewModel viewModel;
    String itemName;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        binding = ActivityUpdateProductBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot());
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        viewModel = new ViewModelProvider(this).get( ProductViewModel.class );

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        product = (Product) bundle.getSerializable("item");
        if (product != null){
            setDataToInputFields();
        }
        itemName = getIntent().getStringExtra( "itemName" );

        binding.updateProductBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        } );

        binding.backBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(UpdateProduct.this,MainActivity.class) );
                finish();
            }
        } );

        binding.edExpiry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                binding.edExpiry.requestFocus();
                binding.edExpiry.setInputType( InputType.TYPE_NULL );

                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        calendar.set( Calendar.YEAR, year );
                        calendar.set( Calendar.MONTH, month );
                        calendar.set( Calendar.DAY_OF_MONTH, day );

                        String myFormat = "MMMM d, yyyy"; // In which you need put here
                        String monthFormat = "MMMM";
                        SimpleDateFormat sdf = new SimpleDateFormat( myFormat, Locale.US );
                        SimpleDateFormat sddf = new SimpleDateFormat( monthFormat, Locale.US );
                        expiryDate= sddf.format( calendar.getTime() );

                        binding.edExpiry.setText( sdf.format( calendar.getTime() ) );
                    }
                };
                new DatePickerDialog( UpdateProduct.this, onDateSetListener, calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ),
                        calendar.get( Calendar.DAY_OF_MONTH ) ).show();
            }
        } );

        binding.cardImg.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImg();
            }
        } );
    }

    private void updateData() {
        String img;
        String name = binding.edName.getEditableText().toString();
        if (selectedImageUri == null){
            img = product.getImage();
        }else{
            img = selectedImageUri.toString();
        }

        String unit = binding.edUnit.getEditableText().toString();
        String date = binding.edExpiry.getEditableText().toString();

        int inventory;
        if(binding.edInventory.getEditableText().toString().isEmpty()){
            inventory = 0;
        }else {
            inventory = Integer.parseInt( binding.edInventory.getEditableText().toString() );
        }
        double price;

        if(binding.edPrice.getEditableText().toString().isEmpty()) {
            price = 0.0;
        }else {
            price = Double.parseDouble( binding.edPrice.getEditableText().toString() );
        }
        double inventoryPrice = inventory * price;

        product = new Product(name,img,unit,price,date,inventory,inventoryPrice);
        viewModel.updateData( product, itemName);

        Toast.makeText( UpdateProduct.this, "Product Updated", Toast.LENGTH_SHORT ).show();
        startActivity( new Intent(UpdateProduct.this, MainActivity.class ) );
        finish();
    }

    private void setDataToInputFields() {
        binding.edName.setText( product.getName() );
        binding.edUnit.setText( product.getUnit() );
        binding.edPrice.setText( String.valueOf( product.getPrice() ) );
        binding.edExpiry.setText( product.getDate() );
        binding.edInventory.setText( String.valueOf( product.getInventory() ) );
        if(selectedImageUri == null){
            image = product.getImage();
            Uri imgUri = Uri.parse( image );
            Glide.with( this ).load( imgUri ).into( binding.productImg);
        }
    }

    private void selectImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(Intent.createChooser(intent,
                "Select Picture"));
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if(data != null  && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Glide.with( MainActivity.getContextOfApplication() ).load( selectedImageUri ).into( binding.productImg );
                        }
                    }
                }
            });
}