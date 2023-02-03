package com.example.prototype1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int CAM_REQ_CODE = 100;
    Uri imageUri;
    ImageView img;
    Button cam, upload;
    String currentPhotoPath;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    FirebaseDatabase fdatabase;
    FirebaseStorage fstorage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cam = findViewById(R.id.camBtn);
        img = findViewById(R.id.preview);
        upload = findViewById(R.id.uploadBtn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   uploadImage();

            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });
    }

//    private void uploadImage() {
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference("test").child("ImageDetails");
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Uploading");
//        progressDialog.show();
//        SimpleDateFormat formatter = new SimpleDateFormat("YYYY_MM_DD_HH_MM_SS", Locale.getDefault());
//        Date now = new Date();
//        String filename = formatter.format(now);
//        storageReference = FirebaseStorage.getInstance().getReference("images/"+filename);
//        storageReference.putFile(ima).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        imageModel model = new imageModel(uri.toString());
//                        String modelId = db.push().getKey();
//                        db.child(modelId).setValue(model);
//                        }
//                });
//                if(progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                if(progressDialog.isShowing()){
//                    progressDialog.dismiss();
//                    Toast.makeText(MainActivity.this, "Failed Upload", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAM_REQ_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        String filename = "photo";
        File fileStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = File.createTempFile(filename, ".jpg", fileStorage);
            Uri imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.prototype1.fileProvider",imageFile);
            currentPhotoPath = imageFile.getAbsolutePath();
            cam.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cam, CAM_REQ_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQ_CODE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//             img.setImageBitmap(imageBitmap);

            Bitmap bm = BitmapFactory.decodeFile(currentPhotoPath);
            String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bm, "val", null);
            img.setImageBitmap(bm);
            imageUri = Uri.parse(currentPhotoPath);
        }
    }
}