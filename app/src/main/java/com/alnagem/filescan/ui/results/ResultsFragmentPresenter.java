package com.alnagem.filescan.ui.results;

import com.alnagem.filescan.model.ScanResult;
import com.alnagem.filescan.ui.mvpbase.BaseMVPPresenter;

import java.io.File;

/**
 * Created by lalnagem on 3/5/18.
 */

public class ResultsFragmentPresenter extends BaseMVPPresenter<ResultsFragmentView> {
    private ScanResult scanResult;


    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
        updateUIwithScanResult();
    }

    private void updateUIwithScanResult() {
        if (getMvpView() != null) {
            getMvpView().displayAverageSize(String.format("%.2f", scanResult.getAverageFileSize()));

            StringBuilder extensions = new StringBuilder();
            for (String extension : scanResult.getTopFiveExtensions()) {
                extensions.append(".").append(extension).append("   ");
            }
            getMvpView().displayTopFiveEntensions(extensions.toString());

            getMvpView().displayTopFiles(scanResult.getTopTenFiles());

            StringBuilder largestFiles = new StringBuilder();
            for (File f : scanResult.getTopTenFiles()) {
                String fileString = f.getName() + " " + f.length() + "\n";
                largestFiles.append(fileString).append(" ");
            }

            String shareText = "FileScan: averageFileSize:" + String.format("%.2f", scanResult.getAverageFileSize()) +
                    " topFiveExtensions:" + extensions.toString() + " largestFiles:" + largestFiles;

            getMvpView().setupShareButton(shareText);
        }
    }
}
