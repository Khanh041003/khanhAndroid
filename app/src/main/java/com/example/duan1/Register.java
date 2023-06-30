
package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {

    private ImageView imgBack;

    private TextView tvTermsAndUse, tvLogin ;
    private EditText edtNumberPhone, edtPassword, edtName;
    private Button btnRegister;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private Broadcast broadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        initListenerClick();

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Pattern.
                compile("^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\\\.[A-Za-z0-9]+)$").
                matcher(target).matches());
    }

  private void initUi() {
        imgBack = findViewById(R.id.img_back_register);
        tvLogin = findViewById(R.id.tv_login);
        tvTermsAndUse = findViewById(R.id.tv_terms_of_use);
        edtNumberPhone = findViewById(R.id.edt_numberphone_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtName = findViewById(R.id.edt_name_register);
        btnRegister = findViewById(R.id.btn_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        broadcast = new Broadcast();
    }

    private void initListenerClick(){

        //click back
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code back
                finish();
            }
        });

        //click reset password
        tvTermsAndUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code reset password
                Toast.makeText(Register.this, "Terms and use from Login", Toast.LENGTH_SHORT).show();

            }
        });

        //click login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code register
                finish();
            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edtNumberPhone.getText().toString().trim().isEmpty() ||
                        edtPassword.getText().toString().trim().isEmpty() ||
                        edtName.getText().toString().trim().isEmpty()){
                    btnRegister.setBackgroundResource(R.drawable.bg_button_login);
                    return;
                }else{
                    btnRegister.setBackgroundResource(R.drawable.bg_button_register);
                    //code register
                    OnRegister(getListData());
                }
            }
        });

        edtNumberPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (edtNumberPhone.getText().toString().trim().isEmpty() ||
                        edtPassword.getText().toString().trim().isEmpty() ||
                        edtName.getText().toString().trim().isEmpty()){
                    btnRegister.setBackgroundResource(R.drawable.bg_button_login);
                    return;
                }else{
                    btnRegister.setBackgroundResource(R.drawable.bg_button_register);
                    //code register
                    OnRegister(getListData());
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
                if (edtNumberPhone.getText().toString().trim().isEmpty() ||
                        edtPassword.getText().toString().trim().isEmpty() ||
                        edtName.getText().toString().trim().isEmpty()){
                    btnRegister.setBackgroundResource(R.drawable.bg_button_login);
                    return;
                }else{
                    btnRegister.setBackgroundResource(R.drawable.bg_button_register);
                    //code Register
                    OnRegister(getListData());
                }
            }
        });

        tvTermsAndUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code terms of use
                Toast.makeText(Register.this, "Terms of use from register", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void OnRegister(List<Users> listUsers){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        //click register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code register
                String name = edtName.getText().toString().trim();
                if (name.isEmpty() || edtName == null){
                    edtName.setError("Tên không được để trống");
                    return;
                }
                String email = edtNumberPhone.getText().toString().trim();
                if (isValidEmail(email)){
                    edtNumberPhone.setError("Email không đúng!");
                    return;
                }
                String password = edtPassword.getText().toString().trim();
                if (password.length() < 6 ){
                    edtPassword.setError("Mật khẩu phải bằng hoặc trên 6 kí tự");
                    return;
                }
                progressDialog.setTitle("Xin chờ...!");
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                        // Sign in success, update UI with the signed-in user's information

                                        int size = listUsers.size();
                                        int IDcuoi = -1;
                                        if (size != 0){
                                            IDcuoi = listUsers.get(size-1).getId();
                                        }

                                                Users users = new Users(IDcuoi+1, name, email,
                                                        " ", " ", password, " ",
                                                        " ", " ", " ");
//                                        Users users = new Users(IDcuoi+1, name, email,
//                                                " ", " ", password);

                                        myRef.child("Users/"+(IDcuoi+1)).setValue(users);
                                        Toast.makeText(Register.this, "Tạo tài khoản thành công!",
                                                Toast.LENGTH_SHORT).show();
                                        finish();
                                        progressDialog.dismiss();
                                        }
                                    },1500);

                                } else {
                                    Toast.makeText(Register.this, "Tạo tài khoản thất bại!",
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }
                        });

            }
        });
    }

    private List<Users> getListData() {
        List<Users> list  = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren() ){
                    Users users = snapshot1.getValue(Users.class);
                    list.add(users);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Register.this, "Tải thông tin tài khoản thất bại!", Toast.LENGTH_SHORT).show();
            }
        });

        return list;
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


