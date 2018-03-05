package com.alnagem.filescan.ui.results;

import java.io.File;
import java.util.List;

/**
 * Created by lalnagem on 3/5/18.
 */

public interface ResultsFragmentView {
    void displayAverageSize(String averageFileSize);

    void displayTopFiveEntensions(String topFiveExtensions);

    void displayTopFiles(List<File> topTenFiles);

    void setupShareButton(String shareText);
}
