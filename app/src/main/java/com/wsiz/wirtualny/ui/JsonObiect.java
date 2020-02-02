package com.wsiz.wirtualny.ui;

public class JsonObiect {
    String tytuł;
    long data;

    public String getTytuł() {
        return tytuł;
    }

    public long getData() {
        return data;
    }

    public JsonObiect(String tytuł, long data) {
        this.tytuł = tytuł;
        this.data = data;
    }
}
