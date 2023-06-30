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
import com.example.duan1.Adapter.dmucTheThaoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.dienTu;
import com.example.duan1.model.giaiTri;

import java.util.ArrayList;
import java.util.List;

public class chonDanhMucGiaiTriActivity extends AppCompatActivity {
    ImageView backPage;
    private RecyclerView rcvChonDanhMuc;
    List<giaiTri> listGiaTri;
    dmucTheThaoAdapter dmucTheThaoAdapter;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_danh_muc_giai_tri);
        initUi();
        clickBackPage();

        listGiaTri = new ArrayList<>();
        dmucTheThaoAdapter = new dmucTheThaoAdapter(this , getListData());
        rcvChonDanhMuc.setAdapter(dmucTheThaoAdapter);
        rcvChonDanhMuc.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<giaiTri> getListData() {
      listGiaTri.add(new giaiTri("Nhạc cụ"));
      listGiaTri.add(new giaiTri("Sách"));
      listGiaTri.add(new giaiTri("Đồ thể thao, Dã ngoại"));
      listGiaTri.add(new giaiTri("Đồ sưu tầm  ,Đồ cổ"));
      listGiaTri.add(new giaiTri("Thiết bị chơi game"));
      listGiaTri.add(new giaiTri("Sở thích khác"));

        return listGiaTri;
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
        rcvChonDanhMuc = findViewById(R.id.rcvChonDanhMucGiaiTri);

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