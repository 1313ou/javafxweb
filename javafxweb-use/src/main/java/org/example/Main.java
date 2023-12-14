package org.example;

import com.bbou.web.SwingBrowser;

import javax.swing.*;

public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                SwingBrowser browser = new SwingBrowser();
                browser.setVisible(true);
                browser.loadURL(args[0]);
            }
        });
    }
}
