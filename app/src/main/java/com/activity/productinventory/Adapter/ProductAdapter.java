package com.activity.productinventory.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.TypefaceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.activity.productinventory.Model.Product;
import com.activity.productinventory.R;
import com.activity.productinventory.View.MainActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    List<Product> productList;
    OnItemClicked onItemClicked;

    public ProductAdapter( OnItemClicked onItemClicked, List<Product> productList){
        this.productList = productList;
        this.onItemClicked = onItemClicked;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter(List<Product> productList){
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.product_list_layout, parent, false );
        return new myViewHolder( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.myViewHolder holder, int position) {
        String name = productList.get( position ).getName();
        holder.name.setText( name );
        holder.unit.setText( productList.get( position ).getUnit() );
        holder.dateExpiry.setText( productList.get( position ).getDate() );
        holder.inventory.setText( String.valueOf( productList.get( position ).getInventory() )  );

        double price = productList.get( position ).getPrice();
        holder.price.setText( "₱" + String.format("%.2f", price));

       double inventoryCost = productList.get( position ).getInventoryPrice();
        holder.inventoryCost.setText( "₱"+ String.format("%.2f", inventoryCost) );

        int[] androidColors = MainActivity.getContextOfApplication().getResources().getIntArray(R.array.colors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        holder.imgBg.setCardBackgroundColor(randomAndroidColor);

        String img = productList.get( position ).getImage();
        Uri imgUri = Uri.parse( img );

        Glide.with( MainActivity.getContextOfApplication() ).load( imgUri ).into( holder.productImg );

        holder.update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClicked != null){
                    onItemClicked.itemUpdate( holder.getAdapterPosition(), name );
                }
            }
        } );
        holder.delete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClicked != null){
                    onItemClicked.itemDelete( (holder.getAdapterPosition() ), name);
                    productList.remove( holder.getAdapterPosition()  );
                    notifyItemRemoved( holder.getAdapterPosition()  );
                    notifyItemRangeChanged(holder.getAdapterPosition() , productList.size());
                }
            }
        } );
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }else {
            return 0;
        }

    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg;
        TextView name,price,unit,inventory,dateExpiry,inventoryCost;
        MaterialCardView imgBg;
        MaterialButton update,delete;

        public myViewHolder(@NonNull View itemView) {
            super( itemView );

            productImg = itemView.findViewById( R.id.productImg );
            name = itemView.findViewById( R.id.productName );
            price = itemView.findViewById( R.id.productPrice );
            unit = itemView.findViewById( R.id.productUnit );
            inventory = itemView.findViewById( R.id.productInventory );
            dateExpiry = itemView.findViewById( R.id.productExpiry );
            inventoryCost = itemView.findViewById( R.id.productInventoryCost );
            imgBg = itemView.findViewById( R.id.cardImg );
            update = itemView.findViewById( R.id.updateBtn );
            delete = itemView.findViewById( R.id.deletebtn );
        }
    }

    public interface OnItemClicked{
        void itemDelete(int position, String name);
        void itemUpdate(int position, String name);
    }
}
