package com.example.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.R;
import com.example.duan1.model.chitietdacdiem;

import java.util.List;


public class chitietDacDiemAdapter extends RecyclerView.Adapter<chitietDacDiemAdapter.ViewHolder> {
    private Context context;
    private List<chitietdacdiem> chitietdacdiemList;

    public chitietDacDiemAdapter(Context context, List<chitietdacdiem> chitietdacdiemList) {
        this.context = context;
        this.chitietdacdiemList = chitietdacdiemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_chitiet_dacdiem,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        chitietdacdiem OOP = chitietdacdiemList.get(position);

    if(h.mota == null || h.tenDacDiem == null) {
        try {
            h.mota.setText(OOP.getMota());
            h.tenDacDiem.setText(OOP.getName());
        }catch (Exception e){

        }
    }else {
        h.mota.setText(OOP.getMota());
        h.tenDacDiem.setText(OOP.getName());
    }

    }

    @Override
    public int getItemCount() {
        if(chitietdacdiemList != null){
            return chitietdacdiemList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    private TextView mota ,tenDacDiem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mota = itemView.findViewById(R.id.tv_chitiet_dacdiem_item);
            tenDacDiem = itemView.findViewById(R.id.tvTenDacDiem);
        }
    }
}
