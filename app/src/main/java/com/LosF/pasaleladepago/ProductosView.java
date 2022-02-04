package com.LosF.pasaleladepago;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductosView extends RecyclerView.Adapter<ProductosView.ViewHolder>{
    private List<Productos> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ProductosView(List<Productos> itemList,Context context){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemList;
    }


    @Override

    public int getItemCount(){
        return mData.size();
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view=mInflater.inflate(R.layout.item_skins,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<Productos> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cartaV;
        TextView Nombre,Precio;
        ImageButton Comprar;
        ViewHolder(View itemView){
            super(itemView);
            cartaV=itemView.findViewById(R.id.CartaVen);
            Nombre =itemView.findViewById(R.id.ProductoNom);
            Precio=itemView.findViewById(R.id.ProductoPrec);
            Comprar=itemView.findViewById(R.id.btnComprar);
        }

        void bindData(final Productos item){

            cartaV.setImageResource(item.getImagen());
            Nombre.setText(item.getNombre());
            Precio.setText(item.getPrecio());
        }

    }

}
