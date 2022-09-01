package com.kunminx.puremusic.domain.event;

import com.kunminx.puremusic.data.bean.DownloadState;

/**
 * Create by KunMinX at 2022/7/4
 */
public class DownloadEvent {
    public final static int EVENT_DOWNLOAD = 1;
    public final static int EVENT_DOWNLOAD_GLOBAL = 2;

    public final int eventId;
    public final DownloadState downloadState;

    public DownloadEvent(int eventId) {
        this.eventId = eventId;
        this.downloadState = new DownloadState();
    }

    public DownloadEvent(int eventId, DownloadState downloadState) {
        this.eventId = eventId;
        this.downloadState = downloadState;
    }

    public DownloadEvent copy(DownloadState downloadState) {
        return new DownloadEvent(this.eventId, downloadState);
    }
}
