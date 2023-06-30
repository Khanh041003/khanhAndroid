package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.xeCo;

import java.util.ArrayList;
import java.util.List;

public class dmucXeCoAdapter extends RecyclerView.Adapter<dmucXeCoAdapter.ViewHolder> {
    private Context mContext ;
    private List<xeCo> listXe = new ArrayList<>();

    public dmucXeCoAdapter(Context mContext, List<xeCo> listXe) {
        this.mContext = mContext;
        this.listXe = listXe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dmuc_xe_co, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        xeCo mXeCo = listXe.get(position);
        holder.tenDanhMuc.setText(mXeCo.getTen());
        holder.layout_item_xeCo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Danh mục " + mXeCo.getTen(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listXe != null ){
            return listXe.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenDanhMuc;
        LinearLayout layout_item_xeCo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            layout_item_xeCo = itemView.findViewById(R.id.layout_item_xeCo);
        }
    }
}
