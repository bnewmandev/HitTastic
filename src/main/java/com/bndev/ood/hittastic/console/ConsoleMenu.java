package com.bndev.ood.hittastic.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu implements IAction {
    String name;
    String prompt;
    boolean isRoot;

    private boolean isLooping = true;
    public LinkedHashMap<String, Runnable> actionsMap = new LinkedHashMap<>();

    public ConsoleMenu(String name, String prompt, String backText, String backMsg, Runnable cleanup) {
        this.name = name;
        this.prompt = prompt;
        this.addBackAction(backText, backMsg, cleanup);
    }

    public ConsoleMenu(String name, String prompt, boolean isRoot) {
        this.name = name;
        this.prompt = prompt;
        this.isRoot = isRoot;
        this.addBackAction();
    }

    public ConsoleMenu(String name, String prompt) {
        this.name = name;
        this.prompt = prompt;
        this.isRoot = false;
        this.addBackAction();
    }


    void addBackAction() {
        String backText = this.isRoot ? "Exit" : "Go Back";
        String backMsg = this.isRoot ? "Closing application..." : "Returning to previous menu...";
        this.addAction(new Action(backText, () -> {
            this.isLooping = false;
            System.out.println(backMsg);
        }));
    }

    void addBackAction(String backText, String backMsg, Runnable cleanup) {
        this.addAction(new Action(backText, () -> {
            this.isLooping = false;
            System.out.println(backMsg);
            cleanup.run();
        }));
    }


    public void addAction(Action action) {
        this.actionsMap.put(action.name, action.callback);
    }

    public void addAction(String name, Runnable callback) {
        this.actionsMap.put(name, new Action(name, callback));
    }

    public void removeAction(String name) {
        this.actionsMap.remove(name);
    }

    String genMenuString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        List<String> actionKeys = new ArrayList<>(actionsMap.keySet());
        for (int i = 0; i < actionKeys.size(); i++) {
            sb.append(String.format("%d: %s%n", i, actionKeys.get(i)));
        }
        return sb.toString();
    }

    void execute(int menuNumber) {
        if (menuNumber < 0 || menuNumber >= actionsMap.size()) {
            System.out.println("Menu input out of range");
        } else {
            List<Runnable> actions = new ArrayList<>(actionsMap.values());
            actions.get(menuNumber).run();
        }
    }

    public void stopMenu() {
        this.isLooping = false;
    }

    public void run() {
        this.isLooping = true;
        Scanner scanner = new Scanner(System.in);
        while (this.isLooping) {
            System.out.println("\n");
            System.out.println(this.genMenuString());
            System.out.print(this.prompt);
            if (!scanner.hasNextInt()) {
                waitForReturn("Please enter a number");
                scanner.next();
                continue;
            }
            int actionNumber = scanner.nextInt();
            this.execute(actionNumber);
        }
    }

    public static void waitForReturn(String msg) {
        System.out.println(msg);
        System.out.println("Press RETURN to continue");
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
