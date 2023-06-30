package com.example.duan1.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.dienTu;

import java.util.List;

public class dmucDoDienTuAdapter extends RecyclerView.Adapter<dmucDoDienTuAdapter.ViewHolder> {
    private Context mContext;
    private List<dienTu> listDienTu;

    public dmucDoDienTuAdapter(Context mContext, List<dienTu> listDienTu) {
        this.mContext = mContext;
        this.listDienTu = listDienTu;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dmuc_dien_tu , parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dienTu mDienTu = listDienTu.get(position);

        holder.tvTenDanhMuc.setText(mDienTu.getTen());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Danh mục " + mDienTu.getTen(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listDienTu != null) {
            return listDienTu.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMuc;
        LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.layout_item_DienTu);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMucDienTu);
        }
    }
}
