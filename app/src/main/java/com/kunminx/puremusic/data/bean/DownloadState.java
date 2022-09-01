package com.kunminx.puremusic.data.bean;

import java.io.File;

/**
 * Create by KunMinX at 2022/7/15
 */
public class DownloadState {
    public final boolean isForgive;
    public final int progress;
    public final File file;

    public DownloadState() {
        this.isForgive = false;
        this.progress = 0;
        this.file = null;
    }

    public DownloadState(boolean isForgive, int progress, File file) {
        this.isForgive = isForgive;
        this.progress = progress;
        this.file = file;
    }
}
