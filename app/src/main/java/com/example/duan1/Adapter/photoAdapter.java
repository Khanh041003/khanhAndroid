package com.example.duan1.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.duan1.R;

import java.util.ArrayList;

public class photoAdapter extends RecyclerView.Adapter<photoAdapter.ViewHolder> {

    private ArrayList<Uri> imageUris;
    private Context mContext;
    CountOfImageWhenRemove countOfImageWhenRemove;

    public photoAdapter(ArrayList<Uri> imageUris, Context mContext , CountOfImageWhenRemove countOfImageWhenRemove) {
        this.imageUris = imageUris;
        this.mContext = mContext;
        this.countOfImageWhenRemove = countOfImageWhenRemove;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_add_news , parent,false);
        return new ViewHolder(view ,countOfImageWhenRemove);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.showImage.setImageURI(imageUris.get(position));
        Glide.with(mContext).load(imageUris.get(position)).into(holder.showImage);
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUris.remove(imageUris.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position , getItemCount());
                countOfImageWhenRemove.clicked(imageUris.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(imageUris != null) {
            return imageUris.size();
        }else {
            return 0;

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView showImage;
        private ImageView deleteImg;
        CountOfImageWhenRemove countOfImageWhenRemove;
        public ViewHolder(@NonNull View itemView, CountOfImageWhenRemove countOfImageWhenRemove) {
            super(itemView);
            this.countOfImageWhenRemove = countOfImageWhenRemove;
            showImage = itemView.findViewById(R.id.img_add_news);
            deleteImg = itemView.findViewById(R.id.deleteImg);
        }
    }
    public interface CountOfImageWhenRemove {
        void clicked(int getSize);
    }
}
