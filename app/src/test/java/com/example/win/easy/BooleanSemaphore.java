package com.example.win.easy;

public class BooleanSemaphore {
    BooleanSemaphore(boolean ready){setReady(ready);}
    private boolean ready;
    boolean isNotReady(){return !ready;}
    void setReady(boolean ready){this.ready=ready;}
}
