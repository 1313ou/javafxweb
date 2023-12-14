package com.bbou.web;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.function.Consumer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import static javafx.concurrent.Worker.State.FAILED;

public class BrowserJPane extends JPanel
{
    // J A V A F X
    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;

    // S W I N G
    private final JLabel lblStatus = new JLabel();
    private final JButton btnGo = new JButton("Go");
    private final JTextField txtURL = new JTextField();
    private final JProgressBar progressBar = new JProgressBar();

    // I N T E R F A C E

    private Consumer<String> titleListener = null;

    // C O N S T R U C T O R

    public BrowserJPane()
    {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents()
    {
        createScene();

        // listener
        ActionListener al = e -> loadURL(txtURL.getText());
        btnGo.addActionListener(al);
        txtURL.addActionListener(al);

        // top bar
        JPanel topBar = new JPanel(new BorderLayout(5, 0));
        topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        topBar.add(txtURL, BorderLayout.CENTER);
        topBar.add(btnGo, BorderLayout.EAST);

        // progress
        progressBar.setPreferredSize(new Dimension(150, 18));
        progressBar.setStringPainted(true);

        // status bar
        JPanel statusBar = new JPanel(new BorderLayout(5, 0));
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.CENTER);
        statusBar.add(progressBar, BorderLayout.EAST);

        // assemble panel
        add(topBar, BorderLayout.NORTH);
        add(jfxPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    private void createScene()
    {
        Platform.runLater(() -> {
            WebView view = new WebView();
            engine = view.getEngine();
            engine.titleProperty().addListener((observable, oldValue, newValue) -> SwingUtilities.invokeLater(() -> {
                if (titleListener != null) titleListener.accept(newValue);
            }));
            engine.setOnStatusChanged(event -> SwingUtilities.invokeLater(() -> lblStatus.setText(event.getData())));
            engine.locationProperty().addListener((ov, oldValue, newValue) -> SwingUtilities.invokeLater(() -> txtURL.setText(newValue)));
            engine.getLoadWorker().workDoneProperty().addListener((observableValue, oldValue, newValue) -> SwingUtilities.invokeLater(() -> progressBar.setValue(newValue.intValue())));
            engine.getLoadWorker()
                    .exceptionProperty()
                    .addListener((o, old, value) -> {
                        if (engine.getLoadWorker().getState() == FAILED)
                        {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                    BrowserJPane.this,
                                    (value != null) ?
                                            engine.getLocation() + "\n" + value.getMessage() :
                                            engine.getLocation() + "\nUnexpected error.",
                                    "Loading error...",
                                    JOptionPane.ERROR_MESSAGE));
                        }
                    });
            jfxPanel.setScene(new Scene(view));
        });
    }

    // A C C E S S

    public void setTitleListener(Consumer<String> titleConsumer)
    {
        this.titleListener = titleConsumer;
    }

    // L O A D

    public void loadURL(final String url)
    {
        Platform.runLater(() -> {
            String tmp = toURL(url);
            if (tmp == null)
            {
                tmp = toURL("http://" + url);
            }
            engine.load(tmp);
        });
    }

    private static String toURL(String str)
    {
        try
        {
            return new URL(str).toExternalForm();
        }
        catch (MalformedURLException exception)
        {
            return null;
        }
    }
}
