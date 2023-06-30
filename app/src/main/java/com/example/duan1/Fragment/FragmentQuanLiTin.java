package com.example.duan1.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;
import com.example.duan1.Adapter.historyNewsAdapter;
import com.example.duan1.MainActivity;
import com.example.duan1.R;
import com.example.duan1.model.BDSNews;
import com.example.duan1.model.Users;
import com.example.duan1.model.historyNews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentQuanLiTin extends Fragment {

    private RecyclerView rcvQuanLyTinDang;
    private historyNewsAdapter historyNewsAdapter;
    private List<historyNews> listHistoryNews;
    public static String nameUser;
    public static int idUser;
//    private String name;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    DatabaseReference myData = FirebaseDatabase.getInstance().getReference("Tin");
    MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_li_tin, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            rcvQuanLyTinDang = view.findViewById(R.id.rcvQuanLyTin);
            mainActivity = (MainActivity) getActivity();
            listHistoryNews = new ArrayList<>();

            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users");

            getUser();

            getListHistoryNews();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
            rcvQuanLyTinDang.setLayoutManager(linearLayoutManager);
            historyNewsAdapter = new historyNewsAdapter(getContext(),mainActivity, listHistoryNews);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setAnimation(R.anim.layout_animation_left_to_right);
                }
            }, 100);

        }
        return view;
    }
    private void getUser() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            return;
        }
        List<Users> mListUser = new ArrayList<>();
        String email = currentUser.getEmail();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Users user = snapshot1.getValue(Users.class);
                    mListUser.add(user);
                }

                for (int i = 0; i < mListUser.size(); i++) {
                    if (mListUser.get(i).getEmail().equals(email)) {
                        nameUser = (mListUser.get(i).getName());
                        idUser = mListUser.get(i).getId();
//                        name = nameUser;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error get Id and Name from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getListHistoryNews() {
            myData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listHistoryNews.clear();
                    for (DataSnapshot snapshot1: snapshot.getChildren()){
                        BDSNews bdsNews = snapshot1.getValue(BDSNews.class);
                        if (bdsNews.getIdUser() == idUser) {
                            listHistoryNews.add(new historyNews(bdsNews.getId(), bdsNews.getTitle(),
                                    bdsNews.getDescription() , bdsNews.getDate(),
                                    bdsNews.getTenDanhMuc(), bdsNews.getImage()));
                        }
//                        else if(name.equals("Admin")){
//                            listHistoryNews.add(new historyNews(bdsNews.getId(), bdsNews.getTitle(),
//                                    bdsNews.getDescription() , bdsNews.getDate(), bdsNews.getTenDanhMuc(),
//                                    bdsNews.getImage()));
//                        }
                    }

                    Collections.reverse(listHistoryNews);
                    historyNewsAdapter.notifyDataSetChanged();

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void setAnimation(int animResource){
        LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(
                mainActivity, animResource
        );
        rcvQuanLyTinDang.setLayoutAnimation(layoutAnimationController);
        rcvQuanLyTinDang.setAdapter(historyNewsAdapter);
    }



}