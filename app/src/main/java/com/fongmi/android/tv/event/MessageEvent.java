package com.fongmi.android.tv.event;

import com.fongmi.android.tv.bean.Msg;

public class MessageEvent {
    Msg message;
    public MessageEvent(Msg message) {
        this.message = message;
    }
    public Msg getMessage(){
        return message;
    }
}
