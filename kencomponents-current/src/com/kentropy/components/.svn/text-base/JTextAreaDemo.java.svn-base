package com.kentropy.components;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JTextAreaDemo extends JFrame {

  String davidMessage = "David says, \"\u05E9\u05DC\u05D5\u05DD \u05E2\u05D5\u05DC\u05DD\" \n";
  String andyMessage = "Andy also says, \"\u05E9\u05DC\u05D5\u05DD \u05E2\u05D5\u05DC\u05DD\"";

  public JTextAreaDemo() {
    super("Narrative");
    this.setBounds(10,10,800,600);
    GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font font = new Font("LucidaSans", Font.PLAIN, 40);
    JTextArea textArea = new JTextArea(davidMessage + andyMessage);
    textArea.setFont(font);
    textArea.setWrapStyleWord(true);
    textArea.setLineWrap(true);
    this.getContentPane().setBounds(10,10,800,600);
    this.getContentPane().add(textArea);
    textArea.setBounds(10,10,800,600);
    textArea.show();
  }

  public static void main(String[] args) {
    JFrame frame = new JTextAreaDemo();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {System.exit(0);}
    });

    //frame.pack();
    frame.setVisible(true);
    frame.setSize( 800, 600);
  }
}
