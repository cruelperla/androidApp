package com.rza.firebaseloginpractice.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.dao.EmployeeDao;
import com.rza.firebaseloginpractice.dao.OfficeDao;
import com.rza.firebaseloginpractice.model.Office;

public class HomeActivity extends AppCompatActivity {
    private SimpleDraweeView sdvImage;
    private Intent intent;
    private GoogleApiClient api;
    private Button btnEmployees;
    private Button btnAssets;
    static EmployeeDao employeeDao;
    static OfficeDao officeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        afterViews();

        employeeDao = EmployeeDao.getInstance();
        officeDao = OfficeDao.getInstance();

        employeeDao.init();
        officeDao.init();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        api = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AddNewOfficeActivity.class);
                startActivity(i);
            }
        });




        btnEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, EmployeesListActivity.class);
                startActivity(i);
            }
        });

        populateViews();
    }


    private void populateViews() {
        intent = getIntent();
        String imageUri = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");

        Toast.makeText(HomeActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();

        sdvImage.setImageURI(Uri.parse(imageUri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(api).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("user", "signedOut");
                    Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
        }


        return super.onOptionsItemSelected(item);
    }

    public void afterViews() {
        sdvImage = (SimpleDraweeView) findViewById(R.id.sdv_user_image_home);
        btnAssets = (Button) findViewById(R.id.btn_assets);
        btnEmployees = (Button) findViewById(R.id.btn_employees);
    }
}
