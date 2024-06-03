package com.fongmi.android.tv.bean;

public class Msg {
    private int type;
    private Object obj;

    public Msg(){}
    public Msg(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
