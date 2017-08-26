package com.rza.firebaseloginpractice.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.dao.EmployeeDao;
import com.rza.firebaseloginpractice.model.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rza on 23-Aug-17.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private ArrayList<Employee> employees = new ArrayList<>();

    public EmployeeAdapter(){

    }


    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.recycler_employee_row;

        View view = inflater.inflate(layoutId, parent, false);
        EmployeeViewHolder vh = new EmployeeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.bind(employee);
    }

    @Override
    public int getItemCount() {
        if (employees != null) {
            return employees.size();

        }
        else {
            Log.d("employeeSize", "0");
            return 0;
        }
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmail;
        public TextView tvPosition;
        public SimpleDraweeView sdvImg;
        public TextView tvDateOfBirth;

        public EmployeeViewHolder(View v) {
            super(v);
            tvName = (TextView)v.findViewById(R.id.tv_employee_name);
            tvEmail = (TextView) v.findViewById(R.id.tv_employee_email);
            tvPosition = (TextView) v.findViewById(R.id.tv_employee_position);
            sdvImg= (SimpleDraweeView) v.findViewById(R.id.sdv_employee_image);
            tvDateOfBirth = (TextView) v.findViewById(R.id.tv_employee_birth);
        }

        public void bind(Employee employee) {
            tvName.setText(employee.getName());
            tvDateOfBirth.setText(employee.getDate());
            tvPosition.setText(employee.getPosition());
            if (employee.getImgUri() != null) {
                sdvImg.setImageURI(Uri.parse(employee.getImgUri()));
            }else {
                sdvImg.setImageURI("http://ornithology.ucc.ie/wp-content/uploads/sites/41/2015/10/Unknown-person.gif");
            }

            tvEmail.setText(employee.getEmail());
        }




    }

    public void setEmployees(ArrayList employees) {
        this.employees.clear();
        this.employees.addAll(employees);
        notifyDataSetChanged();
    }
}
