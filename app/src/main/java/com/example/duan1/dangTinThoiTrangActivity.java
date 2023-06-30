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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Adapter.photoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.product_type;
import com.example.duan1.model.thoiTrangNews;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
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


public class dangTinThoiTrangActivity extends AppCompatActivity implements photoAdapter.CountOfImageWhenRemove {
    private TextView tvTenDanhMuc;
    private ImageView imgBackPage, addImageProduct;
    private Spinner spn_product_type;
    private EditText edtTitlePost, edtDescription, edtPrice, edtAddress;
    private Button btnDangTin ,btnSuaTin ;
    private com.example.duan1.Adapter.photoAdapter photoAdapter;
    private RecyclerView rcvView_select_img_fashion;
    StorageReference imageFolder;
    private String strTitlePost, strDescription, strPrice, strLoaiSanPham, strAddress, nameUser
            , tenDanhMuc , title_Post , date , title_Post1;
    private int idUser;
    private double dbPrice;
    private List<thoiTrangNews> listFashion;
    thoiTrangNews newsFashion;
    FirebaseApp firebaseApp;
    FirebaseAppCheck firebaseAppCheck;
    private Bitmap bitmapselect;
    private int REQUEST_PERMISSION_CODE = 35;
    private int PICK_IMAGE = 1;
    private int update_count = 0, maxID, idEdit;
    private ArrayList<Uri> imageUri = new ArrayList<>();
    private ProgressDialog progressDialog;
    DatabaseReference myData;
    MainActivity mainActivity;
    private Calendar calendar = Calendar.getInstance();
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_tin_thoi_trang);
        imageFolder = FirebaseStorage.getInstance().getReference().child("Image").child("images" + System.currentTimeMillis() +"jpg");
        myData = FirebaseDatabase.getInstance().getReference("Tin");

        nameUser = mainActivity.name;
        idUser = mainActivity.id;

        initUi();
        clickBAckPage();
        eventClickSPN();
        clickAddImageFashion();
        getListFashion();
        clickDangTin();
        getCurrentDate();
        if(title_Post1 == null || title_Post1.equals("")){
            btnDangTin.setVisibility(View.VISIBLE);
            btnSuaTin.setVisibility(View.INVISIBLE);
        }else {
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
                firebaseApp.initializeApp(dangTinThoiTrangActivity.this);
                firebaseAppCheck = FirebaseAppCheck.getInstance();
                firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());

                progressDialog = new ProgressDialog(dangTinThoiTrangActivity.this);
                progressDialog.setMessage("Please wait for save");
                strTitlePost = edtTitlePost.getText().toString().trim();
                strDescription = edtDescription.getText().toString();
                strPrice = edtPrice.getText().toString();
                try {
                    dbPrice = Double.parseDouble(strPrice);
                } catch (Exception e) {
                    System.err.println("Lỗi Parse kiểu dữ liệu");
                }
                strLoaiSanPham = spn_product_type.getSelectedItem().toString();
                strAddress = edtAddress.getText().toString();

                if (strTitlePost.isEmpty() || strDescription.isEmpty() || strPrice.isEmpty()
                        || strAddress.isEmpty()) {
                    MainActivity.showDiaLogWarning(dangTinThoiTrangActivity.this, "vui lòng nhập đầy đủ thông tin");
                } else {

                progressDialog.show();

                String strId = String.valueOf(idEdit);
                myData.child(strId ).setValue(new thoiTrangNews(idEdit ,
                        strTitlePost,
                        strDescription,
                        strLoaiSanPham,
                        dbPrice,
                        strAddress ,
                        idUser,
                        nameUser,tenDanhMuc,date)) .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        upImage(idEdit);
                        Toast.makeText(dangTinThoiTrangActivity.this, "Sửa tin thành công", Toast.LENGTH_SHORT).show();
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
                thoiTrangNews thoiTrangNews = snapshot.getValue(thoiTrangNews.class);
                listFashion.add(thoiTrangNews);
                if(thoiTrangNews.getId() == idEdit) {
                    edtTitlePost.setText(thoiTrangNews.getTitle());
                    edtAddress.setText(thoiTrangNews.getAddress());
                    edtDescription.setText(thoiTrangNews.getDescription());
                    String price = String.valueOf(thoiTrangNews.getPrice());
                    edtPrice.setText(price);

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


    private void clickDangTin() {
        btnDangTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseApp.initializeApp(dangTinThoiTrangActivity.this);
                firebaseAppCheck = FirebaseAppCheck.getInstance();
                firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());

                progressDialog = new ProgressDialog(dangTinThoiTrangActivity.this);
                progressDialog.setMessage("Please wait for save");
                strTitlePost = edtTitlePost.getText().toString().trim();
                strDescription = edtDescription.getText().toString();
                strPrice = edtPrice.getText().toString();
                try {
                    dbPrice = Double.parseDouble(strPrice);
                } catch (Exception e) {
                    System.err.println("Lỗi Parse kiểu dữ liệu");
                }

                strLoaiSanPham = spn_product_type.getSelectedItem().toString();
                strAddress = edtAddress.getText().toString();

                if (strTitlePost.isEmpty() || strDescription.isEmpty() || strPrice.isEmpty()
                        || strAddress.isEmpty()) {
                    MainActivity.showDiaLogWarning(dangTinThoiTrangActivity.this, "vui lòng nhập đầy đủ thông tin");

                } else {
                    progressDialog.show();

                    }
                if(newsFashion == null) {
                    maxID = 0;
                }else {
                    maxID = newsFashion.getId() + 1;
                }
                    String strId = String.valueOf(maxID);
                    myData.child(strId ).setValue(new thoiTrangNews(maxID ,
                                    strTitlePost,
                                    strDescription,
                                    strLoaiSanPham,
                                    dbPrice,
                                    strAddress ,
                                    idUser,
                                    nameUser,tenDanhMuc, date)) .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    upImage(maxID);
                                    Toast.makeText(dangTinThoiTrangActivity.this, "Đăng tin thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
            }
        });

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
                    Toast.makeText(dangTinThoiTrangActivity.this, "Lỗi cập nhật hình ảnh!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(dangTinThoiTrangActivity.this, "Lỗi tải URL hình ảnh!", Toast.LENGTH_SHORT).show();
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

    private List<thoiTrangNews> getListFashion() {
         listFashion = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listFashion.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren() ){
                    thoiTrangNews thoiTrangNews = snapshot1.getValue(thoiTrangNews.class);
                    listFashion.add(thoiTrangNews);
                    newsFashion = thoiTrangNews;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(dangTinThoiTrangActivity.this, "Load data Error", Toast.LENGTH_SHORT).show();
            }
        });

        return listFashion;

    }

//      @Override
//      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//       Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//      while (!urlTask.isSuccessful());
//      Uri downloadUrl = urlTask.getResult();
//       FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUrl.toString());
//      mDatabaseReference.push().setValue(friendlyMessage); }

    private void clickAddImageFashion() {
        photoAdapter = new photoAdapter(imageUri, this, this);
        rcvView_select_img_fashion.setLayoutManager(new GridLayoutManager(dangTinThoiTrangActivity.this, 1));
        rcvView_select_img_fashion.setAdapter(photoAdapter);

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

    private void eventClickSPN() {
        product_type[] product_type = dangTinThoiTrangActivity.productType.getProductType();
        ArrayAdapter<product_type> adapter = new ArrayAdapter<product_type>(this,
                android.R.layout.simple_spinner_item, product_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_product_type.setAdapter(adapter);

        spn_product_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(dangTinThoiTrangActivity.this, "Loại sản phẩm " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void clickBAckPage() {
        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initUi() {
        tvTenDanhMuc = findViewById(R.id.tvTenDanhMucTT);
        Intent intent = getIntent();
        title_Post1 = intent.getStringExtra("title1");
        idEdit = intent.getIntExtra("idTT", 0);
        tvTenDanhMuc.setText("Danh Mục - " + intent.getStringExtra("tenDanhMuc"));
        imgBackPage = findViewById(R.id.icon_backTT);
        spn_product_type = findViewById(R.id.spn_product_type);
        edtTitlePost = findViewById(R.id.title_postTT);
        edtDescription = findViewById(R.id.description_postTT);
        edtPrice = findViewById(R.id.priceTT);
        edtAddress = findViewById(R.id.addressTT);
        btnDangTin = findViewById(R.id.btnDangTinTT);
        rcvView_select_img_fashion = findViewById(R.id.rcvView_select_img_fashion);
        addImageProduct = findViewById(R.id.addImageProductTT);
        btnSuaTin = findViewById(R.id.btnSuaTin);

        imageUri = new ArrayList<>();
        Intent intent1 = getIntent();
        tenDanhMuc = intent1.getStringExtra("tenDanhMuc");
        title_Post = intent1.getStringExtra("title");

        broadcast = new Broadcast();

    }

    @Override
    public void clicked(int getSize) {

    }

    public static class productType {
        public static product_type[] getProductType() {
            product_type prd1 = new product_type("Nam");
            product_type prd2 = new product_type("Nữ");
            product_type prd3 = new product_type("Cả hai");
            return new product_type[]{prd1, prd2, prd3};


        }
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