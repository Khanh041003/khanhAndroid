package com.example.duan1;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.Users;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1000;
    private ImageView imgBack;
    private CircleImageView imgLoginFacebook, imgLoginGoole, imgLoginZalo;
    private TextView tvResetPassword, tvRegister;
    private EditText edtNumberPhone, edtPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private Broadcast broadcast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();

        //login Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        //login Goolgle
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initListenerClick();
        initListenerTextChange();


    }

    private void initUi() {
        imgBack = findViewById(R.id.img_back);
        imgLoginFacebook = findViewById(R.id.img_login_facebook);
        imgLoginGoole = findViewById(R.id.img_login_google);
        imgLoginZalo = findViewById(R.id.img_login_zalo);
        tvResetPassword = findViewById(R.id.tv_reset_password);
        tvRegister = findViewById(R.id.tv_register);
        edtNumberPhone = findViewById(R.id.edt_numberphone);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        broadcast = new Broadcast();

    }

    private void initListenerClick() {
        //click back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code back
                finish();
            }
        });

        //click login facebook
        imgLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code login facebook
                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));
            }
        });

        //click login google
        imgLoginGoole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code login google
                LoginGoogle();
            }
        });

        //click login zalo
        imgLoginZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code login zalo
                Toast.makeText(Login.this, "Login zalo from Login", Toast.LENGTH_SHORT).show();
            }
        });

        //click reset password
        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code reset password
                Toast.makeText(Login.this, "reset Password from Login", Toast.LENGTH_SHORT).show();
            }
        });

        //click register
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code register
                startActivity(new Intent(Login.this, Register.class));
            }
        });


    }

    private void initListenerTextChange() {
        edtNumberPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtNumberPhone.getText().toString().trim().equals("") || edtPassword.getText().toString().trim().equals("")) {
                    btnLogin.setBackgroundResource(R.drawable.bg_button_login);
                    return;
                } else {
                    btnLogin.setBackgroundResource(R.drawable.bg_button_register);
                    OnLogin();
                }
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtNumberPhone.getText().toString().trim().equals("") || edtPassword.getText().toString().trim().equals("")) {
                    btnLogin.setBackgroundResource(R.drawable.bg_button_login);
                    return;
                } else {
                    btnLogin.setBackgroundResource(R.drawable.bg_button_register);
                    OnLogin();
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void OnLogin() {

        //click login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code login
                ProgressDialog progressDialog = new ProgressDialog(Login.this);
                String email = edtNumberPhone.getText().toString().trim();
                if (!isValidEmail(email)) {
                    edtNumberPhone.setError("Email không đúng!");
                    return;
                }
                String password = edtPassword.getText().toString().trim();
                if (password.length() < 6) {
                    edtPassword.setError("Mật khẩu phải bằng hoặc trên 6 kí tự");
                    return;
                }


                progressDialog.setTitle("Xin chờ");
                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
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
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(Login.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for (int i = 0; i < mListUser.size(); i++) {
                                                        if (mListUser.get(i).getEmail().equals(email) && mListUser.get(i).getPassword().equals(password)) {
                                                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(Login.this, MainActivity.class);
//                                                            intent.putExtra("statusLogin", 1);
                                                            setResult(RESULT_OK, intent);
                                                            finish();

                                                        }
                                                    }
                                                }
                                            },500);


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(Login.this, "Đăng nhập thất bại, tài khoản hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                                        }
                                        progressDialog.dismiss();
                                    }
                                }, 2000);
                            }
                        });
            }
        });

    }

    private void LoginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        //google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            finish();
        }

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