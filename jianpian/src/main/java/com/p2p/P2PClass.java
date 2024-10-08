package com.p2p;

import com.github.catvod.utils.Path;

public class P2PClass {

    public int port;

    public P2PClass() {
        System.loadLibrary("jpa");
        this.port = P2Pdoxstarthttpd("TEST3E63BAAECDAA79BEAA91853490A69F08".getBytes(), Path.jpa().getAbsolutePath().getBytes());
    }

    public int P2Pdoxstarthttpd(byte[] bArr, byte[] bArr2) {
        return doxstarthttpd(bArr, bArr2);
    }

    public void P2Pdoxendhttpd() {
        doxendhttpd();
    }

    public void P2Pdoxstart(byte[] bArr) {
        doxstart(bArr);
    }

    public long P2Pdownload(byte[] bArr) {
        return (long) doxstart(bArr);
    }

    public long P2Pgetdownsize(int var1) {
        return getdownsize(var1);
    }

    public long P2Pgetfilesize(int var1) {
        return getfilesize(var1);
    }

    public long P2Pgetspeed(int var1) {
        return getspeed(var1);
    }


    public void P2Pdoxadd(byte[] bArr) {
        doxadd(bArr);
    }

    public void P2Pdoxpause(byte[] bArr) {
        doxpause(bArr);
    }

    public void P2Pdoxdel(byte[] bArr) {
        doxdel(bArr);
    }

    private native int doxstarthttpd(byte[] bArr, byte[] bArr2);

    private native int doxendhttpd();

    private native int doxstart(byte[] bArr);

    private native int doxadd(byte[] bArr);

    private native int doxpause(byte[] bArr);

    private native int doxdel(byte[] bArr);

    private native long getdownsize(int var1);

    private native long getfilesize(int var1);

    private  native long getspeed(int i);


}