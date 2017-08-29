package com.rza.firebaseloginpractice.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
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
    private static int RECYCLER_LINEAR_LAYOUT = 1;
    private static int RECYCLER_GRID_LAYOUT = 2;



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
    public void onLongClickListener(final Employee employee) { //on long raisuje dialog email, delete, phone
        final Dialog d = new Dialog(EmployeesListActivity.this);
        d.setContentView(R.layout.dialog_email_delete);

        ImageButton imgEmail = (ImageButton) d.findViewById(R.id.ib_email);
        ImageButton imgPhone = (ImageButton) d.findViewById(R.id.ib_phone);
        ImageButton imgDelete = (ImageButton) d.findViewById(R.id.ib_delete);
        d.show();

        imgDelete.setOnClickListener(new View.OnClickListener() { //on delete listener
            @Override
            public void onClick(View v) { //listener za imgdelete koji brise employee-a
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        HomeActivity.employeeDao.delete(employee);
                        adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                        Toasty.warning(EmployeesListActivity.this, "Employee deleted!", Toast.LENGTH_SHORT, true).show();
                        dialog.dismiss();
                        d.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        d.dismiss();
                        break;
                }


            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeesListActivity.this);
        builder.setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
            }
        });

        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //poziva intent za phone dial
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + employee.getNumber()));
                startActivity(i);
            }
        });



        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //poziva intent za mejl
                String email = employee.getEmail();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send Email..."));
            }
        });


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
    public boolean onOptionsItemSelected(MenuItem item) { //handluje klik na menu ikonicu
        int id = item.getItemId();
        if (id == R.id.menu_icon_sort) {
                Collections.reverse(HomeActivity.employeeDao.getEmployees());
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees()); //SORTIRANJE (IZVRTANJE)
        }
        else if (id == R.id.menu_item_grid) { //menja izgled recyclera u grid
            if (gridListCounter == RECYCLER_LINEAR_LAYOUT) {
                gridListCounter = RECYCLER_GRID_LAYOUT;
                adapter = null;
                adapter = new EmployeeGridAdapterEmployee(this);
                adapter.setEmployees(HomeActivity.employeeDao.getEmployees());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(gridLayoutManager);
                item.setIcon(R.drawable.ic_list_black_24dp);

            }
            else if (gridListCounter == RECYCLER_GRID_LAYOUT) { //menja layout recyclera u linear
                gridListCounter = RECYCLER_LINEAR_LAYOUT;
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

    private void afterViews() { //view inject
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
