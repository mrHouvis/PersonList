package com.example.personlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class EditRecordActivity extends AppCompatActivity {

    private ImageView pImageView;
    private EditText pNameEt, pAgeEt, pPhoneEt;
    Button saveInfoBt;
    ActionBar actionBar;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private byte[] image;

    private String id, name, age, phone, addTimeStamp, updateTimeStamp;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pImageView = findViewById(R.id.personImage);
        pNameEt = findViewById(R.id.personName);
        pAgeEt = findViewById(R.id.personAge);
        pPhoneEt = findViewById(R.id.personPhone);

        saveInfoBt = findViewById(R.id.addButton);

        Intent intent = getIntent();
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        age = intent.getStringExtra("AGE");
        phone = intent.getStringExtra("PHONE");
        image = intent.getByteArrayExtra("IMAGE");
        addTimeStamp = intent.getStringExtra("ADD_TIMESTAMP");
        updateTimeStamp = intent.getStringExtra("UPDATE_TIMESTAMP");

        actionBar.setTitle("Update Information");

        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        age = intent.getStringExtra("AGE");
        phone = intent.getStringExtra("PHONE");
        image = intent.getByteArrayExtra("IMAGE");
        addTimeStamp = intent.getStringExtra("ADD_TIMESTAMP");
        updateTimeStamp = intent.getStringExtra("UPDATE_TIMESTAMP");

        pNameEt.setText(name);
        pAgeEt.setText(age);
        pPhoneEt.setText(phone);

        if (image.equals("null")) {
            pImageView.setImageResource(R.drawable.ic_action_addphoto);
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
            pImageView.setImageBitmap(bmp);
        }

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //initiate database object in main function
        dbHelper = new DatabaseHelper(this);

        pImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePickDialog();

            }
        });

        saveInfoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //insert the data to db
                getData();
                startActivity(new Intent(EditRecordActivity.this, MainActivity.class));
                Toast.makeText(EditRecordActivity.this, "Update Succesfully!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getData() {

        name = "" + pNameEt.getText().toString().trim();
        age = "" + pAgeEt.getText().toString().trim();
        phone = "" + pPhoneEt.getText().toString().trim();

        String newUpdateTime = "" + System.currentTimeMillis();

        dbHelper.updateInfo(
                "" + id,
                "" + name,
                "" + age,
                "" + phone,
                image,
                "" + addTimeStamp,
                "" + newUpdateTime
        );

    }

    private void imagePickDialog() {

        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select for Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    //if 0 then open the camera and also check the permission of camera
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromStorage();
                    }
                }
            }
        });

        builder.create().show();

    }

    //get image from gallery
    private void pickFromStorage() {

        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    //get image from camera
    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera permission required!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickFromStorage();
                    } else {
                        Toast.makeText(this, "Storage permission required!", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                try {

                    final Uri imageUri = data.getData();
                    final InputStream imageStream;
                    imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    pImageView.setImageBitmap(selectedImage);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    image = stream.toByteArray();
                    stream.close();

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                Bundle extras = data.getExtras();
                if (extras != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    image = stream.toByteArray();
                    pImageView.setImageBitmap(bitmap);

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return super.onSupportNavigateUp();

    }
}

