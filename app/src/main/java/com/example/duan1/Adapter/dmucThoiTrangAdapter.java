package com.example.duan1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.chonDanhMucThoiTrangAcrivity;
import com.example.duan1.dangTinThoiTrangActivity;
import com.example.duan1.model.thoiTrang;
import com.example.duan1.model.xeCo;

import java.util.ArrayList;
import java.util.List;

public class dmucThoiTrangAdapter extends RecyclerView.Adapter<dmucThoiTrangAdapter.ViewHolder> {
    private Context mContext ;
    private List<thoiTrang> listThoiTrang ;
    chonDanhMucThoiTrangAcrivity chonDanhMucThoiTrangAcrivity;

    public dmucThoiTrangAdapter(Context mContext, List<thoiTrang> listThoiTrang) {
        this.mContext = mContext;
        this.listThoiTrang = listThoiTrang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_muc_thoi_trang, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        thoiTrang mThoiTrang = listThoiTrang.get(position);

        holder.tenDanhMuc.setText(mThoiTrang.getTenThoiTrang());



        holder.layout_item_thoiTrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , dangTinThoiTrangActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tenDanhMuc" , mThoiTrang.getTenThoiTrang());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listThoiTrang != null ){
            return listThoiTrang.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenDanhMuc;
        LinearLayout layout_item_thoiTrang;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            layout_item_thoiTrang = itemView.findViewById(R.id.layout_item_thoiTrang);
        }
    }
}
