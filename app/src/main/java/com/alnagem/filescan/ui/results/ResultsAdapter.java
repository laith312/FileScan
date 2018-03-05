package com.alnagem.filescan.ui.results;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alnagem.filescan.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lalnagem on 3/5/18.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ScanResultViewHolder> {
    List<File> mData;

    public ResultsAdapter(List<File> data) {
        this.mData = data;
    }

    public void setmData(List<File> newData) {
        mData.clear();
        mData = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ScanResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanResultViewHolder holder, int position) {
        holder.fileName.setText(mData.get(position).getName());

        double size = (double) mData.get(position).length() / 1000000;
        holder.fileSize.setText(String.format("%.2f", size));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ScanResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.file_name)
        TextView fileName;

        @BindView(R.id.file_size)
        TextView fileSize;

        public ScanResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
