package com.bndev.ood.hittastic.console;

public class Action implements IAction {
    public String name;
    public Runnable callback;

    public Action(String name, Runnable callback) {
        this.name = name;
        this.callback = callback;
    }

    public void run() {
        callback.run();
    }
}
