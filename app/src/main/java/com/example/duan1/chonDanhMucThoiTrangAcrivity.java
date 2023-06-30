package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.duan1.Adapter.dmucThoiTrangAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.thoiTrang;

import java.util.ArrayList;
import java.util.List;

public class chonDanhMucThoiTrangAcrivity extends AppCompatActivity {
    ImageView backPage;
    RecyclerView rcvChonDanhMuc;
    List<thoiTrang> listTT;
    dmucThoiTrangAdapter dmucThoiTrangAdapter;
    public static int IdUser = 0;
    public static String nameUser = "";
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_danh_muc_thoi_trang_acrivity);
        initUi();
        clickBackPage();
        getNameUser();
        getIdUser();
        listTT = new ArrayList<>();
        dmucThoiTrangAdapter = new dmucThoiTrangAdapter(this , getList());

        rcvChonDanhMuc.setAdapter(dmucThoiTrangAdapter);
        rcvChonDanhMuc.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<thoiTrang> getList() {
        listTT.add(new thoiTrang("Quần áo"));
        listTT.add(new thoiTrang("Đồng hồ"));
        listTT.add(new thoiTrang("Giày dép"));
        listTT.add(new thoiTrang("Túi xách"));
        listTT.add(new thoiTrang("Nước hoa"));
        listTT.add(new thoiTrang("Phụ kiện khác"));
        return listTT;
    }

    private void clickBackPage() {
        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initUi() {
        backPage = findViewById(R.id.icon_back);
        rcvChonDanhMuc = findViewById(R.id.rcvChonDanhMucThoiTrang);

        broadcast = new Broadcast();
    }
    public int getIdUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("Id");
        return IdUser = id;
    }
    public String getNameUser() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("Name");
        return nameUser =  name;
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcast, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcast);
        super.onStop();
    }




}