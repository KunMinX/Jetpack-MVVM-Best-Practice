package com.kunminx.puremusic.domain.event;

import com.kunminx.architecture.domain.event.Event;

/**
 * Create by KunMinX at 2022/7/4
 */
public class Messages extends Event<Messages.Param, Messages.Result> {
    public final static int EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED = 1;
    public final static int EVENT_CLOSE_ACTIVITY_IF_ALLOWED = 2;
    public final static int EVENT_OPEN_DRAWER = 3;
    public final static int EVENT_ADD_SLIDE_LISTENER = 4;

    public Messages(int eventId) {
        this.eventId = eventId;
        this.param = new Param();
        this.result = new Result();
    }

    public static class Param {
    }

    public static class Result {
    }
}
