package com.alnagem.filescan.ui.results;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alnagem.filescan.R;
import com.alnagem.filescan.model.ScanResult;
import com.alnagem.filescan.ui.mvpbase.BaseMVPFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lalnagem on 3/4/18.
 */

public class ResultsFragment extends BaseMVPFragment<ResultsFragmentView, ResultsFragmentPresenter> implements ResultsFragmentView {

    @BindView(R.id.top_extensions)
    TextView topFiveExtensionsView;

    @BindView(R.id.average_file_size)
    TextView averageFileSizeView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.share_button)
    Button shareButton;

    private ResultsAdapter resultsAdapter;
    private ResultsFragmentViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mViewModel = ViewModelProviders.of(this).get(ResultsFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, view);

        resultsAdapter = new ResultsAdapter(new ArrayList<>());
        recyclerView.setAdapter(resultsAdapter);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        ScanResult scanResult = bundle != null ? bundle.getParcelable("result") : null;
        getPresenter().setScanResult(scanResult);
    }

    @NonNull
    @Override
    protected ResultsFragmentPresenter createPresenter() {
        return new ResultsFragmentPresenter();
    }

    @NonNull
    @Override
    protected ResultsFragmentView getMVPView() {
        return this;
    }

    @Override
    public void displayAverageSize(String averageFileSize) {
        mViewModel.averageFileSize = averageFileSize;
        averageFileSizeView.setText(mViewModel.averageFileSize);
    }

    @Override
    public void displayTopFiveEntensions(String topFiveExtensions) {
        mViewModel.topFiveExtensions = topFiveExtensions;
        topFiveExtensionsView.setText(mViewModel.topFiveExtensions);
    }

    @Override
    public void displayTopFiles(List<File> topTenFiles) {
        mViewModel.tenLargestFiles = topTenFiles;
        resultsAdapter.setmData(mViewModel.tenLargestFiles);
    }

    @Override
    public void setupShareButton(String shareText) {
        shareButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        });
    }
}
