package com.example.duan1.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duan1.MainActivity;
import com.example.duan1.R;
import com.example.duan1.dagTinGiaiTriActivity;
import com.example.duan1.dangTinBDSActivity;
import com.example.duan1.dangTinBDSDatActivity;
import com.example.duan1.dangTinBDSPhongTroActivity;
import com.example.duan1.dangTinThoiTrangActivity;
import com.example.duan1.model.BDSNews;
import com.example.duan1.model.giaiTriNews;
import com.example.duan1.model.historyNews;
import com.example.duan1.model.thoiTrangNews;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class historyNewsAdapter extends RecyclerView.Adapter<historyNewsAdapter.HolderView>  {
    private Context mContext;
    private List<historyNews> listHistory;
    private List<String> listChild = new ArrayList<>();
    private List<String> listChild1 = new ArrayList<>();
    private List<String> listChild2 = new ArrayList<>();

    private MainActivity mainActivity;
//    private String name = FragmentQuanLiTin.nameUser;

    DatabaseReference myData = FirebaseDatabase.getInstance().getReference("Tin");

    public historyNewsAdapter(Context mContext, MainActivity mainActivity, List<historyNews> listHistory) {
        this.mContext = mContext;
        this.listHistory = listHistory;
        this.mainActivity = mainActivity;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quan_ly_tin_dang, parent, false);

        return new HolderView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderView holder,final int position) {
        historyNews giaiTriNews =  listHistory.get(position);
        String tenDanhMuc = giaiTriNews.getTenDanhMuc();
        String Title_Post = giaiTriNews.getTitle_historyNews();
        listChild1();
        listChild2();
        listChild3();
        holder.title_historyNews.setText(giaiTriNews.getTitle_historyNews());
        holder.desc_historyNews.setText(giaiTriNews.getDesc_historyNews());
        holder.time_historyNews.setText(giaiTriNews.getTime_historyNews());
        String strImage = giaiTriNews.getImage();
        Glide.with(mContext).load(strImage).into(holder.imgSP);
        holder.icon_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setMessage("Bạn có chắc chắn muốn xóa tin này?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // START THE GAME!
                                LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                                        mainActivity, R.anim.layout_delete_item_right_to_left
                                );
                                holder.layout_item.setLayoutAnimation(layoutAnimationController);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        XoaTin(giaiTriNews.getId());
                                    }
                                },500);

                            }
                        })
                        .setNegativeButton("Trở lại", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                builder.create();
                builder.show();


            }
        });

        int id = giaiTriNews.getId();
//        if(name.equals("Admin")){
//            holder.layout_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(mContext , chitiet_news.class);
//                    intent.putExtra("title" , giaiTriNews.getTitle_historyNews());
//                    mContext.startActivity(intent);
//                }
//            });
//        }else {

        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tenDanhMuc.equals("Quần áo") || tenDanhMuc.equals("Đồng hồ") || tenDanhMuc.equals("Giày dép") ||tenDanhMuc.equals("Túi xách")
                || tenDanhMuc.equals("Nước hoa") ){
                    Intent intent = new Intent(mContext , dangTinThoiTrangActivity.class);
                    intent.putExtra("title1" , Title_Post);
                    intent.putExtra("tenDanhMuc" , tenDanhMuc);
                    intent.putExtra("idTT", id);
                    mContext.startActivity(intent);
                }else if(tenDanhMuc.equals("Phòng trọ")) {
                    Intent intent = new Intent(mContext , dangTinBDSPhongTroActivity.class);
                    intent.putExtra("title1" , Title_Post);
                    intent.putExtra("tenDanhMuc" , tenDanhMuc);
                    intent.putExtra("idPT", id);
                    mContext.startActivity(intent);
                }else if(tenDanhMuc.equals("Đất")) {
                    Intent intent = new Intent(mContext , dangTinBDSDatActivity.class);
                    intent.putExtra("title" , Title_Post);
                    intent.putExtra("tenDanhMuc" , tenDanhMuc);
                    intent.putExtra("idD", id);
                    mContext.startActivity(intent);
                }else if(tenDanhMuc.equals("Văn Phòng")
                        || tenDanhMuc.equalsIgnoreCase("Nhà ở")
                        || tenDanhMuc.equalsIgnoreCase("Chung cư")){

                    Intent intent = new Intent(mContext , dangTinBDSActivity.class);
                    intent.putExtra("title" , Title_Post);
                    intent.putExtra("tenDanhMuc" , tenDanhMuc);
                    intent.putExtra("idBDS", id);
                    mContext.startActivity(intent);
                }else if(mainActivity.name.equals("Admin")){
                    Toast.makeText(mContext, "đang chờ layout", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(mContext , dagTinGiaiTriActivity.class);
                    intent.putExtra("title" , Title_Post);
                    intent.putExtra("tenDanhMuc" , tenDanhMuc);
                    intent.putExtra("idGT", id);
                    mContext.startActivity(intent);
                }
            }
        });
//        }
    }

    @Override
    public int getItemCount() {
        if (listHistory != null) {
            return listHistory.size();
        } else {
            return 0;
        }
    }
private void XoaTin(int idDelete){
    myData.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot snapshot1: snapshot.getChildren()){
                giaiTriNews giaiTriNews = snapshot1.getValue(giaiTriNews.class);
                if(giaiTriNews.getId() == idDelete){
                    String id = String.valueOf(giaiTriNews.getId());
                    myData.child(id).removeValue();
                    Toast.makeText(mContext.getApplicationContext(), "Đã xóa Tin", Toast.LENGTH_SHORT).show();
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}

    public class HolderView extends RecyclerView.ViewHolder {
        private TextView title_historyNews, desc_historyNews, time_historyNews;
        private ImageView icon_option, imgSP;
    private CardView layout_item;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            title_historyNews = itemView.findViewById(R.id.title_historyNews);
            desc_historyNews = itemView.findViewById(R.id.desc_historyNews);
            time_historyNews = itemView.findViewById(R.id.time_historyNews);
            icon_option = itemView.findViewById(R.id.icon_option);
            layout_item = itemView.findViewById(R.id.layout_item);
            imgSP = itemView.findViewById(R.id.imgTinDang);

        }




    }

    private void suaTin() {
    }

    private List<String> listChild1() {
        listChild.add(new String("Nhạc cụ"));
        listChild.add(new String("Sách"));
        listChild.add(new String("Đồ thể thao, Dã ngoại"));
        listChild.add(new String("Đồ sưu tầm  ,Đồ cổ"));
        listChild.add(new String("Thiết bị chơi game"));
        listChild.add(new String("Sở thích khác"));

        return listChild;
    }

    private List<String> listChild2() {
        listChild1.add(new String("Quần áo"));
        listChild1.add(new String("Đồng hồ"));
        listChild1.add(new String("Giày dép"));
        listChild1.add(new String("Túi xách"));
        listChild1.add(new String("Nước hoa"));
        listChild1.add(new String("Phụ kiện khác"));

        return listChild1;
    }

    private List<String> listChild3() {
        listChild2.add(new String("Chung cư"));
        listChild2.add(new String("Nhà ở"));
        listChild2.add(new String("Đất"));
        listChild2.add(new String("Văn Phòng"));
        listChild2.add(new String("Phòng trọ"));

        return listChild2;
    }

}
