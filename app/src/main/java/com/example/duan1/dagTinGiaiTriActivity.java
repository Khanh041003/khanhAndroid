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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan1.Adapter.photoAdapter;
import com.example.duan1.Broadcast.Broadcast;
import com.example.duan1.model.BDSNews;
import com.example.duan1.model.direction;
import com.example.duan1.model.giaiTriNews;
import com.example.duan1.model.product_type;
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

public class dagTinGiaiTriActivity extends AppCompatActivity implements com.example.duan1.Adapter.photoAdapter.CountOfImageWhenRemove {
    private TextView tvTenDanhMuc;
    private ImageView imgBackPage;
    private Spinner spn_product_type;
    private LinearLayout layout_spnTypeProduct, addImageProduct;
    private EditText edtTitlePost, edtDescription, edtPrice, edtAddress ;
    private Button btnDangTin ,btnSuaTinGT;

    private String date , strTitlePost , strDescription , strPrice , strAddress ,strLoaiSanPham , nameUser ,tenDanhMuc ,title_Post = null;
    private double dbPrice;
    private com.example.duan1.Adapter.photoAdapter photoAdapter;
    private RecyclerView rcvView_select_img_GiaiTri;
    private ArrayList<Uri> imageUri;
    private int REQUEST_PERMISSION_CODE = 35;
    private int PICK_IMAGE = 1;
    private int maxID  , idUser , idEdit;
    private ProgressDialog progressDialog;
    private List<giaiTriNews> listGiaiTri;
    MainActivity mainActivity;
    giaiTriNews giaiTriNewsId;
    private DatabaseReference myData;
    private StorageReference imageFolder;
    private Bitmap bitmapselect;
    private Calendar calendar = Calendar.getInstance();
    private Broadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dag_tin_giai_tri);

        imageFolder = FirebaseStorage.getInstance().getReference().child("Image.jpg");
        myData = FirebaseDatabase.getInstance().getReference("Tin");
        imageUri = new ArrayList<>();

        idUser = mainActivity.id;
        nameUser = mainActivity.name;

        Intent intentID = getIntent();
        idEdit = intentID.getIntExtra("idGT", 0);

        initUi();
        clickBackPage();
        eventClickSPN();
        clickAddImageFashion();
        getListGiaiTri();
        clickDangTin();
        getCurrentDate();
        if(title_Post == null || title_Post.equals("")) {
            btnDangTin.setVisibility(View.VISIBLE);
           btnSuaTinGT.setVisibility(View.INVISIBLE);
        }else {
            btnSuaTinGT.setVisibility(View.VISIBLE);
            btnDangTin.setVisibility(View.INVISIBLE);
            setTextInput();
            btnSuaTinGT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editNews();
                }
            });
        }
    }

    private void getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        date = sdf.format(c.getTime());
    }

    private void editNews() {
        btnSuaTinGT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(dagTinGiaiTriActivity.this);
                progressDialog.setMessage("Please wait for save");

                strTitlePost = edtTitlePost.getText().toString().trim();
                strDescription = edtDescription.getText().toString();
                strLoaiSanPham = spn_product_type.getSelectedItem().toString().trim();
                strAddress = edtAddress.getText().toString().trim();
                strPrice = edtPrice.getText().toString();

                try {
                    dbPrice = Double.parseDouble(strPrice);

                } catch(Exception e) {
                    System.err.println("Lỗi Parse kiểu dữ liệu");
                }

                if (strTitlePost.isEmpty() || strDescription.isEmpty() || strPrice.isEmpty()
                        || strAddress.isEmpty() ||  strLoaiSanPham.isEmpty()) {
                    MainActivity.showDiaLogWarning(dagTinGiaiTriActivity.this, "vui lòng nhập đầy đủ thông tin");

                } else {
                    progressDialog.show();

                    String strId = String.valueOf(idEdit);
                    myData.child(strId ).setValue(new giaiTriNews(idEdit ,strTitlePost , strDescription ,strAddress, dbPrice
                                    ,strLoaiSanPham ,idUser ,nameUser , tenDanhMuc, "GiaiTri", date, " "))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    upImage(idEdit);
                                    Toast.makeText(dagTinGiaiTriActivity.this, "Sửa tin thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                }
            }
        });
    }

    private void setTextInput() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tin");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                giaiTriNews giaiTriNews = snapshot.getValue(giaiTriNews.class);
                if(giaiTriNews.getId() == idEdit) {
                    edtTitlePost.setText(giaiTriNews.getTitle());
                    edtDescription.setText(giaiTriNews.getDescription());
                    String price = String.valueOf(giaiTriNews.getPrice());
                    edtPrice.setText(price);
                    edtAddress.setText(giaiTriNews.getAddress());
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

    private void clickBackPage() {
        imgBackPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void eventClickSPN() {
        product_type[] product_type = dagTinGiaiTriActivity.productType.getProductType();
        ArrayAdapter<product_type> adapter = new ArrayAdapter<product_type>(this, android.R.layout.simple_spinner_item, product_type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_product_type.setAdapter(adapter);

        spn_product_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(dagTinGiaiTriActivity.this, "Loại sản phẩm " + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void clickAddImageFashion() {
        photoAdapter = new photoAdapter(imageUri, this, this);
        rcvView_select_img_GiaiTri.setLayoutManager(new GridLayoutManager(dagTinGiaiTriActivity.this, 1));
        rcvView_select_img_GiaiTri.setAdapter(photoAdapter);

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
                progressDialog = new ProgressDialog(dagTinGiaiTriActivity.this);
                progressDialog.setMessage("Please wait for save");

                strTitlePost = edtTitlePost.getText().toString().trim();
                strDescription = edtDescription.getText().toString();
                strLoaiSanPham = spn_product_type.getSelectedItem().toString().trim();
                strAddress = edtAddress.getText().toString().trim();
                strPrice = edtPrice.getText().toString();

                try {
                    dbPrice = Double.parseDouble(strPrice);

                } catch(Exception e) {
                    System.err.println("Lỗi Parse kiểu dữ liệu");
                }

                if (strTitlePost.isEmpty() || strDescription.isEmpty() || strPrice.isEmpty()
                        || strAddress.isEmpty() ||  strLoaiSanPham.isEmpty()) {
                    MainActivity.showDiaLogWarning(dagTinGiaiTriActivity.this, "vui lòng nhập đầy đủ thông tin");

                } else {
                    progressDialog.show();

                    if(giaiTriNewsId == null) {
                        maxID = 0;
                    }else {
                        maxID = giaiTriNewsId.getId() +1;
                    }


                    String strId = String.valueOf(maxID);

                    myData.child(strId ).setValue(new giaiTriNews(maxID ,strTitlePost , strDescription ,strAddress, dbPrice
                                      ,strLoaiSanPham ,idUser ,nameUser , tenDanhMuc, "GiaiTri",date, " "))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    upImage(maxID);
                                    Toast.makeText(dagTinGiaiTriActivity.this, "Đăng tin thành công", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                }
            }
        });

    }

    private List<giaiTriNews> getListGiaiTri() {
        listGiaiTri = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Tin");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGiaiTri.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren() ){
                    giaiTriNews giaiTriNews = snapshot1.getValue(giaiTriNews.class);
                    listGiaiTri.add(giaiTriNews);
                    giaiTriNewsId = giaiTriNews;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(dagTinGiaiTriActivity.this, "Load data Error", Toast.LENGTH_SHORT).show();
            }
        });

        return listGiaiTri;

    }

    private void initUi() {
        tvTenDanhMuc = findViewById(R.id.tvTenDanhMuc);
        Intent intent = getIntent();
        title_Post = intent.getStringExtra("title");
        tvTenDanhMuc.setText("Danh Mục - " + intent.getStringExtra("tenDanhMuc"));

        imgBackPage = findViewById(R.id.icon_back);
        spn_product_type = findViewById(R.id.spn_product_type);
        layout_spnTypeProduct = findViewById(R.id.layout_spnTypeProduct);

        edtTitlePost = findViewById(R.id.title_post);
        edtDescription = findViewById(R.id.description_post);
        edtPrice = findViewById(R.id.price);
        edtAddress = findViewById(R.id.address);
        btnDangTin = findViewById(R.id.btnSubmit);
        addImageProduct = findViewById(R.id.addImageProduct);
        btnSuaTinGT = findViewById(R.id.btnSuaTinGT);
        rcvView_select_img_GiaiTri = findViewById(R.id.rcvView_select_img_GiaiTri);

        Intent intent1 = getIntent();
       tenDanhMuc = intent1.getStringExtra("tenDanhMuc");

       broadcast = new Broadcast();

    }

    @Override
    public void clicked(int getSize) {

    }

    public static class productType {
        public static product_type[] getProductType() {
            product_type prd1 = new product_type("Thạch,đá quí");
            product_type prd2 = new product_type("Tranh ảnh,khung tranh");
            product_type prd3 = new product_type("Bật lửa");
            product_type prd4 = new product_type("Đồ gốm sứ");
            product_type prd5 = new product_type("Tường trang trí");
            return new product_type[]{prd1, prd2, prd3, prd4, prd5};


        }
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
                    Toast.makeText(dagTinGiaiTriActivity.this, "Lỗi cập nhật hình ảnh!", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                    mountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String strImage = String.valueOf(uri);
                            myData.child(id+"/image").setValue(strImage);
                            progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            progressDialog.dismiss();
                            Toast.makeText(dagTinGiaiTriActivity.this, "Lỗi tải URL hình ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (IOException e) {
            Toast.makeText(mainActivity, "Lỗi try-catch", Toast.LENGTH_SHORT).show();
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