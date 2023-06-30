package com.example.duan1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.dagTinGiaiTriActivity;
import com.example.duan1.dangTinThoiTrangActivity;
import com.example.duan1.model.giaiTri;

import java.util.List;

public class dmucTheThaoAdapter extends RecyclerView.Adapter<dmucTheThaoAdapter.ViewHolder> {

    private Context mContext;
    private List<giaiTri> listGiaiTri;

    public dmucTheThaoAdapter(Context mContext, List<giaiTri> listGiaiTri) {
        this.mContext = mContext;
        this.listGiaiTri = listGiaiTri;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_muc_the_thao, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        giaiTri mGiaiTri = listGiaiTri.get(position);

        holder.tenDanhMuc.setText(mGiaiTri.getTen());
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext , dagTinGiaiTriActivity.class);
                intent.putExtra("tenDanhMuc" , mGiaiTri.getTen());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listGiaiTri != null) {
            return listGiaiTri.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenDanhMuc;
        LinearLayout layout_item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_item = itemView.findViewById(R.id.layout_item_theThao);
            tenDanhMuc = itemView.findViewById(R.id.tvTenDanhMucTheThao);
        }
    }
}
