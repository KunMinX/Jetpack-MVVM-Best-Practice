package com.kunminx.puremusic.data.bean;

import java.io.File;

/**
 * Create by KunMinX at 20/03/17
 */
public class DownloadFile {

    private int progress;
    private File file;
    private boolean forgive;

    public DownloadFile() {
    }

    public DownloadFile(int progress, File file, boolean forgive) {
        this.progress = progress;
        this.file = file;
        this.forgive = forgive;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isForgive() {
        return forgive;
    }

    public void setForgive(boolean forgive) {
        this.forgive = forgive;
    }
}
