package com.rza.firebaseloginpractice.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.adapter.EmployeeBaseAdapter;
import com.rza.firebaseloginpractice.adapter.EmployeeLinearAdapterEmployee;
import com.rza.firebaseloginpractice.model.Employee;
import com.rza.firebaseloginpractice.model.Office;

import java.util.ArrayList;
import java.util.List;

public class OfficeDetailsActivity extends AppCompatActivity implements EmployeeBaseAdapter.EmployeeAdapterOnLongClickListener {
    private RecyclerView recyclerView;
    private EmployeeBaseAdapter adapter;
    private TextView tvName;
    private SimpleDraweeView sdvImage;
    private ArrayList<Employee> employees = new ArrayList<>();
    private ImageView ivNavigation;
    private String lat;
    private String lng;
    private String userLat;
    private String userLng;
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 55;
    private LocationManager locationManager;
    private Criteria criteria;
    private String providers;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_details);
        tvName = (TextView) findViewById(R.id.tv_office_name_details);
        sdvImage = (SimpleDraweeView) findViewById(R.id.sdv_office_image_details);
        ivNavigation = (ImageView) findViewById(R.id.iv_navigation_image);




        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        Office office = (Office)bundle.getSerializable("office");
        String officeName = office.getName();
        String imgUri = office.getImgUrl();
        String officeId = office.getId();
        lat = office.getLat();
        lng = office.getLng();
        employees.addAll(HomeActivity.employeeDao.getEmployeesByOfficeId(officeId));


        tvName.setText(officeName);

        if (imgUri != null) {
            sdvImage.setImageURI(imgUri);
        }
        else {
            sdvImage.setImageResource(R.drawable.officeroom);
        }

        ivNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(OfficeDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(OfficeDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OfficeDetailsActivity.this,
                            new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION);
                }
                driveToCoordinate();


            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees_office_details);
        adapter = new EmployeeLinearAdapterEmployee(this);
        adapter.setEmployees(employees);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onLongClickListener(Employee employee) {

    }

    private void driveToCoordinate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        location = getBestKnownLocation();
        if (lat != null && lng != null) {
            userLat = String.valueOf(location.getLatitude());
            userLng = String.valueOf(location.getLongitude());



            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + userLat + "," + userLng + "&daddr=" + lat + "," + lng));
            startActivity(intent);
        }

    }

    private Location getBestKnownLocation() {

            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;

            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);


                if (l == null) {
                    continue;
                }

                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
            return bestLocation;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    driveToCoordinate();
                }
            }
        }
    }
}
