package com.rza.firebaseloginpractice.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.model.Employee;
import com.rza.firebaseloginpractice.storage.OnImageUploadedListener;
import com.rza.firebaseloginpractice.storage.PhotoStorage;

import java.util.Collections;
import java.util.Comparator;

public class AddNewEmployeeActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPosition;
    private EditText etDate;
    private ImageView imgEmployee;
    private Button btnAdd;
    private Employee employee;
    private static int RESULT_LOAD_IMAGE = 2;
    private static int MY_PERMISSION_INTERNAL_STORAGE = 3;
    private static int RESULT_CAMERA_IMAGE = 4;
    private static int MY_PERMISSION_CAMERA = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_employee);
        employee = new Employee();

        etName = (EditText) findViewById(R.id.et_employee_name);
        etEmail = (EditText) findViewById(R.id.et_employee_email);
        etPosition = (EditText) findViewById(R.id.et_position);
        etDate = (EditText) findViewById(R.id.et_employee_date_of_birth);
        imgEmployee = (ImageView) findViewById(R.id.iv_employee_image);
        btnAdd = (Button) findViewById(R.id.btn_add_employee);
        etName.requestFocus();




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //klik na add employee

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String position = etPosition.getText().toString();
                employee.setName(name);
                if (email.equals("")) {
                    employee.setEmail("Undefined");
                }
                else {
                    employee.setEmail(email);
                }
                employee.setPosition(position);

                if (name.equals("") || position.equals("")) {
                    Toast.makeText(AddNewEmployeeActivity.this, "Please Fill All Necessary Fields", Toast.LENGTH_SHORT).show();
                }
                else {
                        HomeActivity.employeeDao.write(employee); //upisivanje u bazu
                        HomeActivity.employeeDao.getEmployees().add(employee); //upisuje zaposlenog u array listu
                        Collections.sort(HomeActivity.employeeDao.getEmployees(), new Comparator<Employee>() { //sortira array listu po imenu
                            @Override
                            public int compare(Employee o1, Employee o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });
                        Toast.makeText(AddNewEmployeeActivity.this, employee.toString() + " added to Database", Toast.LENGTH_SHORT).show();
                        clearViews(); //sets name, email, position, date to ""
                }
            }
        });

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { //klik na datum polje
                if (hasFocus) {
                    final Dialog dialog = new Dialog(AddNewEmployeeActivity.this);
                    dialog.setContentView(R.layout.dialog_calendar);
                    Button btnOk = (Button) dialog.findViewById(R.id.btn_dialog_ok);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
                    final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker_employee);

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//ok na na date picker dialogu
                            String date = String.valueOf(datePicker.getDayOfMonth()) + "." + String.valueOf(datePicker.getMonth()) + "." + String.valueOf(datePicker.getYear());
                            employee.setDate(date);
                            etDate.setText(date);
                            etName.requestFocus();
                            dialog.dismiss();
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //cancel na date picker dialogu
                            dialog.dismiss();
                            etName.requestFocus();
                        }
                    });
                    dialog.show();
                }
            }
        });

        imgEmployee.setOnClickListener(new View.OnClickListener() { //listener image view-a otvara dialog odakle se bira import path (camera / storage)
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddNewEmployeeActivity.this);
                dialog.setContentView(R.layout.dialog_import_photo);
                ImageButton btnCamera = (ImageButton) dialog.findViewById(R.id.btn_dialog_camera);
                ImageButton btnStorage = (ImageButton) dialog.findViewById(R.id.btn_dialog_storage);
                dialog.show();

                btnStorage.setOnClickListener(new View.OnClickListener() { //klik na ikonicu storage-a u dialogu
                    @Override
                    public void onClick(View v) {
                        importFromStorage();
                        dialog.dismiss();
                    }
                });

                btnCamera.setOnClickListener(new View.OnClickListener() { //klik na ikonicu kamere u dialogu
                    @Override
                    public void onClick(View v) {
                        importFromCamera();
                        dialog.dismiss();
                    }
                });

            }
        });
    }


    public void clearViews() { //brise sve sto je bilo u viewovima
        etName.setText("");
        etEmail.setText("");
        etPosition.setText("");
        etDate.setText("");
        imgEmployee.setImageResource(R.drawable.add_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) { //otvara internal storage
            if (isStoragePermissionGranted()) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                imgEmployee.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                storePhoto();
            }
        }
        else if (requestCode == RESULT_CAMERA_IMAGE && resultCode == RESULT_OK && data != null) { //otvara kameru
            if (isCameraPermissionGranted()) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgEmployee.setImageBitmap(photo);
                storePhoto();
            }
        }
    }

    public boolean isStoragePermissionGranted() { //proverava permission za internal storage
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission", "Granted");
                return true;
            } else {
                ActivityCompat.requestPermissions(AddNewEmployeeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_INTERNAL_STORAGE);
                return false;
            }
        }
        return false;
    }

    public boolean isCameraPermissionGranted() { //proverava permission za kameru
        if (Build.VERSION.SDK_INT >=23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission", "granted");
                return true;
            }
            else {
                ActivityCompat.requestPermissions(AddNewEmployeeActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
                return false;
            }
        }
        return false;
    }

    public void importFromStorage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void importFromCamera() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, RESULT_CAMERA_IMAGE);
    }

    public void storePhoto() { //metoda postavlja bitmap na image view i upisuje tu sliku u firebase storage
        PhotoStorage photoStorage = new PhotoStorage();
        photoStorage.storeImage(imgEmployee, new OnImageUploadedListener() {
            @Override
            public void onImageUploaded(String url) {
                employee.setImgUri(url);
                Log.d("URI", url);
                Toast.makeText(AddNewEmployeeActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
