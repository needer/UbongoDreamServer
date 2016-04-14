package com.ubongo.player;


import java.io.PrintWriter;

public class Player {
    private PrintWriter out;
    private String name;

    public Player(String name, PrintWriter out) {
        this.out = out;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getName() {
        return name;
    }
}
