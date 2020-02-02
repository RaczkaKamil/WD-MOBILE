package com.wsiz.wirtualny.ui;

public class UiCounter {
    private boolean end = false;

    public void startCounting() throws InterruptedException {
        Thread.sleep(300);
        this.end=true;
    }

    public boolean getValue() {
        return end;
    }
}