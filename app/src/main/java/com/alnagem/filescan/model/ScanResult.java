package com.alnagem.filescan.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.List;

/**
 * Created by lalnagem on 3/5/18.
 */

public class ScanResult implements Parcelable {

    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel in) {
            return new ScanResult(in);
        }

        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };

    private List<String> topFiveExtensions;
    private List<File> topTenFiles;
    private double averageFileSize;

    public ScanResult(List<String> topFiveExtensions, List<File> topTenFiles, double averageFileSize) {
        this.topFiveExtensions = topFiveExtensions;
        this.topTenFiles = topTenFiles;
        this.averageFileSize = averageFileSize;
    }

    public List<String> getTopFiveExtensions() {
        return topFiveExtensions;
    }

    public void setTopFiveExtensions(List<String> topFiveExtensions) {
        this.topFiveExtensions = topFiveExtensions;
    }

    public List<File> getTopTenFiles() {
        return topTenFiles;
    }

    public void setTopTenFiles(List<File> topTenFiles) {
        this.topTenFiles = topTenFiles;
    }

    public double getAverageFileSize() {
        return averageFileSize;
    }

    public void setAverageFileSize(double averageFileSize) {
        this.averageFileSize = averageFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(topFiveExtensions);
        dest.writeList(topTenFiles);
        dest.writeDouble(averageFileSize);
    }


    private ScanResult(Parcel in) {
        topFiveExtensions = in.readArrayList(null);
        topTenFiles = in.readArrayList(null);
        averageFileSize = in.readDouble();
    }
}
