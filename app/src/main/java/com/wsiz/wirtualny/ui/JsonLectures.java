package com.wsiz.wirtualny.ui;

public class JsonLectures {
    long przedmiotid;
    String nazwa;

    public JsonLectures(long przedmiotid, String nazwa) {
        this.przedmiotid = przedmiotid;
        this.nazwa = nazwa;
    }

    public long getPrzedmiotid() {
        return przedmiotid;
    }

    public String getNazwa() {
        return nazwa;
    }
}
