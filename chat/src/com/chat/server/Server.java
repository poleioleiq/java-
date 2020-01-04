package com.chat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.chat.util.XMLUtil;

//sever's GUI
public class Server extends JFrame {

	private JLabel jLabel1;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JButton jButton;

	private JPanel jPanel1;//上层 jpane

	private JPanel jPanel2; //下层 jpane

	private JScrollPane jScrollPane;  //jTestArea's scroll

	private JTextArea jTextArea;//服务器信息

	private JTextField jTextField;//  端口号	port  填写界面

	private Map<String, ServerMessageThread> map = new HashMap<String, ServerMessageThread>();//服务器线程信息

	public Server(String name)//constracter
		{
			super(name);//public jFrame (String title);

			this.initComponents(); //initialize UI
	}
	//get 方法。
	public Map<String, ServerMessageThread> getMap()
	{
		return map;
	}

	public JLabel getJLabel2()
	{
		return jLabel2;
	}

	public JButton getJButton()
	{
		return jButton;
	}

	public JTextArea getJTextArea()
	{
		return jTextArea;
	}

	public JTextField getJTextField()
	{
		return jTextField;
	}

	public void setJTextField(JTextField textField)
	{
		jTextField = textField;
	}

	private void initComponents()///初始化
	{
		jPanel1 = new JPanel();//上层界面
		jPanel2 = new JPanel();//下层界面

		jLabel1 = new JLabel();
		jLabel2 = new JLabel();
		jLabel3 = new JLabel();

		jTextField = new JTextField(10);  //
		jButton = new JButton();   //  按钮
		jScrollPane = new JScrollPane();  //   滚轮界面
		jTextArea = new JTextArea(); //

		jPanel1.setBorder(BorderFactory.createTitledBorder("服务器信息"));//title
		jPanel1.setBackground(new Color(145,145,145));//	设置背景色
		jPanel2.setBorder(BorderFactory.createTitledBorder("在线用户列表"));  //title
		jPanel2.setBackground(new Color(192,192,192));//???
		jTextField.setText("5050"); //默认设置端口号5050
		jTextField.setEditable(false);  //不允许修改

		jLabel1.setText("服务器状态");  //label1 设置文本“服务器状态”
		jLabel2.setText("停止");  //  label2设置文本  “停止”
		jLabel2.setForeground(new Color(204, 0, 51));
		jLabel3.setText("端口号");  //文本三设置文本端口号

		jButton.setText("启动服务器");

		//Jbutton 添加 匿名类监听
		jButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				Server.this.execute(event);
			}
		});

		this.addWindowListener(new WindowAdapter()//窗口添加一个Windows事件消息，监听服务器是否关闭
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					Collection<ServerMessageThread> cols = Server.this.map.values();

								String messageXML = XMLUtil.constructCloseServerWindowXML();      //返回构造的服务器窗口关闭信息的xml

						for(ServerMessageThread smt : cols)
						{
							smt.sendMessage(messageXML);                                //遍历collection中每个元素，发送给客户端
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					System.exit(0);                  				//退出
				}
			}
		});

//添加到jpane中
		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField);
		jPanel1.add(jButton);

		jTextArea.setEditable(false); //不允许用户手动修改在线用户列表
		jTextArea.setRows(15);//设置行列，可用setbounds()
		jTextArea.setColumns(30);
		jTextArea.setForeground(new Color(0, 51, 204));

		jScrollPane.setViewportView(jTextArea);

		jPanel2.add(jScrollPane);  //将JTextArea放入JScrollPane中

		this.getContentPane().add(jPanel1, BorderLayout.NORTH);//边界布局放入内容界面中
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭界面的时候关闭进程
		this.setAlwaysOnTop(true);//实现窗体置顶
		this.setResizable(false);//设置不可拉伸界面的大小
		this.pack();//调整此窗口的大小,以适合其子组件的首选大小和布局
		this.setLocation(500, 170);
		this.setVisible(true);//设置可见
	}

	private void execute(ActionEvent evt)      //点击按钮就开始解析  端口号和连接服务器
	{
		int port = Integer.parseInt(this.getJTextField().getText());//给port赋值成输入的端口号，

		new ServerConnection(this, port).start();  //连接服务器
	}

	public static void main(String[] args)
	{
		new Server("Chat Server");
	}//    chatServerr  启动

}