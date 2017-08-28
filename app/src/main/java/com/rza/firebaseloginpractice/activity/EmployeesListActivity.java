package com.rza.firebaseloginpractice.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.adapter.EmployeeBaseAdapter;
import com.rza.firebaseloginpractice.adapter.EmployeeGridAdapterEmployee;
import com.rza.firebaseloginpractice.adapter.EmployeeLinearAdapterEmployee;

import java.util.Collections;

public class EmployeesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeBaseAdapter adapter;
    private FloatingActionButton fab;
    private static String TAG = "LOG";
    private int gridListCounter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_employee);
        gridListCounter = 1;
        afterViews();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_employee_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_icon_sort) {
                Collections.reverse(HomeActivity.employeeDao.getEmployees());
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees()); //SORTIRANJE (IZVRTANJE)
        }
        else if (id == R.id.menu_item_grid) { //menja u grid
            if (gridListCounter == 1) {
                gridListCounter = 2;
                adapter = null;
                adapter = new EmployeeGridAdapterEmployee();
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(gridLayoutManager);
                item.setIcon(R.drawable.ic_list_black_24dp);

            }
            else if (gridListCounter == 2) { //menja u linear
                gridListCounter = 1;
                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                adapter = null;
                adapter = new EmployeeLinearAdapterEmployee();
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                item.setIcon(R.drawable.ic_grid_on_black_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void afterViews() {
        adapter = new EmployeeLinearAdapterEmployee();
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
}
