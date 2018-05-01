package com.hibaobo.browser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.io.*;
import java.util.*;

class ViewSourceFrame extends JFrame implements ActionListener{
    JPanel contentPane;
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    Border border1;

    JButton closebutton = new JButton();
    JButton savebutton = new JButton();
    JScrollPane jScrollPane1 = new JScrollPane();
    JTextArea jTextArea1 = new JTextArea();

    String htmlSource;

    /**
     * 构造函数，初始化图形界面
     */
    public ViewSourceFrame(String htmlSource){
        this.htmlSource = htmlSource;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setSize(new Dimension(600,500));
        setTitle("源代码");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BorderLayout());

        panel2.setLayout(new FlowLayout());

        savebutton.setText("保存");
        closebutton.setText("退出");

        closebutton.addActionListener(this);
        savebutton.addActionListener(this);

        jScrollPane1.getViewport().add(jTextArea1,null);
        border1 = BorderFactory.createEmptyBorder(4,4,4,4);
        panel1.setLayout(new BorderLayout());
        panel1.setBorder(border1);
        panel1.add(jScrollPane1,BorderLayout.CENTER);
        contentPane.add(panel1,BorderLayout.CENTER);

        panel2.add(savebutton);
        panel2.add(closebutton);

        contentPane.add(panel2,BorderLayout.SOUTH);
        this.jTextArea1.setEditable(true);
        this.jTextArea1.setText(this.htmlSource);
        this.jTextArea1.setCaretPosition(0);
    }

    /**
     * 实现监听器接口的actionPerformed函数
     */
    public void actionPerformed(ActionEvent e){
        String url = "";
        if(e.getSource() == closebutton){
            dispose();
        }else if(e.getSource() == savebutton){
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(ViewSourceFrame.this);
            File saveFile = fileChooser.getSelectedFile();
            try{
                FileWriter writer = new FileWriter(saveFile);
                writer.write(jTextArea1.getText());
                writer.close();
            }catch (IOException ex){
                System.out.println("保存失败");
            }
        }
    }

}
