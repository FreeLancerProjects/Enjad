package com.developer.enjad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.enjad.R;
import com.developer.enjad.databinding.ReportRow2Binding;
import com.developer.enjad.databinding.ReportRowBinding;
import com.developer.enjad.models.ReportModel;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_DATA2 = 2;

    private List<ReportModel> dataList;
    private Context context;

    public ReportsAdapter(Context context, List<ReportModel> dataList) {

        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            ReportRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.report_row,parent,false);
            return new MyHolder(binding);
        } else {
            ReportRow2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.report_row2,parent,false);
            return new MyHolder2(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            MyHolder myHolder = (MyHolder) holder;
            myHolder.binding.setModel(dataList.get(position));

        } else {
            MyHolder2 myHolder2 = (MyHolder2) holder;
            myHolder2.binding.setModel(dataList.get(position));

        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ReportRowBinding binding;
        public MyHolder(ReportRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }

    public class MyHolder2 extends RecyclerView.ViewHolder {

        private ReportRow2Binding binding;
        public MyHolder2(ReportRow2Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position%2 == 0) {
            return ITEM_DATA2;
        } else {
            return ITEM_DATA;

        }


    }
}
