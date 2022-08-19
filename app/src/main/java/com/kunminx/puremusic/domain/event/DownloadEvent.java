package com.kunminx.puremusic.domain.event;

import com.kunminx.architecture.domain.event.Event;
import com.kunminx.puremusic.data.bean.DownloadState;

/**
 * Create by KunMinX at 2022/7/4
 */
public class DownloadEvent extends Event<DownloadEvent.Param, DownloadEvent.Result> {
    public final static int EVENT_DOWNLOAD = 1;
    public final static int EVENT_DOWNLOAD_GLOBAL = 2;

    public DownloadEvent(int eventId) {
        this.eventId = eventId;
        this.param = new Param();
        this.result = new Result();
    }

    public static class Param {
    }

    public static class Result {
        public DownloadState downloadState = new DownloadState();
    }
}
