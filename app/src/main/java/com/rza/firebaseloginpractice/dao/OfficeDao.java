package com.rza.firebaseloginpractice.dao;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.rza.firebaseloginpractice.activity.HomeActivity;
import com.rza.firebaseloginpractice.model.Office;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rza on 26-Aug-17.
 */

public class OfficeDao {



    private static final String TAG_OFFICES = "offices";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private List<Office> officeList = new ArrayList<>();

    private Map<String, Office> officeMap = new HashMap<>();

    private static OfficeDao instance = null;

    private EmployeeDao employeeDao = new EmployeeDao();


    protected OfficeDao() {

    }

    public static OfficeDao getInstance() {
        if (instance == null) {
            instance = new OfficeDao();
        }
        return instance;
    }

    public void init() {
        database.getReference(TAG_OFFICES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                officeMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Office>>() {});
                publish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void write(Office office) {
        DatabaseReference reference = database.getReference(TAG_OFFICES);

        reference = reference.push();
        office.setId(reference.getKey());
        reference.setValue(office);
    }

    private void publish() {
        officeList.clear();
        if (officeMap != null) {
            officeList.addAll(officeMap.values());
        }
    }

    public List<Office> getOfficeList() {
        return officeList;
    }

    public void delete(Office office) {
        DatabaseReference reference = database.getReference(TAG_OFFICES).child(office.getId());
        reference.removeValue();
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }
}
