package com.rza.firebaseloginpractice.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.adapter.EmployeeBaseAdapter;
import com.rza.firebaseloginpractice.adapter.EmployeeGridAdapterEmployee;
import com.rza.firebaseloginpractice.adapter.EmployeeLinearAdapterEmployee;
import com.rza.firebaseloginpractice.model.Employee;

import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class EmployeesListActivity extends AppCompatActivity implements EmployeeBaseAdapter.EmployeeAdapterOnLongClickListener{

    private RecyclerView recyclerView;
    private EmployeeBaseAdapter adapter;
    private FloatingActionButton fab;
    private static String TAG = "LOG";
    private int gridListCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_list);
        adapter = new EmployeeBaseAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_employees);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_employee);
        gridListCounter = 1;
        afterViews();




    }

    @Override
    public void onLongClickListener(final Employee employee) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        HomeActivity.employeeDao.delete(employee);
                        adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                        Toasty.success(EmployeesListActivity.this, "Employee deleted!", Toast.LENGTH_SHORT, true).show();

                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }


            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
        HomeActivity.employeeDao.delete(employee);



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
                adapter = new EmployeeGridAdapterEmployee(this);
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
                adapter = new EmployeeLinearAdapterEmployee(this);
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
                recyclerView.addItemDecoration(dividerItemDecoration);
                item.setIcon(R.drawable.ic_grid_on_black_24dp);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void afterViews() {
        adapter = new EmployeeLinearAdapterEmployee(this);
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
