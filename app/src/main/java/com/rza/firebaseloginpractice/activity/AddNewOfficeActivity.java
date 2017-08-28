package com.rza.firebaseloginpractice.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.model.Office;
import com.rza.firebaseloginpractice.storage.OnImageUploadedListener;
import com.rza.firebaseloginpractice.storage.PhotoStorage;

public class AddNewOfficeActivity extends AppCompatActivity {
    private EditText etOfficeName;
    private ImageButton btnLocation;
    private ImageView officeImage;
    private FloatingActionButton btnAdd;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int MY_PERMISSION_INTERNAL_STORAGE = 2;
    private static int RESULT_MAPS_ACTIVITY = 3;
    private static int REQUEST_LOCATION = 4;
    private Toast toast;
    private Office office;
    private TextView tvLat;
    private TextView tvLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_office);
        afterViews();
        office = new Office();
    }


    public void afterViews() {
        etOfficeName = (EditText) findViewById(R.id.et_office_name_add);
        btnLocation = (ImageButton) findViewById(R.id.btn_location);
        officeImage = (ImageView) findViewById(R.id.iv_image_office);
        btnAdd = (FloatingActionButton) findViewById(R.id.fab_add_office);
        tvLat = (TextView) findViewById(R.id.tv_lat_maps);
        tvLng = (TextView) findViewById(R.id.tv_lng_maps);

        officeImage.setOnClickListener(new View.OnClickListener() { //klik na image view
            @Override
            public void onClick(View v) {
                importFromStorageIntent();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() { //klik na faboulous button
            @Override
            public void onClick(View v) {
                office.setName(etOfficeName.getText().toString());


                HomeActivity.officeDao.write(office);
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() { //otvara maps activity
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewOfficeActivity.this, MapsActivity.class);
                startActivityForResult(i, RESULT_MAPS_ACTIVITY);
            }
        });

    }

    public void importFromStorageIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            if (isStoragePermissionGranted()) {
                String imagePath = getImageFromStorage(data); //vraca string image path-a
                officeImage.setImageBitmap(BitmapFactory.decodeFile(imagePath)); //postavlja sliku na image view
                storePhoto(); //dodaje sliku na storage
                toast = Toast.makeText(AddNewOfficeActivity.this, "Wait while image is uploading...", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else if (requestCode == RESULT_MAPS_ACTIVITY && resultCode == RESULT_OK) {
            if (isMapsPermissionGranted()) {
                String lat = data.getStringExtra("lat");
                String lng = data.getStringExtra("lng");
                tvLat.setText(lat);
                tvLng.setText(lng);

                office.setLat(lat);
                office.setLng(lng);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getImageFromStorage(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        return picturePath;
    }

    public boolean isStoragePermissionGranted() { //proverava permission za internal storage
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission", "Granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(AddNewOfficeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_INTERNAL_STORAGE);
                return false;
            }
        }
        return false;
    }

    public boolean isMapsPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION);
            } else {
                return false;
            }
        }
        return false;
    }


    public void storePhoto() { //metoda postavlja bitmap na image view i upisuje tu sliku u firebase storage
        PhotoStorage photoStorage = new PhotoStorage();
        photoStorage.storeImage(officeImage, new OnImageUploadedListener() {
            @Override
            public void onImageUploaded(String url) {
                office.setImgUrl(url);
                Log.d("URI", url);
                if (toast != null) { //gasi image uploading toast i postavlja image done toast
                    toast = null;
                }
                toast = Toast.makeText(AddNewOfficeActivity.this, "Image is Uploaded!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


}
