package com.example.win.easy;

public class BooleanSemaphore {
    public BooleanSemaphore(boolean ready){setReady(ready);}
    private boolean ready;
    public boolean isNotReady(){return !ready;}
    public boolean isReady(){return ready;}
    public void setReady(boolean ready){this.ready=ready;}
}
