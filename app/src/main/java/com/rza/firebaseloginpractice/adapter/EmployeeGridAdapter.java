package com.rza.firebaseloginpractice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rza.firebaseloginpractice.R;

/**
 * Created by Rza on 26-Aug-17.
 */

public class EmployeeGridAdapter extends BaseAdapter {

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recycler_employee_row_grid;
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(layoutId, parent, false);
        EmployeeViewHolder vh = new EmployeeViewHolder(v);
        return vh;
    }
}
