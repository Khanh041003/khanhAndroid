package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.duan1.Adapter.dmucDoDienTuAdapter;
import com.example.duan1.Adapter.dmucThoiTrangAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.dienTu;
import com.example.duan1.model.thoiTrang;

import java.util.ArrayList;
import java.util.List;

public class chonDanhMucDoDienTuActivity extends AppCompatActivity {
    ImageView backPage;
    RecyclerView rcvChonDanhMuc;
    List<dienTu> listDienTu;
    dmucDoDienTuAdapter dmucDoDienTuAdapter;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_danh_muc_do_dien_tu);
        initui();
        clickBackPage();
        listDienTu = new ArrayList<>();
        dmucDoDienTuAdapter = new dmucDoDienTuAdapter(getApplicationContext() , getList());

        rcvChonDanhMuc.setAdapter(dmucDoDienTuAdapter);
        rcvChonDanhMuc.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<dienTu> getList() {
        listDienTu.add(new dienTu("Điện thoại"));
        listDienTu.add(new dienTu("Máy tính bảng"));
        listDienTu.add(new dienTu("Laptop"));
        listDienTu.add(new dienTu("Máy tính bàn"));
        listDienTu.add(new dienTu("Máy ảnh , Máy quay"));
        listDienTu.add(new dienTu("Tivi, Âm thanh"));
        listDienTu.add(new dienTu("Thiết bị đeo thông minh"));
        listDienTu.add(new dienTu("Phụ kiện , Màn hình, chuột"));
        return listDienTu;
    }

    private void clickBackPage() {
        backPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initui() {
        backPage = findViewById(R.id.icon_back);
        rcvChonDanhMuc = findViewById(R.id.rcvChonDanhMucDienTu);

        broadcast = new Broadcast();
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