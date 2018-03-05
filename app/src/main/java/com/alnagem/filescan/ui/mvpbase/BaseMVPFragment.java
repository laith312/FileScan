package com.alnagem.filescan.ui.mvpbase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lalnagem on 3/3/18.
 */

public abstract class BaseMVPFragment<V, P extends BaseMVPPresenter<V>> extends Fragment {

    private P mPresenter;

    public BaseMVPFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = createPresenter();
        mPresenter.attachView(getMVPView());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentActivity activity = getActivity();
        if (activity != null && activity.isFinishing()) {
            mPresenter.detachView();
        }
    }

    @NonNull
    protected abstract P createPresenter();

    @NonNull
    protected abstract V getMVPView();

    public P getPresenter() {
        return mPresenter;
    }
}