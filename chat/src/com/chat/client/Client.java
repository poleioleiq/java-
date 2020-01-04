package com.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

//客户端登录界面类
public class Client extends JFrame {
    private JButton jButton1;  //登录按钮

    private JButton jButton2; // 退出按钮

    private JLabel jLabel1;  //用户名

    private JLabel jLabel2;  //服务器ip

    private JLabel jLabel3;  //端口号

    private JPanel jPanel;  //用户登录jpane

    private JTextField username;  //用户名填写界面

    private JTextField hostAddress;  //主机号ip填写界面

    private JTextField port;  //端口号填写界面

    public Client(String name) {//构造器
        super(name);//继承方法   ，设置 title

        initComponents(); // initialize UI
    }

    //初始化
    private void initComponents() {
        jPanel = new JPanel();

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        username = new JTextField(15);
        hostAddress = new JTextField(15);
        port = new JTextField(15);

        jButton1 = new JButton();
        jButton2 = new JButton();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭
        this.setAlwaysOnTop(true);//设置界面置顶
        this.setResizable(false);  //设置界面size不可由用户改变

        jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("用户登录"));//设置jpane的title
        //set相应的Text
        jLabel1.setText("用户名");
        jLabel2.setText("服务器");
        jLabel3.setText("端口号");

        jButton1.setText("登录");
        jButton2.setText("退出");
        //给button添加一个匿名类监听
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.this.login(e);
            }//匿名类使用外部类cient的方法
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        username.setText("请输入你的用户名");
        hostAddress.setText("127.0.0.1");
        port.setText("5050");
        hostAddress.setEditable(false);  //设置主机号不可由用户编辑
        port.setEditable(false);   //设置端口号不可由用户编辑

        jPanel.add(jLabel1);
        jPanel.add(username);
        jPanel.add(jLabel2);
        jPanel.add(hostAddress);
        jPanel.add(jLabel3);
        jPanel.add(port);

        jPanel.add(jButton1);
        jPanel.add(jButton2);

        this.getContentPane().add(jPanel);

        this.setSize(250, 200);
        this.setLocation(550, 300);
        this.setVisible(true);
    }

    //登录方法
    private void login(ActionEvent event) {
        String username = this.username.getText();//返回用户名
        String hostAddress = this.hostAddress.getText();//返回服务器主机号
        String port = this.port.getText();//返回端口号

        ClientConnection clientConnection = new ClientConnection(this,
                hostAddress, Integer.parseInt(port), username);  //构造方法，初始化对象

        if (clientConnection.login()) {
            clientConnection.start();                            //登陆成功，开启新的线程
        } else {
            JOptionPane.showMessageDialog(this, "用户名重复或服务器未运行！", "错误",
                    JOptionPane.INFORMATION_MESSAGE);   //jOPtionpane类弹窗方法
        }
    }

    public static void main(String[] args) {          //主函数
        new Client("用户登录");
    }//客户端界面 启动！！

}
