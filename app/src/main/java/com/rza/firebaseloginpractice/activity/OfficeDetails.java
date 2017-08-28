package com.rza.firebaseloginpractice.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_details);
        tvName = (TextView) findViewById(R.id.tv_office_name_details);
        sdvImage = (SimpleDraweeView) findViewById(R.id.sdv_office_image_details);


        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        Office office = (Office)bundle.getSerializable("office");
        String officeName = office.getName();
        String imgUri = office.getImgUrl();
        String officeId = office.getId();
        Log.d("office Id", officeId);
        employees.addAll(HomeActivity.employeeDao.getEmployeesByOfficeId(officeId));
        Log.d("employees", String.valueOf(employees.size()));


        tvName.setText(officeName);

        if (imgUri != null) {
            sdvImage.setImageURI(imgUri);
        }
        else {
            sdvImage.setImageResource(R.drawable.officeroom);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees_office_details);
        adapter = new EmployeeLinearAdapterEmployee();
        adapter.setEmployees(employees);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


    }
}
