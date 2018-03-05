package com.alnagem.filescan.ui.main;

import com.alnagem.filescan.model.ScanResult;

/**
 * Created by lalnagem on 3/4/18.
 */

public interface MainFragmentView {
    void updateProgressBar(int value);

    void resetProgressbar();

    void sendStartNotification();

    void enableResults(ScanResult result);

    void disableResults();

    void setSeaButtonToStart();

    void setSearchButtonToStop();
}
