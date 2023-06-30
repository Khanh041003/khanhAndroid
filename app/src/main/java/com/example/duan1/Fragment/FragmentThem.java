package com.example.duan1.Fragment;

import static android.app.Activity.RESULT_OK;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.duan1.Account;
import com.example.duan1.Login;
import com.example.duan1.MainActivity;
import com.example.duan1.R;
import com.example.duan1.model.Users;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
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
import java.util.ArrayList;
import java.util.List;

public class FragmentThem extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imgLoginRegister;
    private TextView tvEmail;

    private MainActivity mainActivity;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private NavigationView nav;
    private ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    GoogleSignInAccount acct;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Restart();
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them, container, false);
        mainActivity = (MainActivity) getActivity();
        initUi(view);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(mainActivity, gso);
        acct = GoogleSignIn.getLastSignedInAccount(mainActivity);

        if (isLoggedIn || currentUser != null || acct != null) {
            progressDialog.setTitle("Đang tải dữ liệu");
            progressDialog.show();
            getDataUser(isLoggedIn);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initListenerClick();
                    progressDialog.dismiss();
                }
            }, 500);
            nav.setItemIconTintList(null);
            nav.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        } else {
            getDataUser(isLoggedIn);
            initListenerClick();
            nav.setItemIconTintList(null);
            nav.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        }


        return view;
    }

    private String checkname(String s) {

        if (s.isEmpty()) {
            return null;
        }
        String name = "";
        if (s.length() > 25) {
            for (String s1 : s.split("")) {
                name += s1;
                if (name.length() == 25) {
                    break;
                }
            }
            ;
            return name + "...";
        }

        return s;
    }

    private void initUi(View view) {
        imgLoginRegister = view.findViewById(R.id.img_login_register);
        tvEmail = view.findViewById(R.id.tvEmail);
        nav = view.findViewById(R.id.nav_them);

        progressDialog = new ProgressDialog(mainActivity);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    private void initListenerClick() {
        imgLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check đã đăng nhập hay chưa
                currentUser = mAuth.getCurrentUser();
                // Check if user is signed in (non-null) and update UI accordingly.
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (currentUser != null || isLoggedIn || acct != null) {
                    startActivity(new Intent(mainActivity, Account.class));
                } else {
                    mActivityResultLauncher.launch(new Intent(mainActivity, Login.class));
                }

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            progressDialog.setTitle("Đang đăng xuất...");
            progressDialog.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    AccessToken.setCurrentAccessToken(null);
                    mGoogleSignInClient.signOut();
                    Toast.makeText(mainActivity, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    Restart();

                }
            }, 2000);
        }
//        else if (id == R.id.listUser) {
//            String name = tvEmail.getText().toString();
//
//            if (name.equals("Admin")) {
//                Intent intent = new Intent(getContext(), listUserActivity.class);
//                intent.putExtra("name" , name);
//                startActivity(intent);
//            }else {
//               MainActivity.showDiaLogWarning(getContext() , "Chức năng chỉ dành cho Admin");
//            }
//
//        }

        return true;
    }

    private void Restart() {
        FragmentManager fragmentManager;
        fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
        fragmentTransaction1.replace(R.id.container, new FragmentThem());
        fragmentTransaction1.detach(FragmentThem.this);
        fragmentTransaction1.commit();
    }

    private void updateUI() {

        String email = currentUser.getEmail();
        List<Users> mListUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
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
                        if (mListUser.get(i).getName() == null) {

                        }
                        tvEmail.setText(checkname(mListUser.get(i).getName()));
                        String strImage = mListUser.get(i).getImage().trim();
                        if (strImage.isEmpty()) {
                            return;
                        }
                        Picasso.with(mainActivity)
                                .load(strImage)
                                .placeholder(R.drawable.user_icon)
                                .error(R.drawable.user_icon)
                                .into(imgLoginRegister);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
                            tvEmail.setText(checkname(fullName));
                            Picasso.with(mainActivity)
                                    .load(image)
                                    .placeholder(R.drawable.user_icon)
                                    .error(R.drawable.user_icon)
                                    .into(imgLoginRegister);

                        } catch (JSONException e) {
                            Toast.makeText(mainActivity, "Lỗi login fb", Toast.LENGTH_SHORT).show();
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
        String personEmail = acct.getEmail();
        String personPhoto = String.valueOf(acct.getPhotoUrl());
        if ((personName + " (" + personEmail + ")").length() > 30) {
            tvEmail.setText(checkname(personName + " (" + personEmail + ")"));
        } else {
            tvEmail.setText(personName + " (" + personEmail + ")");
        }

        Picasso.with(mainActivity)
                .load(personPhoto)
                .placeholder(R.drawable.user_icon)
                .error(R.drawable.user_icon)
                .into(imgLoginRegister);

    }

    private void getDataUser(boolean isLoggedIn) {
        //facebook
        if (isLoggedIn) {
            getProfileUserFB();
        }

        //firebase
        if (currentUser != null) {
            updateUI();
        }

        //google
        if (acct != null) {
            getProfileUserGoogle();
        }
    }
}