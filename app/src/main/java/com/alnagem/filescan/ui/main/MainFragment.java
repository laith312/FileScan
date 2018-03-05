package com.alnagem.filescan.ui.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alnagem.filescan.R;
import com.alnagem.filescan.model.ScanResult;
import com.alnagem.filescan.ui.mvpbase.BaseMVPFragment;
import com.alnagem.filescan.ui.results.ResultsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lalnagem on 3/4/18.
 */

public class MainFragment extends BaseMVPFragment<MainFragmentView, MainFragmentPresenter> implements MainFragmentView {

    @BindView(R.id.start_button)
    Button searchButton;

    @BindView(R.id.result_button)
    Button resultButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.demo_delay)
    EditText demoDelay;

    private MainFragmentViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);
        mViewModel.presenter = getPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        searchButton.setOnClickListener((v) -> {
            mViewModel.demoSleepDuration = demoDelay.getText().toString();
            if (mViewModel.demoSleepDuration == null || mViewModel.demoSleepDuration.length() == 0) {
                mViewModel.demoSleepDuration = "0";
            }
            getPresenter().setDemoSleepDuration(Long.parseLong(mViewModel.demoSleepDuration));
            getPresenter().toggleScan();
        });

        demoDelay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.demoSleepDuration = demoDelay.getText().toString();
            }
        });

        getPresenter().setResultState();
        getPresenter().setSearchButtonState();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    protected MainFragmentPresenter createPresenter() {
        if (mViewModel == null) {
            return new MainFragmentPresenter();
        } else {
            return mViewModel.presenter;
        }
    }

    @NonNull
    @Override
    protected MainFragmentView getMVPView() {
        return this;
    }

    @Override
    public void updateProgressBar(int value) {
        mViewModel.progress = value;
        progressBar.setProgress(mViewModel.progress);
    }

    @Override
    public void resetProgressbar() {
        progressBar.setProgress(0);
        resultButton.setClickable(false);
        resultButton.setOnClickListener(null);
    }

    @Override
    public void enableResults(ScanResult result) {
        resultButton.setOnClickListener(v -> {
            ResultsFragment newFragment = new ResultsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("result", result);
            newFragment.setArguments(bundle);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_content, newFragment, null).addToBackStack(null).commit();
        });
        resultButton.setClickable(true);
        resultButton.setBackgroundColor(getResources().getColor(R.color.clickableBlue));
    }

    @Override
    public void disableResults() {
        resultButton.setClickable(false);
        resultButton.setBackgroundResource(android.R.drawable.btn_default);
    }

    @Override
    public void setSeaButtonToStart() {
        searchButton.setText(getString(R.string.start));
        searchButton.setBackgroundResource(android.R.drawable.btn_default);
    }

    @Override
    public void setSearchButtonToStop() {
        searchButton.setText(getString(R.string.stop));
        searchButton.setBackgroundColor(getResources().getColor(R.color.cancelRed));
    }

    @Override
    public void sendStartNotification() {
        String channelID = "notify_001";

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), channelID);
        Intent ii = new Intent(getContext(), MainFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Scanning Files");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID,
                    "ScanFiles Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
