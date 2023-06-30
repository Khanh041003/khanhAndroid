package com.example.duan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.duan1.Adapter.dmucDienMayAdapter;
import com.example.duan1.Adapter.dmucTheThaoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.dienMay;
import com.example.duan1.model.giaiTri;

import java.util.ArrayList;
import java.util.List;

public class chonDanhMucDienMayActivity extends AppCompatActivity {
    ImageView backPage;
    private RecyclerView rcvChonDanhMuc;
    List<dienMay> listDienMay;
    dmucDienMayAdapter dmucDienMayAdapter;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_danh_muc_dien_may);
        initUi();
        clickBackPage();
        listDienMay = new ArrayList<>();
        dmucDienMayAdapter = new dmucDienMayAdapter(getApplicationContext() , getData());
        rcvChonDanhMuc.setAdapter(dmucDienMayAdapter);
        rcvChonDanhMuc.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<dienMay> getData() {
        listDienMay.add(new dienMay("Tủ lạnh"));
        listDienMay.add(new dienMay("Máy lạnh , Điều hòa"));
        listDienMay.add(new dienMay("Máy giặt"));
        return listDienMay;
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
        rcvChonDanhMuc = findViewById(R.id.rcvChonDanhMucDienMay);

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