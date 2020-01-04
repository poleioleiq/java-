package com.chat.client;

import com.chat.server.Server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//聊天室类 登陆	聊天室需要服务端和客户端创立连接，
//构造方法需要初始化cilentconnection和chatclient界面
//两个监听方法，gui界面关闭需要调用sendmessage()方法，发送chatlient关闭的信息，，2. 发送按钮监听，
public class ChatClient extends javax.swing.JFrame {
    private javax.swing.JButton jButton1;   //发送按钮
    private javax.swing.JButton jButton2;   //清屏按钮
    private javax.swing.JPanel jPanel1;     //聊天室信息jpane
    private javax.swing.JPanel jPanel2;    //在线用户jpane
    private JPanel jPanel3;                //信息发送栏和两个按钮的jpane
    private javax.swing.JScrollPane jScrollPane1;     //添加滚轮界面
    private javax.swing.JScrollPane jScrollPane2;     //添加滚轮
    private javax.swing.JTextArea jTextArea1;         //聊天室信息
    private javax.swing.JTextArea jTextArea2;         //在线用户信息
    private javax.swing.JTextField jTextField;        //信息发送栏

    private ClientConnection clientConnection;        //new 一个客户端连接类的对象


    public ChatClient(ClientConnection clientConnection)    //构造器初始化方法
    {
        this.clientConnection = clientConnection;

        initComponents();   //初始化
    }

    //返回私有变量
    public JTextArea getJTextArea2() {
        return jTextArea2;
    }

    public JTextArea getJTextArea1() {
        return jTextArea1;
    }

    //初始化界面
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();    //聊天室信息Jpane
        jScrollPane1 = new javax.swing.JScrollPane();   //聊天室信息jScrollPane
        jTextArea1 = new javax.swing.JTextArea();       //聊天室信息
        jTextField = new javax.swing.JTextField(20);   //信息发送栏
        jButton1 = new javax.swing.JButton();           //发送按钮
        jButton2 = new javax.swing.JButton();           //清屏按钮
        jPanel2 = new javax.swing.JPanel();             //用户在线Jpane
        jScrollPane2 = new javax.swing.JScrollPane();   //用户在线界面的SCrollpane
        jTextArea2 = new javax.swing.JTextArea();       //在线用户信息

        jPanel3 = new JPanel();                        //信息发送栏和两个按钮的Jpane

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //关闭gui后退出进程
        this.setTitle("聊天室");                           //设置标题“聊天室”
        setResizable(false);                               //设置不可改变尺寸
        jPanel1.setBorder(BorderFactory.createTitledBorder("聊天室信息"));         //设置jpane1标题
        jPanel2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));       //设置jpane2标题
        jTextArea1.setColumns(30);
        jTextArea1.setRows(25);
        //设置行列
        jTextArea2.setColumns(20);
        jTextArea2.setRows(25);
        //设置行列
        this.jTextArea1.setEditable(false);
        this.jTextArea2.setEditable(false);
        //设置两个JtextArea都为不可由用户修改
        jPanel3.add(jTextField);
        jPanel3.add(jButton1);
        jPanel3.add(jButton2);

        jPanel1.setLayout(new BorderLayout());      //设置边界布局
        jPanel1.add(jScrollPane1, BorderLayout.NORTH);
        jPanel1.add(jPanel3, BorderLayout.SOUTH);   //设置jpane3添加到jpane1，north布局

        jPanel2.add(jScrollPane2);                  //jpanel2添加 jSCRollpanel2
        //设置两个jpanel都为置顶状态
        jScrollPane1.setViewportView(jTextArea1);
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("发送");         //设置jbutton1的标题为“发送”
        jButton2.setText("清屏");         //设置jbutton2的标题为“清屏”

        //button1添加匿名类监听是否按下发送按钮
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatClient.this.sendMessage(e);
            }   //如果按了按钮就调用外部类的sendmeaage()方法
        });

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChatClient.this.clear(e);


            }
        });


        //添加window监听，如果gui界面关闭了，就发送"client closed"
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ChatClient.this.clientConnection.sendMessage("client closed", "5");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //容器添加jpanel
        this.setLayout(new FlowLayout());
        this.getContentPane().add(jPanel1);
        this.getContentPane().add(jPanel2);


        this.pack();                                        //自动调节容器装下jpanel
        this.setVisible(true);                              //设置为可见状态
    }


    private void clear(ActionEvent event) {
        this.jTextArea1.setText("");
    }

    //sendmessage方法
    private void sendMessage(ActionEvent event) {
        // 用户聊天的数据
        String message = this.jTextField.getText();
        // 清空聊天数据
        this.jTextField.setText("");
        // 向服务器端发送聊天数据
        this.clientConnection.sendMessage(message, "2");
    }


}
