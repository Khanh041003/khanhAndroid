package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.duan1.Adapter.NewsTrangChuAdapter;
import com.example.duan1.Adapter.chitietDacDiemAdapter;
import com.example.duan1.Adapter.listUserAdapter;
import com.example.duan1.model.BDSNews;
import com.example.duan1.model.NewsTrangChu;
import com.example.duan1.model.Users;
import com.example.duan1.model.chitietdacdiem;
import com.example.duan1.model.giaiTriNews;
import com.example.duan1.model.thoiTrangNews;
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


public class chitiet_news extends AppCompatActivity {
    private NewsTrangChuAdapter newsTrangChuAdapter;
    private chitietDacDiemAdapter chitietDacDiemAdapter;
    private RecyclerView rcvTinkhac1, rcvTinkhac2, rcvDacdiem;
    private ImageView imgavatar, imgfavoriteheart, imgPic , icon_back;
    private TextView tvTitle, tvFee, tvaddress, tvaddressMap, tvTime,
            tvTitleDacDiem, tvTitleMota, tvMota, tvMotaLienhe,
            tvNameNguoiBan, tvXemTrang, tvTitleTinkhac1, tvTitleTinkhac2 ,tvSdtUser;
    private LinearLayout layoutDacDiem;
    private int id , idUser;
    ProgressDialog progressDialog;
    private String Loai, title_new , strImageAvtUser, strSoDienThoai ,addressUser , tenDanhMuc;
    private List<NewsTrangChu> newsTrangChuList1, newsTrangChuList2;
    Locale localeEN = new Locale("en", "EN");
    NumberFormat en = NumberFormat.getInstance(localeEN);
    //    1. cùng người bán
//    2. cùng loại tin
    private List<chitietdacdiem> chitietdacdiemList = new ArrayList<>();

    DatabaseReference myData = FirebaseDatabase.getInstance().getReference("Tin").child("GiaiTri");
    DatabaseReference myData1 = FirebaseDatabase.getInstance().getReference("Tin").child("BDS");
    DatabaseReference myData2 = FirebaseDatabase.getInstance().getReference("Tin").child("ThoiTrang");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_news);
        unitUI();
        newsTrangChuList1 = new ArrayList<>();
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(chitiet_news.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.show();

//
//        id = bundle.getInt("id");
//        Loai = bundle.getString("Loai");
        getUser();
//        setData();
        getListNews();

        rcvDacdiem = findViewById(R.id.rcv_chitiet_dacdiem);
        chitietDacDiemAdapter = new chitietDacDiemAdapter(this , chitietdacdiemList);
        rcvDacdiem.setLayoutManager(new GridLayoutManager(chitiet_news.this , 2));
        rcvDacdiem.setAdapter(chitietDacDiemAdapter);
        rcvDacdiem.getRecycledViewPool().clear();
        chitietDacDiemAdapter.notifyDataSetChanged();
        getTinDangLienQuan();


//        setDataTinCungSeller();
        rcvTinkhac1 = findViewById(R.id.rcv_chitiet_tinkhac1);
        newsTrangChuAdapter = new NewsTrangChuAdapter(chitiet_news.this ,newsTrangChuList1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvTinkhac1.setLayoutManager(linearLayoutManager);
        rcvTinkhac1.setAdapter(newsTrangChuAdapter);
//
//        setDataTinCungLoai();
//        rcvTinkhac2 = findViewById(R.id.rcv_chitiet_tinkhac1);
//        newsTrangChuAdapter = new NewsTrangChuAdapter(chitiet_news.this);
//        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        rcvTinkhac2.setLayoutManager(linearLayoutManager2);
//        newsTrangChuAdapter.setDATA(newsTrangChuList2);
//        rcvTinkhac2.setAdapter(newsTrangChuAdapter);


    }

    private void getTinDangLienQuan() {

    }

    private void getUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    if(user.getId() == idUser) {
                        strImageAvtUser = user.getImage();
                        strSoDienThoai = user.getSdt();
                        addressUser = user.getAddress();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getListNews() {

        myData1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    BDSNews bdsNews = snapshot1.getValue(BDSNews.class);
                    String title = bdsNews.getTitle();
                    String address = bdsNews.getAdress();
                    String time = bdsNews.getDate();
                    String desc = bdsNews.getDescription();
                    String nguoiBan = bdsNews.getNameUser();

                    if (title_new.equals(bdsNews.getTitle())) {
                        idUser = bdsNews.getIdUser();
//                        tenDanhMuc = bdsNews.getTenDanhMuc();
                        tvTitle.setText(title);
                        String price1 = en.format(bdsNews.getPrice());
                        tvFee.setText(price1);
                        tvaddress.setText(address);
                        tvTime.setText(time);
                        tvMota.setText(desc);
                        tvNameNguoiBan.setText(nguoiBan);
                        tvSdtUser.setText(strSoDienThoai);
                        String strimage = bdsNews.getImage();
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strimage)
                                    .into(imgPic);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strImageAvtUser)
                                    .into(imgavatar);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                        try {
                            chitietdacdiemList.add(new chitietdacdiem("Diện tích:" , bdsNews.getDienTich()));
                            chitietdacdiemList.add(new chitietdacdiem("hướng : " , bdsNews.getDirection()));
                            chitietdacdiemList.add(new chitietdacdiem("Tên phân khu : " , bdsNews.getTenPhanKhu()));
                            chitietdacdiemList.add(new chitietdacdiem("Số phòng ngủ : " , bdsNews.getSoPhongNgu()));
                            chitietdacdiemList.add(new chitietdacdiem("Số phòng Wc : " , bdsNews.getSoPhongWc()));
                            chitietdacdiemList.add(new chitietdacdiem("Loại đất : " , bdsNews.getLoaiDat()));

                        }catch (Exception e) {

                        }


                    }

                    newsTrangChuList1.add(new NewsTrangChu(bdsNews.getTitle(), bdsNews.getDescription()
                            , bdsNews.getDate(), bdsNews.getPrice(), false, bdsNews.getImage(), bdsNews.getTenDanhMuc()));

                    chitietDacDiemAdapter = new chitietDacDiemAdapter(chitiet_news.this , chitietdacdiemList);
                    rcvDacdiem.setLayoutManager(new LinearLayoutManager(chitiet_news.this));
                    rcvDacdiem.setAdapter(chitietDacDiemAdapter);
                    rcvDacdiem.getRecycledViewPool().clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    giaiTriNews giaiTriNews = snapshot1.getValue(giaiTriNews.class);
                    String title = giaiTriNews.getTitle();
                    String address = giaiTriNews.getAddress();
                    String time = giaiTriNews.getDate();
                    String desc = giaiTriNews.getDescription();
                    String nguoiBan = giaiTriNews.getNameUser();
                    if(title_new.equals(giaiTriNews.getTitle())){
                        idUser = giaiTriNews.getIdUser();
                        tvTitle.setText(title);
                        String price1 = en.format(giaiTriNews.getPrice());
                        tvFee.setText(price1);
                        tvaddress.setText(address);
                        tvTime.setText(time);
                        tvMota.setText(desc);
                        tvNameNguoiBan.setText(nguoiBan);
                        tvSdtUser.setText(strSoDienThoai);
                        String strimage = giaiTriNews.getImage();
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strimage)
                                    .into(imgPic);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strImageAvtUser)
                                    .into(imgavatar);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                    }
                    chitietdacdiemList.clear();
                    newsTrangChuList1.add(new NewsTrangChu(giaiTriNews.getTitle(), giaiTriNews.getDescription()
                            , giaiTriNews.getDate(), giaiTriNews.getPrice(), false, giaiTriNews.getImage(), giaiTriNews.getTenDanhMuc()));
                    chitietDacDiemAdapter = new chitietDacDiemAdapter(chitiet_news.this , chitietdacdiemList);
                    rcvDacdiem.setLayoutManager(new LinearLayoutManager(chitiet_news.this));
                    rcvDacdiem.setAdapter(chitietDacDiemAdapter);
                    rcvDacdiem.getRecycledViewPool().clear();
          }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myData2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    thoiTrangNews thoiTrangNews = snapshot1.getValue(thoiTrangNews.class);
                    String title = thoiTrangNews.getTitle();
                    String address = thoiTrangNews.getAddress();
                    String time = thoiTrangNews.getDate();
                    String desc = thoiTrangNews.getDescription();
                    String nguoiBan = thoiTrangNews.getNameUser();
                    if (title_new.equals(thoiTrangNews.getTitle())){
                        idUser = thoiTrangNews.getIdUser();
                        tvTitle.setText(title);
                        String price1 = en.format(thoiTrangNews.getPrice());
                        tvFee.setText(price1);
                        tvaddress.setText(address);
                        tvTime.setText(time);
                        tvMota.setText(desc);
                        tvNameNguoiBan.setText(nguoiBan);
                        tvSdtUser.setText(strSoDienThoai);
                        String strimage = thoiTrangNews.getImage();
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strimage)
                                    .into(imgPic);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                        try {
                            Picasso.with(chitiet_news.this)
                                    .load(strImageAvtUser)
                                    .into(imgavatar);
                        } catch (Exception e) {
                            System.out.println("Lỗi load ảnh trong chi tiet News" + e);
                        }
                    }
                    chitietdacdiemList.clear();
                    newsTrangChuList1.add(new NewsTrangChu(thoiTrangNews.getTitle(), thoiTrangNews.getDescription()
                            , thoiTrangNews.getDate(), thoiTrangNews.getPrice(), false, thoiTrangNews.getImage(), thoiTrangNews.getTenDanhMuc()));
                    chitietDacDiemAdapter = new chitietDacDiemAdapter(chitiet_news.this , chitietdacdiemList);
                    rcvDacdiem.setLayoutManager(new LinearLayoutManager(chitiet_news.this));
                    rcvDacdiem.setAdapter(chitietDacDiemAdapter);
                    rcvDacdiem.getRecycledViewPool().clear();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog.dismiss();
    }



    // cùng data giống như News bên trang chủ
    private void setDataTinCungLoai() {

    }

    private void setDataTinCungSeller() {
    }

    private void setData() {


        tvTitle.setText("");
        tvFee.setText("");
        tvaddress.setText("");
        tvTime.setText("");

//        nếu loại sản phẩm có đặc điểm để hiển thị ở đây anh set dữ liệu giúp em xem thử ở layout nó là RCV chitiet dacdiem
        if (Loai == "") {
            tvTitleDacDiem.setText("");
        } else if (Loai == "") {
            tvTitleDacDiem.setText("");
        } else {
            layoutDacDiem.setVisibility(View.GONE);
//            nếu set hết không có là nó sẽ biến mất
        }

        tvMota.setText("");
        tvMotaLienhe.setText("");
        tvMotaLienhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnsLienHe();
            }
        });

        tvNameNguoiBan.setText("");
        tvXemTrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileSeller();
            }
        });

//      Ảnh chính
        Glide.with(this).load("").into(imgPic);
//      Avatar
        Glide.with(this).load("").into(imgavatar);
//      setfavorite
        if (false) {
            imgfavoriteheart.setImageResource(R.drawable.ic_item_trangchu_favorite);
        } else {
            imgfavoriteheart.setImageResource(R.drawable.ic_item_trangchu_unfavorite);
        }
    }

    private void profileSeller() {
//        mở đến trang người bán.
    }

    private void SnsLienHe() {
//      code mở điện thoại liên hệ.
    }

    private void unitUI() {
        Intent intent = getIntent();
        title_new = intent.getStringExtra("title");
        rcvDacdiem = findViewById(R.id.rcv_chitiet_dacdiem);
//        rcvTinkhac1 = findViewById(R.id.rcv_chitiet_tinkhac1);
//        rcvTinkhac2 = findViewById(R.id.rcv_chitiet_tinkhac1);

        imgavatar = findViewById(R.id.img_chitiet_nguoiban_avatar);
        imgfavoriteheart = findViewById(R.id.img_chitiet_favorite);
        imgPic = findViewById(R.id.img_chitiet_pic);
        icon_back = findViewById(R.id.icon_back);
        tvTitle = findViewById(R.id.tv_chitiet_title);
        tvTitleDacDiem = findViewById(R.id.tv_chitiet_title_dacdiem);
        tvTitleMota = findViewById(R.id.tv_chitiet_title_mota);
        tvTitleTinkhac1 = findViewById(R.id.tv_chitiet_tinkhac1);
        tvTitleTinkhac2 = findViewById(R.id.tv_chitiet_tinkhac2);

        tvFee = findViewById(R.id.tv_chitiet_fee);
        tvaddress = findViewById(R.id.tv_chitiet_location);
        tvaddressMap = findViewById(R.id.tv_chitiet_location_map);
        tvTime = findViewById(R.id.tv_chitiet_time);

        tvMota = findViewById(R.id.tv_chitiet_mota_thongtin);
        tvMotaLienhe = findViewById(R.id.tv_chitiet_mota_thongtinlienhe);

        tvNameNguoiBan = findViewById(R.id.tv_chitiet_nguoiban_name);
        tvXemTrang = findViewById(R.id.tv_chitiet_nguoiban_xemtrang);
        tvSdtUser = findViewById(R.id.tvSdtUser);

        layoutDacDiem = findViewById(R.id.layout_chitiet_dacdiem);
    }
}