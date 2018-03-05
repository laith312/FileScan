package com.alnagem.filescan.ui.results;

import com.alnagem.filescan.model.ScanResult;
import com.alnagem.filescan.ui.mvpbase.BaseMVPPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            List<String> topFive = scanResult.getTopFiveExtensions();
            for (String extension : topFive) {
                extensions.append(".").append(extension).append("\n");
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
