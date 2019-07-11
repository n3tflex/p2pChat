package de.hu.p2p;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

//
public class chatWindow {
    private JTextArea chat_display;
    private JTextField chat_input;
    private JTextArea connected_peers;
    private JTextArea debug_display;
    private JButton chat_send;
    private JPanel p2pChatView;
    private PeerManager PM;

    public chatWindow(PeerManager PM) {
        this.PM=PM;
        chat_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String txt = chat_input.getText();
                if(txt != ""){
                    PM.send_all(txt);
                }
            }
        });
    }
    public void test(String[] args) {
        JFrame frame = new JFrame("chatWindow");
        PeerManager ppm = null;
        try {
            PM = new PeerManager(9000);
        } catch(Exception e){
            e.printStackTrace();
        }
        frame.setContentPane(new chatWindow(PM).p2pChatView);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
