package com.alnagem.filescan.ui.mvpbase;

import android.support.annotation.Nullable;

/**
 * Created by lalnagem on 3/2/18.
 */

public abstract class BaseMVPPresenter<V> {

    private V mvpView;

    public BaseMVPPresenter() {
    }

    public final void attachView(V mvpView) {
        this.mvpView = mvpView;
        onMvpViewAttached();
    }

    protected void onMvpViewAttached() {
    }

    public final void detachView() {
        onMvpViewDetached();
        this.mvpView = null;
    }

    protected void onMvpViewDetached() {
    }

    @Nullable
    protected V getMvpView() {
        return this.mvpView;
    }
}
