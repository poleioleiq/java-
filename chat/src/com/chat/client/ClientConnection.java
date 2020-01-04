package com.chat.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;

import com.chat.util.CharacterUtil;
import com.chat.util.XMLUtil;

//clientconnection {login() ,  sendmessage(), run() }
//
public class ClientConnection extends Thread//继承多线程类
{
    private String hostAddress;                    //主机地址

    private int port;                                //端口号

    private String username;                        //用户名

    private Client client;                            //服务器gui对象

    private Socket socket;                        //socket对象

    private InputStream is;                        //socket输入流

    private OutputStream os;                        //socket输出流

    private ChatClient chatClient;                  //聊天室类

    //clientconnection构造方法
    public ClientConnection(Client client, String hostAddress, int port, String username) {
        this.client = client;
        this.hostAddress = hostAddress;
        this.port = port;
        this.username = username;

        //连接服务器
        this.connect2Server();
    }

    // 连接服务器，由构造方法调用
    private void connect2Server() {
        try {
            this.socket = new Socket(this.hostAddress, this.port);

            this.is = this.socket.getInputStream();    //输出流get服务器输入流
            this.os = this.socket.getOutputStream();   //输入流get服务器输出流
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 用户登录，向服务器端传送用户名
    // 返回true表示登录成功
    // 返回false表示登录失败
    public boolean login() {
        try {
            String xml = XMLUtil.constructLoginXML(this.username);                //根据username，构造xml信息
            System.out.println(xml.toString());                                     //控制台输出xml信息
            os.write(xml.getBytes());
            // 向服务器端发送用户的登录信息（其中包含了用户名）getbytes()返回一个操作系统默认的字节数组

            byte[] buf = new byte[5000];
            int length = is.read(buf);
            // 读取服务器端的响应结果，判断用户是否登录成功

            String loginResultXML = new String(buf, 0, length);

            String loginResult = XMLUtil.extractLoginResult(loginResultXML);

            // 登录成功
            if ("success".equals(loginResult))      //对应serverconnection  中的islogin   如果没有重复用户名就改成true并且返回true  否则返回false
            {
                //打开聊天室主窗口
                this.chatClient = new ChatClient(this);     //启动聊天室界面

                this.client.setVisible(false);                        //同时设置登陆界面为不可见

                return true;                                       //返回true
            }
            // 登录失败
            else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    //客户端向服务器发送信息
    public void sendMessage(String message, String type) {
        try {
            int t = Integer.parseInt(type);

            String xml = null;

            //客户端向服务器端发送聊天数据
            if (CharacterUtil.CLIENT_MESSAGE == t)                                //符合t==2的条件时
            {
                xml = XMLUtil.constructMessageXML(this.username, message);      //构造{type---2，user---name，content----message}----->返回document。asxml()
            }
            //客户端向服务器端发送关闭窗口的数据
            else if (CharacterUtil.CLOSE_CLIENT_WINDOW == t) {
                xml = XMLUtil.constructCloseClientWindowXML(this.username);     //构造{type--5,user---uesrname}--->返回document。asxml()
            }

            //向服务器端发送数据
            this.os.write(xml.getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //从 服务端读取信息  ->   读取到信息解析出type ->
    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[5000];
                int length = is.read(buf);

                String xml = new String(buf, 0, length);

                int type = Integer.parseInt(XMLUtil.extractType(xml));            //从xml中解析出type值    xml格式{type-----。}

                //在线用户列表
                if (type == CharacterUtil.USER_LIST)                                ///4
                {
                    List<String> list = XMLUtil.extractUserList(xml);

                    String users = "";

                    for (String user : list) {
                        users += user + "\n";
                    }

                    this.chatClient.getJTextArea2().setText(users);          //把列举表信息添加到chatclient中的在线用户TEXTAREA之中
                }
                // 服务器端发来的聊天数据
                else if (type == CharacterUtil.SERVER_MESSAGE)     //3
                {
                    String content = XMLUtil.extractContent(xml);

                    this.chatClient.getJTextArea1().append(content + "\n");      //添加到聊天界面
                }
                // 关闭服务器端窗口
                else if (type == CharacterUtil.CLOSE_SERVER_WINDOW)   ///6
                {
                    JOptionPane.showMessageDialog(this.chatClient, "服务器端已关闭，程序将退出！", "信息", JOptionPane.INFORMATION_MESSAGE);

                    System.exit(0); //客户端退出
                }
                // 服务器端确认关闭客户端窗口
                else if (type == CharacterUtil.CLOSE_CLIENT_WINDOW_CONFIRMATION)             //7 从服务线程类message{type==7}  则 执行下面的操作
                {
                    try {
                        this.getSocket().getInputStream().close();
                        this.getSocket().getOutputStream().close();
                        this.getSocket().close();
                    } catch (Exception ex) {

                    } finally {
                        System.exit(0);//退出客户端程序
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}	
