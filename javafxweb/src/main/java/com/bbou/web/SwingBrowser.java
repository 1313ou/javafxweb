package com.bbou.web;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

import static javafx.concurrent.Worker.State.FAILED;

public class SwingBrowser extends JFrame
{
    // S W I N G

    private final BrowserJPane panel = new BrowserJPane();

    public SwingBrowser()
    {
        super();
        initComponents();
    }

    private void initComponents()
    {
        panel.setTitleListener(this::setTitle);
        getContentPane().add(panel);

        setPreferredSize(new Dimension(1024, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    public void loadURL(final String url)
    {
        panel.loadURL(url);
    }
}
