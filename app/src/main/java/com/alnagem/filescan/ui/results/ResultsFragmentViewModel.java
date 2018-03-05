package com.alnagem.filescan.ui.results;

import android.arch.lifecycle.ViewModel;

import java.io.File;
import java.util.List;

/**
 * Created by lalnagem on 3/5/18.
 */

public class ResultsFragmentViewModel extends ViewModel {
    String averageFileSize;
    List<File> tenLargestFiles;
    String topFiveExtensions;
}
