/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nipun.dbms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

/**
 *
 * @author Nipun
 */
public class Console {

    private static Console console = new Console();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Console() {
    }

    public static Console getInstance() {
        return console;
    }

    public static void print(String msg) {
        Date date = new Date();
        System.out.println(sdf.format(date) + " : " + msg);
    }

    public static void createConsole(JTextArea textArea) {
        OutputStream os = new TextAreaOutputStream(textArea);
        System.setOut(new PrintStream(os, true));

        textArea.setEditable(false);
    }
}

class TextAreaOutputStream extends OutputStream {

    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private final JTextArea consoleOutput;

    public TextAreaOutputStream(JTextArea consoleOutput) {
        super();
        this.consoleOutput = consoleOutput;
    }

    @Override
    public void flush() throws IOException {
        consoleOutput.append(buf.toString("UTF-8"));
        buf.reset();
    }

    @Override
    public void write(int b) throws IOException {
        buf.write(b);
    }
}
