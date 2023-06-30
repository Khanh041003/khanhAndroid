package com.example.duan1;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAccount extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 12;

    private TextView tvName, tvSdt, tvEmail, tvLocation, tvCCCD, tvPassword, tvSex, tvDateOfBirth
            , tvHoaDon;
    private CircleImageView imgImage;
    private RelativeLayout imgAvatar;
    private Button imgName;
    private ImageButton btnBack;
    private Bitmap bitmapselect;
    private String strImage = " ";
    private FirebaseAuth auth;
    private FirebaseUser userr;
    private ProgressDialog progressDialog;
    private List<Users> mListUser;
    private String oldPasswordUser = " ";
    private Broadcast broadcast;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode()== Activity.RESULT_OK){
                        //there are no request codes
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        try {
                            Calendar calendar = Calendar.getInstance();

                            bitmapselect = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

                            progressDialog.setTitle("Đang tải ảnh lên...!");
                            progressDialog.show();

                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            StorageReference mountainsRef = storageRef.child("AvatarUser/"+userr.getEmail()+"/image"+calendar.getTimeInMillis());
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmapselect.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] dataImage = baos.toByteArray();

                            UploadTask uploadTask = mountainsRef.putBytes(dataImage);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    progressDialog.dismiss();
                                    Toast.makeText(EditAccount.this, "Lỗi cập nhật hình ảnh!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                                    mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            strImage = String.valueOf(uri);
                                            UpdateImageUrl();
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            progressDialog.dismiss();
                                            Toast.makeText(EditAccount.this, "Lỗi tải URL hình ảnh!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }catch (IOException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        initUi();
        getDataUser();
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initClickListener();
                progressDialog.dismiss();
            }
        }, 500);
    }

    //ánh xạ
    private void initUi() {
        tvName = findViewById(R.id.tv_name_activity_editaccount);
        tvSdt = findViewById(R.id.tv_sdt_activity_editaccount);
        tvLocation = findViewById(R.id.tv_location_activity_editaccount);
        tvCCCD = findViewById(R.id.tv_cccd_activity_editaccount);
        tvPassword = findViewById(R.id.tv_password_activity_editaccount);
        tvSex = findViewById(R.id.tv_sex_activity_editaccount);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth_activity_editaccount);
        tvHoaDon = findViewById(R.id.tv_xuat_hoa_don_activity_editaccount);
        imgAvatar = findViewById(R.id.img_avatar_activity_editaccount);
        imgImage = findViewById(R.id.img_image);
        imgName = findViewById(R.id.img_name);
        btnBack = findViewById(R.id.imgbtn_back);

        auth = FirebaseAuth.getInstance();
        userr = auth.getCurrentUser();

        progressDialog = new ProgressDialog(this);

        broadcast = new Broadcast();
    }

    //sự kiện click
    private void initClickListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissionsimage();
            }
        });

        imgName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Xin chờ!...");
                progressDialog.show();
                UpdateName();
                progressDialog.dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvSdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSdt();
            }
        });

        tvCCCD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCCCD();
            }
        });

        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        final Dialog dialog = new Dialog(EditAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reset_password);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        dialog.setCancelable(false);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        //initUi
        EditText edtOldPassword = dialog.findViewById(R.id.edt_dialog_old_password);
        EditText edtNewPassword = dialog.findViewById(R.id.edt_dialog_new_password);
        Button btnUpdate = dialog.findViewById(R.id.btn_dialog_update_password);
        Button btnCancal = dialog.findViewById(R.id.btn_dialog_cancel_reset_password);

        btnCancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userr != null){
                    String strOldPassword = edtOldPassword.getText().toString().trim();
                    String strNewPassword = edtNewPassword.getText().toString().trim();
                    if (strOldPassword.isEmpty() || edtOldPassword == null){
                        edtOldPassword.setError("Không được để trống");
                        return;
                    }
                    if (strNewPassword.isEmpty() || edtNewPassword == null){
                        edtNewPassword.setError("Không được để trống");
                        return;
                    }
                    progressDialog.setTitle("Xin chờ...!");
                    progressDialog.show();
                    if (strOldPassword.equals(oldPasswordUser)){
                        userr.updatePassword(strNewPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            resetPasswordDatabase(strNewPassword);
                                            dialog.dismiss();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                    }else{
                        edtOldPassword.setError("Mật khẩu cũ không chính xác!");
                        progressDialog.dismiss();
                    }
                }

            }
        });
        dialog.show();
    }

    @Nullable
    private String checkname(@NonNull String s){
        String name1 = "";
        if (s.length() > 20){
            for (String s1: s.split("")){
                name1+=s1;
                if (name1.length() == 20){
                    break;
                }
            }
            return name1+"...";
        }

        return s;
    }

    private void getDataUser(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        mListUser = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Users user = snapshot1.getValue(Users.class);
                    mListUser.add(user);
                    if (user.getEmail().equals(email)){
                        tvName.setText(checkname(user.getName()));
                        String strImage = user.getImage().trim();
                        oldPasswordUser = user.getPassword();
                        if (strImage.isEmpty()){
                            return;
                        }
                        Picasso.with(EditAccount.this)
                                .load(strImage)
                                .placeholder(R.drawable.user_icon)
                                .error(R.drawable.user_icon)
                                .into(imgImage);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void requestPermissionsimage() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(EditAccount.this, "Đã từ chối quyền truy cập\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void UpdateImageUrl() {
        if (mListUser.size() == 0){
            return;
        }
        String email = userr.getEmail();
        int index = -1;
        for (int i = 0; i < mListUser.size(); i++){
            if (mListUser.get(i).getEmail().equals(email)){
                index = i;
            }
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(mListUser.get(index).getId()+"").child("image").setValue(strImage)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditAccount.this, "Đã xong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void UpdateName(){
        if (mListUser.size() == 0){
            finish();
        }
        String email = userr.getEmail();
        String name = tvName.getText().toString().trim();
        int index = -1;
        for (int i = 0; i < mListUser.size(); i++){
            if (mListUser.get(i).getEmail().equals(email)){
                index = i;
            }
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(mListUser.get(index).getId()+"").child("name").setValue(name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditAccount.this, "Đã xong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void resetPasswordDatabase(String newPassword){
        if (mListUser.size() == 0){
            finish();
        }
        String email = userr.getEmail();
        int index = -1;
        for (int i = 0; i < mListUser.size(); i++){
            if (mListUser.get(i).getEmail().equals(email)){
                index = i;
            }
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(mListUser.get(index).getId()+"").child("password").setValue(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditAccount.this, "Đã cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setSdt() {
        final Dialog dialog = new Dialog(EditAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_account);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        dialog.setCancelable(false);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        //anh xa
        TextView tvTitle = dialog.findViewById(R.id.tv_title_dialog);
        EditText edtSdt = dialog.findViewById(R.id.edt_dialog);
        TextInputLayout tilHint = dialog.findViewById(R.id.hint);
        Button btnOk = dialog.findViewById(R.id.btn_them_dialog);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog);

        tvTitle.setText("Cập nhật số điện thoại");
        tilHint.setHint("Nhập số điện thoại...");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sdt = edtSdt.getText().toString().trim();
                if (edtSdt == null || sdt.isEmpty()){
                    edtSdt.setError("không được để trống!");
                    return;
                }
                String email = userr.getEmail();
                int index = -1;
                for (int i = 0; i < mListUser.size(); i++){
                    if (mListUser.get(i).getEmail().equals(email)){
                        index = i;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("Users").child(mListUser.get(index).getId()+"").child("sdt").setValue(sdt)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditAccount.this, "Đã xong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setCCCD() {
        final Dialog dialog = new Dialog(EditAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_account);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        dialog.setCancelable(false);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        //anh xa
        TextView tvTitle = dialog.findViewById(R.id.tv_title_dialog);
        EditText edtCCCD = dialog.findViewById(R.id.edt_dialog);
        TextInputLayout tilHint = dialog.findViewById(R.id.hint);
        Button btnOk = dialog.findViewById(R.id.btn_them_dialog);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_dialog);

        tvTitle.setText("Cập nhật số CMND/CCCD/Hộ chiếu");
        tilHint.setHint("CMND/CCCD/Hộ chiếu...");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cccd = edtCCCD.getText().toString().trim();
                if (edtCCCD == null || cccd.isEmpty()){
                    edtCCCD.setError("không được để trống!");
                    return;
                }
                String email = userr.getEmail();
                int index = -1;
                for (int i = 0; i < mListUser.size(); i++){
                    if (mListUser.get(i).getEmail().equals(email)){
                        index = i;
                    }
                }
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("Users").child(mListUser.get(index).getId()+"").child("cccd").setValue(cccd)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditAccount.this, "Đã xong!", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.dismiss();
            }
        });

        dialog.show();
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