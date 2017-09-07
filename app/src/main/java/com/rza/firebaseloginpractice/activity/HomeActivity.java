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

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {
    private SimpleDraweeView sdvImage;
    private Intent intent;
    private GoogleApiClient api;
    private Button btnEmployees;
    private Button btnAssets;
    static EmployeeDao employeeDao;
    static OfficeDao officeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //create this activity if logged in, otherwise raise login activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        afterViews();

        employeeDao = EmployeeDao.getInstance();
        officeDao = OfficeDao.getInstance();

        employeeDao.init(); //initializes both daos
        officeDao.init();



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        api = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toasty.error(HomeActivity.this, "Connection failed!", Toast.LENGTH_SHORT, true).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, OfficesListActivity.class);
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


    private void populateViews() { //iz intenta uzima podatke i populise viewove
        intent = getIntent();
        String imageUri = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");

        Toasty.success(HomeActivity.this, "Welcome " + name, Toast.LENGTH_SHORT, true).show();

        sdvImage.setImageURI(Uri.parse(imageUri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //inflates menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //firebase sign out
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

    public void afterViews() { //view inject
        sdvImage = (SimpleDraweeView) findViewById(R.id.sdv_user_image_home);
        btnAssets = (Button) findViewById(R.id.btn_assets);
        btnEmployees = (Button) findViewById(R.id.btn_employees);
    }

    @Override
    public void onBackPressed() {

    }
}
