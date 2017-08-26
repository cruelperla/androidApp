package com.rza.firebaseloginpractice.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.adapter.EmployeeAdapter;
import com.rza.firebaseloginpractice.dao.EmployeeDao;
import com.rza.firebaseloginpractice.model.Employee;
import com.rza.firebaseloginpractice.model.User;

import java.util.ArrayList;

public class EmployeesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private FloatingActionButton fab;
    private static String TAG = "LOG";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_employee);
        afterViews();

    }

    private void afterViews() {
        adapter = new EmployeeAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmployeesListActivity.this, AddNewEmployeeActivity.class);
                startActivity(i);
            }
        });

        adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
    }
}
