package com.rza.firebaseloginpractice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.rza.firebaseloginpractice.R;
import com.rza.firebaseloginpractice.model.Office;

import java.util.ArrayList;

/**
 * Created by Rza on 28-Aug-17.
 */

public class OfficeAdapter extends RecyclerView.Adapter<OfficeAdapter.OfficeViewHolder> {

    private ArrayList<Office> offices;
    private Context context;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int index);
    }

    public OfficeAdapter(ListItemClickListener listener) {
        this.mOnClickListener = listener;
    }


    @Override
    public OfficeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutId = R.layout.recycler_office_row;

        View view = inflater.inflate(layoutId, parent, false);
        OfficeViewHolder vh = new OfficeViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(OfficeViewHolder holder, int position) {
        Office office = offices.get(position);
        holder.bind(office);
    }

    @Override
    public int getItemCount() {
        if (offices != null) {
            return offices.size();
        }
        else
        {
            return 0;
        }
    }

    public class OfficeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvName;
        SimpleDraweeView sdvOfficeImage;

        public OfficeViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tv_office_name);
            sdvOfficeImage = (SimpleDraweeView) v.findViewById(R.id.sdv_office_image);
            v.setOnClickListener(this);
        }


        void bind(Office office) {
            tvName.setText(office.getName());
            if (office.getImgUrl() != null) {
                sdvOfficeImage.setImageURI(office.getImgUrl());
            }
            else {
                sdvOfficeImage.setImageResource(R.drawable.officeroom);
            }
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }



    public void setContext(Context context) {
        this.context = context;
    }

    public void setOffices(ArrayList<Office> offices) {
        this.offices = offices;
    }
}
