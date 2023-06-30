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
import com.example.duan1.model.dienMay;

import java.util.List;

public class dmucDienMayAdapter extends RecyclerView.Adapter<dmucDienMayAdapter.ViewHolder> {
    private Context mContext;
    private List<dienMay> listDienMay;

    public dmucDienMayAdapter(Context mContext, List<dienMay> listDienMay) {
        this.mContext = mContext;
        this.listDienMay = listDienMay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_muc_dien_may , parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dienMay mDienMay = listDienMay.get(position);

        holder.tenDanhMuc.setText(mDienMay.getName());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "danh mục " + mDienMay.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listDienMay != null) {
            return listDienMay.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenDanhMuc;
        LinearLayout layout_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
            layout_item = itemView.findViewById(R.id.layout_item_dienMay);

        }
    }
}
