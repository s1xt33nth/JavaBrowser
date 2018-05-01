package com.hibaobo.browser;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;


public class WebBrowser extends JFrame implements HyperlinkListener,ActionListener{
    JToolBar bar = new JToolBar();

    JTextField jurl = new JTextField(60);
    JEditorPane jEditorPane1 = new JEditorPane();
    JScrollPane scrollPane = new JScrollPane(jEditorPane1);

    JFileChooser chooser = new JFileChooser();
    JFileChooser chooser1 = new JFileChooser();
    String htmlSource;
    JWindow window = new JWindow(WebBrowser.this);

    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu fileMenu = new JMenu("文件");
    JMenuItem saveAsItem = new JMenuItem("另存为...");
    JMenuItem exitItem = new JMenuItem("退出");
    JMenu editMenu = new JMenu("编辑");
    JMenuItem backItem = new JMenuItem("后退");
    JMenuItem forwardItem = new JMenuItem("前进");
    JMenu viewMenu = new JMenu("视图");
    JMenuItem fullscreenItem = new JMenuItem("全屏");
    JMenuItem sourceItem = new JMenuItem("查看源码");
    JMenuItem reloadItem = new JMenuItem("刷新");

    JToolBar toolBar = new JToolBar();
    JButton picSave = new JButton("另存为");
    JButton picBack = new JButton("后退");
    JButton picForward = new JButton("前进");
    JButton picView = new JButton("查看源代码");
    JButton picExit = new JButton("退出");

    JLabel label = new JLabel("地址");
    JButton button = new JButton("转向");

    JButton button2 = new JButton("窗口还原");
    Toolkit toolkit = Toolkit.getDefaultToolkit();

    Box adress = Box.createHorizontalBox();

    private ArrayList history = new ArrayList();
    private int historyIndex;

    public WebBrowser(){
        setTitle("JavaBrowser");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jEditorPane1.addHyperlinkListener(this);

        Container contentPane = getContentPane();

        scrollPane.setPreferredSize(new Dimension(100,500));
        contentPane.add(scrollPane,BorderLayout.SOUTH);

        toolBar.add(picSave);
        toolBar.addSeparator();
        toolBar.add(picBack);
        toolBar.add(picForward);
        toolBar.addSeparator();
        toolBar.add(picView);
        toolBar.addSeparator();
        toolBar.add(picExit);

        contentPane.add(bar,BorderLayout.CENTER);
        contentPane.add(toolBar,BorderLayout.NORTH);

        viewMenu.add(fullscreenItem);
        viewMenu.add(sourceItem);
        viewMenu.addSeparator();
        viewMenu.add(reloadItem);

        jMenuBar1.add(fileMenu);
        jMenuBar1.add(editMenu);
        jMenuBar1.add(viewMenu);
        setJMenuBar(jMenuBar1);

        adress.add(label);
        adress.add(jurl);
        adress.add(button);
        bar.add(adress);

        saveAsItem.addActionListener(this);
        picSave.addActionListener(this);
        exitItem.addActionListener(this);
        picExit.addActionListener(this);
        backItem.addActionListener(this);
        picBack.addActionListener(this);
        forwardItem.addActionListener(this);
        picForward.addActionListener(this);
        fullscreenItem.addActionListener(this);
        sourceItem.addActionListener(this);
        picView.addActionListener(this);
        reloadItem.addActionListener(this);
        button.addActionListener(this);
        jurl.addActionListener(this);
    }

    /**
     * 实现监听器接口的actionPerformed函数
     */
    public void actionPerformed(ActionEvent e){
        String url = "";
        if (e.getSource() == button ){
            url = jurl.getText();
            if (url.length()>0 && url.startsWith("https://")){
                try{
                    jEditorPane1.setPage(url);
                    history.add(url);
                    historyIndex=history.size()-1;
                    jEditorPane1.revalidate();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(WebBrowser.this,"无法打开，请重新输入","JavaBrowser",JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (url.length()>0 && !url.startsWith("https://")){
                url="https://"+url;
                try{
                    jEditorPane1.setPage(url);
                    history.add(url);
                    historyIndex=history.size()-1;
                    jEditorPane1.revalidate();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(WebBrowser.this,"无法打开，请重新尝试","JavaBrowser",JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (url.length() == 0){
                JOptionPane.showMessageDialog(WebBrowser.this,"重新输入链接","JavaBrowser",JOptionPane.ERROR_MESSAGE);
            }
        }
        //另存为
        else if (e.getSource() == picSave || e.getSource() == saveAsItem){
            url = jurl.getText().toString().trim();
            if(url.length()>0 && !url.startsWith("https://")){
                url="https://"+url;
            }
            if(!url.equals("")){
                saveFile(url);
            }
            else{
                JOptionPane.showMessageDialog(WebBrowser.this,"请输入链接地址","JavaBrowser",JOptionPane.ERROR_MESSAGE);
            }
        }
        //退出
        else if (e.getSource() == exitItem || e.getSource() == picExit){
            System.exit(0);
        }
        //后退
        else if (e.getSource() == backItem || e.getSource() == picBack){
            historyIndex--;
            if(historyIndex<0){
                historyIndex = 0;
            }
            url = jurl.getText();
            try{
                url = (String)history.get(historyIndex);
                jEditorPane1.setPage(url);
                jurl.setText(url.toString());
                jEditorPane1.revalidate();
            }catch (Exception ex){

            }
        }
        //前进
        else if (e.getSource() == forwardItem || e.getSource() == picForward){
            historyIndex++;
            if(historyIndex >= history.size()){
                historyIndex = history.size()-1;
            }
            url = jurl.getText();
            try{
                url = (String)history.get(historyIndex);
                jEditorPane1.setPage(url);
                jurl.setText(url.toString());
                jEditorPane1.revalidate();
            }catch (Exception ex){

            }

        }
        //全屏显示
        else if (e.getSource() == fullscreenItem){
            boolean add_button2=true;
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

            Container content = window.getContentPane();
            content.add(bar,"North");
            content.add(scrollPane,"center");

            if (add_button2==true){
                bar.add(button2);
            }
            button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WebBrowser.this.setEnabled(true);
                    window.remove(bar);
                    window.remove(toolBar);
                    window.remove(scrollPane);
                    window.setVisible(false);

                    scrollPane.setPreferredSize(new Dimension(100,500));
                    getContentPane().add(scrollPane,BorderLayout.SOUTH);
                    getContentPane().add(bar,BorderLayout.CENTER);
                    getContentPane().add(toolBar,BorderLayout.NORTH);
                    bar.remove(button2);
                    pack();
                }
            });
            window.setSize(size);
            window.setVisible(true);
        }
        //查看源文件
        else if (e.getSource() == sourceItem || e.getSource() == picView){
            url = jurl.getText().toString().trim();
            if(url.length()>0 && !url.startsWith("https://")){
                url = "https://" + url;
            }
            if(!url.equals("")){
                getHtmlSource(url);
                ViewSourceFrame vsframe = new ViewSourceFrame(htmlSource);
                vsframe.setBounds(0,0,800,500);
                vsframe.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(WebBrowser.this,"请输入链接地址","JavaBrowser",JOptionPane.ERROR_MESSAGE);
            }
        }
        //刷新
        else if (e.getSource() == reloadItem){
            url = jurl.getText();
            if(url.length()>0 && url.startsWith("https://")){
                try{
                    jEditorPane1.setPage(url);
                    jEditorPane1.revalidate();
                }catch (Exception ex){

                }
            }
            else if (url.length()>0 && !url.startsWith("https://")){
                url = "https://" + url;
                try{
                    jEditorPane1.setPage(url);
                    jEditorPane1.revalidate();
                }catch (Exception ex){

                }
            }

        }
    }

    /**
     * 保存文件
     */
    void saveFile(final String url) {
        //行分隔符
        final String linesep = System.getProperty("line.separator");
        chooser1.setCurrentDirectory(new File("."));
        chooser1.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser1.setDialogTitle("另存为...");
        if (chooser1.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        this.repaint();
        Thread thread = new Thread() {
            public void run() {
                try {
                    java.net.URL source = new URL(url);
                    InputStream in = new BufferedInputStream(source.openStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    File fileName = chooser1.getSelectedFile();
                    FileWriter out = new FileWriter(fileName);
                    BufferedWriter bw = new BufferedWriter(out);
                    String line;
                    while ((line = br.readLine()) != null) {
                        bw.write(line);
                        bw.newLine();
                    }
                    bw.flush();
                    bw.close();
                    out.close();
                    String dMessage = url + "已被保存至" + linesep + fileName.getAbsolutePath();
                    String dTitle = "另存为";
                    int dType = JOptionPane.INFORMATION_MESSAGE;
                    JOptionPane.showMessageDialog((Component) null, dMessage, dTitle, dType);
                } catch (java.net.MalformedURLException muex) {
                    JOptionPane.showMessageDialog((Component) null, muex.toString(), "JavaBrowser", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog((Component) null, ex.toString(), "JavaBrowser", JOptionPane.ERROR_MESSAGE);
                }
            }

        };
        thread.start();
    }

    /**
     * 获得源代码
     */
    void getHtmlSource(String url){
        String linesep,htmlLine;
        linesep = System.getProperty("line.separator");
        htmlLine = "";
        try{
            java.net.URL source = new URL(url);
            InputStream in = new BufferedInputStream(source.openStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ( (htmlLine = br.readLine()) != null){
                htmlSource = htmlSource + htmlLine + linesep;
            }
        }catch (java.net.MalformedURLException muex) {
            JOptionPane.showMessageDialog((Component) null, muex.toString(), "JavaBrowser", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog((Component) null, ex.toString(), "JavaBrowser", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 实现监听器接口的hyperlinkUpdate函数
     */
    public void hyperlinkUpdate(HyperlinkEvent e){
        try{
            if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                jEditorPane1.setPage(e.getURL());
        }catch (Exception ex){
            ex.printStackTrace(System.err);
        }
    }

    /**
     * 生成一个IE对象
     */
    public static void main(String[] args){
        try{
            //可跨平台的默认风格
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch (Exception ex){

        }
        WebBrowser webBrowser = new WebBrowser();
        webBrowser.pack();
        webBrowser.setVisible(true);
    }
}

