package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.Users;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends AppCompatActivity {

    private TextView tvUsernameContent, tvFollowers, tvWatching, tvEditAccount
            , tvJudge, tvParticipationDate, tvLocation, tvFeedbackMessage, tvBelieve;
    private ImageView imgShare;
    private CircleImageView imgAvatar;
    private Button btnPost, btnJobApplication;
    private ImageButton btnBack;
    private ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    GoogleSignInAccount acct;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initUi();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        acct = GoogleSignIn.getLastSignedInAccount(this);

            if (currentUser != null){
                getProfileUser();
            }else if (isLoggedIn){
                getProfileUserFB();
            }else if (acct != null){
                getProfileUserGoogle();
            }

            //code click listener
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initClickListener();
                }
            },500);

    }

    //Ánh xạ
    private void initUi() {
        tvUsernameContent = findViewById(R.id.username_content_activity_account);
        tvFollowers = findViewById(R.id.tv_followers_activity_account);
        tvWatching = findViewById(R.id.tv_watching_activity_account);
        tvEditAccount = findViewById(R.id.tv_edit_account_activity_account);
        tvJudge = findViewById(R.id.tv_judge_activity_account);
        tvParticipationDate = findViewById(R.id.tv_participation_date_activity_account);
        tvLocation = findViewById(R.id.tv_location_activity_account);
        tvFeedbackMessage = findViewById(R.id.tv_feedback_message_activity_account);
        tvBelieve = findViewById(R.id.tv_believe_activity_account);
        imgAvatar = findViewById(R.id.img_avatar_activity_account);
        imgShare = findViewById(R.id.img_share_acvitity_account);
        btnPost = findViewById(R.id.btn_post_news);
        btnJobApplication = findViewById(R.id.btn_job_application);
        btnBack = findViewById(R.id.btn_back_activity_account);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        broadcast = new Broadcast();
    }

    //sự kiện click
    private void initClickListener() {
        tvEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null){
                    Intent intent = new Intent(Account.this, EditAccount.class);
                    intent.putExtra("email", currentUser.getEmail());
                    startActivity(intent);
                }else{
                    Toast.makeText(Account.this, "Đăng nhập bằng tài khoản chợ tốt để chỉnh sửa thông tin!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getProfileUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        List<Users> mListUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Users user = snapshot1.getValue(Users.class);
                    mListUser.add(user);
                }
                for (int i = 0; i < mListUser.size(); i++){
                    if (mListUser.get(i).getEmail().equals(email)){
                        tvUsernameContent.setText(checkname(mListUser.get(i).getName()));
                        String strImage = mListUser.get(i).getImage().trim();
                        if (strImage.isEmpty()){
                            return;
                        }
                        Picasso.with(Account.this)
                                .load(strImage)
                                .placeholder(R.drawable.user_icon)
                                .error(R.drawable.user_icon)
                                .into(imgAvatar);

                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String checkname(String s){
        String name = "";
        if (s.length() > 20){
            for (String s1: s.split("")){
                name+=s1;
                if (name.length() == 20){
                    break;
                }
            }
            return name+"...";
        }
       return s;
    }

    private void getProfileUserFB() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String fullName = object.getString("name");
                            String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            tvUsernameContent.setText(checkname(fullName));
                            Picasso.with(Account.this)
                                    .load(image)
                                    .placeholder(R.drawable.user_icon)
                                    .error(R.drawable.user_icon)
                                    .into(imgAvatar);
                        } catch (JSONException e) {
                            Toast.makeText(Account.this, "Lỗi tải dữ liệu tk fb: "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getProfileUserGoogle() {
        String personName = acct.getDisplayName();
        String personPhoto = String.valueOf(acct.getPhotoUrl());
        tvUsernameContent.setText(checkname(personName));
        Picasso.with(this)
                .load(personPhoto)
                .placeholder(R.drawable.user_icon)
                .error(R.drawable.user_icon)
                .into(imgAvatar);
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