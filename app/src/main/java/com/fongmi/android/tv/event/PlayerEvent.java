package com.fongmi.android.tv.event;

import org.greenrobot.eventbus.EventBus;

public class PlayerEvent {

    private final int state;

    public static void prepare() {
        EventBus.getDefault().post(new PlayerEvent(0));
    }

    public static void state(int state) {
        EventBus.getDefault().post(new PlayerEvent(state));
    }

    private PlayerEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
