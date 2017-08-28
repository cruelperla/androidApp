package com.rza.firebaseloginpractice.activity;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class OfficeDetails extends AppCompatActivity {
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
                if (lat != null && lng != null) {
                    LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
                    String provider = locationManager.getBestProvider(new Criteria(), false);
                    try {
                        Location location = locationManager.getLastKnownLocation(provider);
                        userLat = String.valueOf(location.getLatitude());
                        userLng = String.valueOf(location.getLongitude());

                    }
                    catch (SecurityException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + userLat + "," + userLng + "&daddr=" + lat + "," + lng));
                    startActivity(intent);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees_office_details);
        adapter = new EmployeeLinearAdapterEmployee();
        adapter.setEmployees(employees);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
