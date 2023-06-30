package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.duan1.Adapter.dmucXeCoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.danhMucBatDongSan;
import com.example.duan1.model.xeCo;

import java.util.ArrayList;
import java.util.List;

public class chonDanhMucXeCoActivity extends AppCompatActivity {
    ImageView backPage;
    RecyclerView rcvChonDanhMucXe;
    List<xeCo> listXe;
    dmucXeCoAdapter dmucXeCoAdapter ;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_danh_muc_xe_co);
        initUi();
        clickBackPage();
        listXe = new ArrayList<>();
        dmucXeCoAdapter = new dmucXeCoAdapter(getApplicationContext() , listXeCo());
        rcvChonDanhMucXe.setAdapter(dmucXeCoAdapter);
        rcvChonDanhMucXe.setLayoutManager(new LinearLayoutManager(this));
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
        rcvChonDanhMucXe = findViewById(R.id.rcvChonDanhMucXe);

        broadcast = new Broadcast();
    }
    private List<xeCo> listXeCo() {
        listXe.add(new xeCo("Oto"));
        listXe.add(new xeCo("Xe tải ,Xe ben "));
        listXe.add(new xeCo("Xe điện"));
        listXe.add(new xeCo("Xe máy"));
        listXe.add(new xeCo("Xe đạp"));
        listXe.add(new xeCo("Phương tiện khác"));
        listXe.add(new xeCo("Phụ tùng"));
        return listXe;
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