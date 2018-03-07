package com.alnagem.filescan.ui.main;

import android.os.SystemClock;
import android.util.Log;

import com.alnagem.filescan.model.ScanResult;
import com.alnagem.filescan.ui.mvpbase.BaseMVPPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lalnagem on 3/4/18.
 */

class MainFragmentPresenter extends BaseMVPPresenter<MainFragmentView> {

    private static final String TAG = MainFragmentPresenter.class.getName();

    private Disposable disposable;
    private List<File> allFiles = new ArrayList<>();
    private boolean isScanning;
    private ScanResult result;
    private static final String ROOT_PATH = "/sdcard/";

    protected long demoSleepDuration = 0;

    public void setDemoSleepDuration(long demoSleepDuration) {
        this.demoSleepDuration = demoSleepDuration;
    }

    void toggleScan() {
        if (!isScanning) {
            startScan();
        } else {
            stopScan();
        }
    }

    private void startScan() {
        result = null;
        resetProgressbar();
        setResultState();
        isScanning = true;
        setSearchButtonState();

        scanObservable.subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void stopScan() {
        disposable.dispose();
        result = null;
        allFiles = new ArrayList<>();
        isScanning = false;
        resetProgressbar();
        setSearchButtonState();
    }

    private void scan(ObservableEmitter<Integer> e) {
        sendStartNotification();
        e.onNext(10);
        File root = new File(ROOT_PATH);

        SystemClock.sleep(demoSleepDuration);

        HashMap<String, Integer> fileExtensions = new HashMap<>();
        getFileExtensions(root, fileExtensions);
        e.onNext(30);

        SystemClock.sleep(demoSleepDuration);

        double averageFileSize = (getAverageFileSize(allFiles) / 1000000);

        HashMap<String, Integer> sortedMap = (HashMap<String, Integer>) sortByValue(fileExtensions);
        e.onNext(50);

        SystemClock.sleep(demoSleepDuration);

        Collections.sort(allFiles, (o1, o2) -> {
            return (int) (o2.length() - o1.length());
        });
        e.onNext(85);

        SystemClock.sleep(demoSleepDuration);
        List<String> extensions = generateExtensionList(sortedMap);

        e.onNext(90);

        SystemClock.sleep(demoSleepDuration);

        if (!disposable.isDisposed()) {
            result = new ScanResult(extensions, allFiles.subList(0, allFiles.size() >= 10 ? 10 : allFiles.size()), averageFileSize);
        }

        e.onNext(100);
    }

    List<String> generateExtensionList(HashMap<String, Integer> map) {
        List<String> result = new ArrayList<>();

        List<String> keys = new ArrayList<>(map.keySet()).subList(0, map.size() < 5 ? map.size() : 5);

        for (String key : keys) {
            result.add(key + " (" + map.get(key) + ")");
        }

        return result;
    }


    private double getAverageFileSize(List<File> allFiles) {
        if (allFiles.isEmpty()) {
            return 0;
        }
        double totalSize = 0;
        for (File f : allFiles) {
            totalSize += f.length();
        }
        return totalSize / allFiles.size();
    }

    private void getFileExtensions(File root, HashMap<String, Integer> fileExtensions) {
        File[] files = root.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileExtensions(f, fileExtensions);
            } else {
                allFiles.add(f);
                int i = f.getAbsolutePath().lastIndexOf('.');
                String extension = f.getAbsolutePath().substring(i + 1);

                if (extension.length() > 0) {
                    if (fileExtensions.containsKey(extension)) {
                        fileExtensions.put(extension, fileExtensions.get(extension) + 1);
                    } else {
                        fileExtensions.put(extension, 1);
                    }
                }
            }
        }
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private Observable<Integer> scanObservable = Observable.create(
            e -> {
                scan(e);
                e.onComplete();
            }
    );

    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.v(TAG, "onSubscribe: ");
            disposable = d;
        }

        @Override
        public void onNext(Integer value) {
            Log.v(TAG, "onNext: " + value);
            updateProgressBar(value);
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: " + e.getLocalizedMessage());
        }

        @Override
        public void onComplete() {
            Log.v(TAG, "onComplete");
            isScanning = false;
            setSearchButtonState();
            updateProgressBar(0);
            setResultState();
            disposable.dispose();
        }
    };

    void setResultState() {
        if (result != null) {
            enableResultsButton();
        } else {
            disableResultsButton();
        }
    }

    private void enableResultsButton() {
        if (getMvpView() != null) {
            getMvpView().enableResults(result);
        }
    }

    private void disableResultsButton() {
        if (getMvpView() != null) {
            getMvpView().disableResults();
        }
    }

    void setSearchButtonState() {
        if (isScanning) {
            setSearchButtonToStop();
        } else {
            setSearchButtonToStart();
        }
    }

    private void setSearchButtonToStart() {
        if (getMvpView() != null) {
            getMvpView().setSeaButtonToStart();
        }
    }

    private void setSearchButtonToStop() {
        if (getMvpView() != null) {
            getMvpView().setSearchButtonToStop();
        }
    }

    private void updateProgressBar(int value) {
        if (getMvpView() != null) {
            getMvpView().updateProgressBar(value);
        }
    }

    private void resetProgressbar() {
        if (getMvpView() != null) {
            getMvpView().resetProgressbar();
        }
    }

    private void sendStartNotification() {
        if (getMvpView() != null) {
            getMvpView().sendStartNotification();
        }
    }
}
