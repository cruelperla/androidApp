package com.rza.firebaseloginpractice.dao;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.rza.firebaseloginpractice.model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rza on 25-Aug-17.
 */

public class EmployeeDao {
    private static final String TAG_EMPLOYEES = "employees";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private ArrayList<Employee> employees = new ArrayList<>();

    private Map<String, Employee> employeeMap = new HashMap<>();

    private static String TAG = "LOG";

    private static EmployeeDao instance = null;

    protected EmployeeDao() {

    }

    public static EmployeeDao getInstance() {
        if (instance == null) {
            instance = new EmployeeDao();
        }
        return instance;
    }


    public void init() { //
        database.getReference(TAG_EMPLOYEES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    employeeMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Employee>>() {
                    });
                    publish();
                    Log.d("employeeMap: ", "Published");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void write(Employee employee) {
        DatabaseReference reference = database.getReference(TAG_EMPLOYEES);
        Log.d("reference", reference.toString());

            reference = reference.push();
            employee.setId(reference.getKey());
            reference.setValue(employee);

    }



    private void publish() { //sve zaposlene iz mape smesta u listu, zove ga init
        employees.clear();
        if (employeeMap != null) {
            employees.addAll(employeeMap.values());
            Collections.sort(employees, new Comparator<Employee>() {
                @Override
                public int compare(Employee o1, Employee o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            Log.d(TAG, "PUBLISHED");
        }

    }
    public ArrayList<Employee> getEmployees() {
        Log.d("getEmployee", employees.toString());
        return employees;
    }


}
