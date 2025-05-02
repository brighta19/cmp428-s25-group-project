package org.example;

import javax.swing.*;
import java.applet.Applet;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Knight's Tale");
        Applet applet = new KnightsTaleEntrance();

        applet.init();  // Calls the init method of the Applet
        applet.start(); // Optional, good practice

        frame.setContentPane(applet);
        frame.setSize(800, 600); // Adjust based on your image
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}