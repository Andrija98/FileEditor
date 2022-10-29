package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PredlogKorisnickogInterfejsa extends JDialog {
    private JPanel contentPane;
    private JButton buttonClose;
    private JButton buttonSave;
    private JButton buttonTopOpen;
    private JButton buttonGetTopSelection;
    private JTextArea textAreaTop;
    private JTextArea textAreaBottom;
    private JTextArea textAreaNew;
    private JButton buttonGetBottomSelection;
    private JButton buttonBottomOpen;

    String directory;

    public PredlogKorisnickogInterfejsa() {
        setContentPane(contentPane);
        setModal(true);
            getRootPane().setDefaultButton(buttonTopOpen);

        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonClose();
            }
        });
        buttonGetBottomSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transferSelectedText(textAreaBottom);
            }
        });
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onButtonSave();
            }
        });

        buttonGetTopSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transferSelectedText(textAreaTop);
            }
        });
        buttonTopOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDirectory(textAreaTop);
            }
        });
        buttonBottomOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDirectory(textAreaBottom);
            }
        });

        // call onCancel() when cross is clicked
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onButtonClose();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void onButtonClose() {
        dispose();
    }
    private void transferSelectedText(JTextArea textArea) {
        String topText = (textArea.getSelectedText());
        textAreaNew.append(topText);
    }
    public void saveFile(String directory, String filename){
        if ((filename == null) || (filename.length() == 0))
            return;
        File file;
        FileWriter out = null;
        try {
            file = new File(directory, filename);
            out = new FileWriter(file);
            textAreaNew.getLineCount();
            String s = textAreaNew.getText();
            out.write(s);
            textAreaNew.setText("");
        }
        catch (IOException e) {
            textAreaNew.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        finally {
            try {
                if (out != null)
                    out.close();
            }
            catch (IOException e) {
            }
        }
    }
    public void loadAndDisplayFile(String directory, String filename, JTextArea textArea) {
        if ((filename == null) || (filename.length() == 0))
            return;
        File file;
        FileReader in = null;
        try {
            file = new File(directory, filename);
            in = new FileReader(file);
            char[] buffer = new char[4096];
            int len;
            textArea.setText("");
            while ((len = in.read(buffer)) != -1) {
                String s = new String(buffer, 0, len);
                textArea.append(s);
            }
            this.setTitle("FileViewer: " + filename);
            textArea.setCaretPosition(0);
        }

        catch (IOException e) {
            textArea.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }

        finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
    private void openDirectory(JTextArea textArea){

        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.LOAD);
        f.setDirectory(directory);
        f.setVisible(true);
        directory = f.getDirectory();
        loadAndDisplayFile(directory, f.getFile(), textArea);
        f.dispose();
    }
    private void onButtonSave() {
        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.SAVE);
        f.setDirectory(directory);
        f.setVisible(true);
        directory = f.getDirectory();
        saveFile(directory, f.getFile());
        f.dispose();

    }



    public static void main(String[] args) {
        PredlogKorisnickogInterfejsa dialog = new PredlogKorisnickogInterfejsa();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
