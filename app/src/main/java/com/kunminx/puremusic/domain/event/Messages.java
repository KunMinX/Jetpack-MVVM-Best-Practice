package com.kunminx.puremusic.domain.event;

/**
 * Create by KunMinX at 2022/7/4
 */
public class Messages {
    public final static int EVENT_CLOSE_SLIDE_PANEL_IF_EXPANDED = 1;
    public final static int EVENT_CLOSE_ACTIVITY_IF_ALLOWED = 2;
    public final static int EVENT_OPEN_DRAWER = 3;
    public final static int EVENT_ADD_SLIDE_LISTENER = 4;

    public final int eventId;

    public Messages(int eventId) {
        this.eventId = eventId;
    }
}
