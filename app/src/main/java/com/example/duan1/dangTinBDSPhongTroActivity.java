package com.example.duan1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Adapter.photoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.BDSNews;
import com.example.duan1.model.thoiTrangNews;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class dangTinBDSPhongTroActivity extends AppCompatActivity implements photoAdapter.CountOfImageWhenRemove {
    private TextView tvTenDanhMuc;
    private ImageView imgBackPage;
    private EditText title_post, description, address, dienTich, price;
    private LinearLayout addImageProduct;
    private Button btnDangTin, btnSuaTin;
    private photoAdapter photoAdapter;
    private RecyclerView rcvView_select_img_PhongTro;
    private ArrayList<Uri> imageUri;
    private int REQUEST_PERMISSION_CODE = 35;
    private int PICK_IMAGE = 1;
    private int update_count = 0, idUser, maxID, idEdit;
    private ProgressDialog progressDialog;
    private String strTitle, strDesc, strAddress, strDienTich, strPrice, tenDanhMuc, nameUser, date, title_Post1;
    private Double dbPrice;
    private List<BDSNews> listBDS;
    MainActivity mainActivity;
    BDSNews bdsNewsId;
    StorageReference imageFolder;
    DatabaseReference myData;
    private Bitmap bitmapselect;
    private Calendar calendar = Calendar.getInstance();
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_bdsphong_tro);
        imageFolder = FirebaseStorage.getInstance().getReference().child("Image.jpg");
        myData = FirebaseDatabase.getInstance().getReference("Tin");
        imageUri = new ArrayList<>();

        nameUser = mainActivity.name;
        idUser = mainActivity.id;
        initUi();
        clickBackPage();
        clickAddImageFashion();
        getListBds();
        clickDangTin();
        getCurrentDate();
        if (title_Post1 == null || title_Post1.equals("")) {
            btnDangTin.setVisibility(View.VISIBLE);
            btnSuaTin.setVisibility(View.INVISIBLE);
        } else {
            btnSuaTin.setVisibility(View.VISIBLE);
            btnDangTin.setVisibility(View.INVISIBLE);
            setTextInput();
            suaTin();
        }
    }

    private void suaTin() {
        btnSuaTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(dangTinBDSPhongTroActivity.this);
                progressDialog.setMessage("Please wait for save");

                strTitle = title_post.getText().toString().trim();
                strDesc = description.getText().toString().trim();
                strPrice = price.getText().toString().trim();

                strDienTich = dienTich.getText().toString().trim();
                strAddress = address.getText().toString().trim();
                try {
                    dbPrice = Double.parseDouble(strPrice);
                } catch (Exception e) {
                    Toast.makeText(dangTinBDSPhongTroActivity.this, "Lỗi chuyển đổi kiểu dữ liệu", Toast.LENGTH_SHORT).show();
                }

                if (strAddress.isEmpty() || strPrice.isEmpty() || strDesc.isEmpty() || strTitle.isEmpty() || strDienTich.isEmpty()) {
                    MainActivity.showDiaLogWarning(dangTinBDSPhongTroActivity.this, "vui lòng nhập đầy đủ thông tin");

                } else {
                    progressDialog.show();
                    String strId = String.valueOf(idEdit);
                    myData.child(strId).setValue(
                            new BDSNews(idEdit, strTitle, strDesc, dbPrice, strDienTich, strAddress,
                                    idUser, nameUser,tenDanhMuc, date
                            )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            upImage(idEdit);
                            Toast.makeText(dangTinBDSPhongTroActivity.this, "Sửa tin thành công", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void setTextInput() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Tin");
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BDSNews bdsNews = snapshot.getValue(BDSNews.class);
                listBDS.add(bdsNews);
                if(bdsNews.getId() == idEdit){
                    title_post.setText(bdsNews.getTitle());
                    description.setText(bdsNews.getDescription());
                    address.setText(bdsNews.getAdress());
                    dienTich.setText(bdsNews.getDienTich());
                    String strPrice = String.valueOf(bdsNews.getPrice());
                    price.setText(strPrice);
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCurrentDate() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        date = sdf.format(c.getTime());
    }

    private void clickBackPage() {
        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickAddImageFashion() {
        photoAdapter = new photoAdapter(imageUri, this, this);
        rcvView_select_img_PhongTro.setLayoutManager(new GridLayoutManager(dangTinBDSPhongTroActivity.this, 1));
        rcvView_select_img_PhongTro.setAdapter(photoAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
            return;
        }

        addImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(Intent.createChooser(intent, "select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();

                for (int i = 0; i < count; i++) {
                    if (imageUri.size() < 9) {
                        Uri imgUri = (data.getClipData().getItemAt(i).getUri());
                        imageUri.add(imgUri);
                    } else {
                        Toast.makeText(this, "Chỉ đăng tối đa 9 hình ảnh", Toast.LENGTH_SHORT).show();
                    }

                }
                photoAdapter.notifyDataSetChanged();
            } else {
                if (imageUri.size() < 9) {
                    Uri imgUri = data.getData();
                    imageUri.add(imgUri);
                } else {
                    Toast.makeText(this, "Chỉ đăng tối đa 9 hình ảnh", Toast.LENGTH_SHORT).show();
                }


            }
            photoAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "you haven't pick any Image", Toast.LENGTH_SHORT).show();
        }
    }


    private void clickDangTin() {
        btnDangTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(dangTinBDSPhongTroActivity.this);
                progressDialog.setMessage("Please wait for save");

                strTitle = title_post.getText().toString().trim();
                strDesc = description.getText().toString().trim();
                strPrice = price.getText().toString().trim();

                strDienTich = dienTich.getText().toString().trim();
                strAddress = address.getText().toString().trim();
                try {
                    dbPrice = Double.parseDouble(strPrice);
                } catch (Exception e) {
                    Toast.makeText(dangTinBDSPhongTroActivity.this, "Lỗi chuyển đổi kiểu dữ liệu", Toast.LENGTH_SHORT).show();
                }

                if (strAddress.isEmpty() || strPrice.isEmpty() || strDesc.isEmpty() || strTitle.isEmpty() || strDienTich.isEmpty()) {
                    MainActivity.showDiaLogWarning(dangTinBDSPhongTroActivity.this, "vui lòng nhập đầy đủ thông tin");
                } else {
                    progressDialog.show();

                    if (bdsNewsId == null) {
                        maxID = 0;
                    } else {
                        maxID = bdsNewsId.getId() + 1;
                    }



                    String strId = String.valueOf(maxID);

                    myData.child(strId).setValue(
                            new BDSNews(maxID, strTitle, strDesc, dbPrice, strDienTich, strAddress,
                                    idUser, nameUser, tenDanhMuc, date
                            )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            upImage(maxID);
                            Toast.makeText(dangTinBDSPhongTroActivity.this, "Đăng tin thành công", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });
    }

    private List<BDSNews> getListBds() {
        listBDS = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listBDS.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    BDSNews bdsNews = snapshot1.getValue(BDSNews.class);
                    listBDS.add(bdsNews);
                    bdsNewsId = bdsNews;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(dangTinBDSPhongTroActivity.this, "Load data Error", Toast.LENGTH_SHORT).show();
            }
        });

        return listBDS;

    }

    private void initUi() {
        tvTenDanhMuc = findViewById(R.id.tvTenDanhMuc);
        Intent intent = getIntent();
        title_Post1 = intent.getStringExtra("title1");
        idEdit = intent.getIntExtra("idPT", 0);
        tvTenDanhMuc.setText("Danh Mục - " + intent.getStringExtra("tenDanhMuc"));
        imgBackPage = findViewById(R.id.icon_back);
        addImageProduct = findViewById(R.id.addImageProduct);
        btnDangTin = findViewById(R.id.btnDangTinPT);
        btnSuaTin = findViewById(R.id.btnSuaTinPT);
        title_post = findViewById(R.id.title_post);
        description = findViewById(R.id.description_post);
        address = findViewById(R.id.address);
        dienTich = findViewById(R.id.dienTich);
        price = findViewById(R.id.price_tin_phongtro);
        rcvView_select_img_PhongTro = findViewById(R.id.rcvView_select_img_PhongTro);

        Intent intent1 = getIntent();
        tenDanhMuc = intent1.getStringExtra("tenDanhMuc");

        broadcast = new Broadcast();
    }

    @Override
    public void clicked(int getSize) {

    }

    private void upImage(int id) {
        if (imageUri.size() == 0){
            return;
        }
        Uri uri = imageUri.get(0);
        try {
            bitmapselect = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainsRef = storageRef.child(tenDanhMuc+"/image"+calendar.getTimeInMillis());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapselect.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] dataImage = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(dataImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(dangTinBDSPhongTroActivity.this, "Lỗi cập nhật hình ảnh!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                    mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String strImage = String.valueOf(uri);
                            UpdateImageUrl(strImage, id);
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            progressDialog.dismiss();
                            Toast.makeText(dangTinBDSPhongTroActivity.this, "Lỗi tải URL hình ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (IOException e) {
            Toast.makeText(mainActivity, "Lỗi try-catch", Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateImageUrl(String url, int id) {
        myData.child(id+"/image").setValue(url);
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