package com.rza.firebaseloginpractice.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.adapter.OfficeAdapter;

import java.util.ArrayList;

public class OfficesListActivity extends AppCompatActivity implements OfficeAdapter.ListItemClickListener {
    private RecyclerView recyclerView;
    private OfficeAdapter adapter;
    private FloatingActionButton fabAddOffice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offices_list);
        afterViews();

    }

    public void afterViews() { //inject views
        recyclerView = (RecyclerView) findViewById(R.id.rv_offices);
        fabAddOffice = (FloatingActionButton) findViewById(R.id.fab_add_office_list);

        fabAddOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //otvara addNewOfficeActivity
                Intent i = new Intent(OfficesListActivity.this, AddNewOfficeActivity.class);
                startActivity(i);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new OfficeAdapter(this);
        adapter.setOffices((ArrayList)HomeActivity.officeDao.getOfficeList());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onListItemClick(int index) { //otvara detalje offica
        Intent i = new Intent(this, OfficeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("office", HomeActivity.officeDao.getOfficeList().get(index));
        i.putExtras(bundle);
        startActivity(i);
    }
}
