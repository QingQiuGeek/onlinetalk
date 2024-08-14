package main;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import object.Message;
import object.User;


/**
 * 客户端登录界面
 * 
 */

public class Login extends JFrame implements ActionListener {

	// 定义北部组件
	JLabel jbl1;
	// 定义中部组件
	// 中部有2个JPanel，由一个选项卡管理
	JTabbedPane jtp;
	JPanel jp2, jp3;
	JLabel jp2_jbl1, jp2_jbl2,  jp3_jbl1, jp3_jbl2;
	JTextField jp2_jtf, jp3_jtf;
	JPasswordField jp2_jpf, jp3_jpf;
	// 定义南部组件
	JPanel jp1;
	JButton jp1_jb1, jp1_jb2;

	public static void main(String[] args) {
		new Login();
	}

	public Login() {
		// 北部
		jbl1 = new JLabel(new ImageIcon("client/src/res/tx.png"));// 可以放图片
		// 南部
		jp1 = new JPanel();
		jp1_jb1 = new JButton("登录");
		jp1_jb1.addActionListener(this);
		jp1_jb2 = new JButton("取消");
		jp1_jb2.addActionListener(this);
		// 把三个按钮放到JP1里
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);
		// 中部	
		jp2 = new JPanel(new GridLayout(3, 3));//设置好一个3X3的布局
		jp2_jbl1 = new JLabel("昵称", JLabel.CENTER);//第一个参数为显示的文字，第二个为居中
		jp2_jbl2 = new JLabel("密码", JLabel.CENTER);
		jp2_jtf = new JTextField();
		jp2_jpf = new JPasswordField();

		jp3 = new JPanel(new GridLayout(3, 3));

		jp3_jbl1 = new JLabel("Name", JLabel.CENTER);
		jp3_jbl2 = new JLabel("QQ密码", JLabel.CENTER);
		jp3_jtf = new JTextField();
		jp3_jpf = new JPasswordField();
		// 把控件按照顺序加入到jp2中
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		// 把控件按照顺序加入到jp3中
		jp3.add(jp3_jbl1);
		jp3.add(jp3_jpf);
		jtp = new JTabbedPane();
		jtp.add("账号登录", jp2);
		jtp.add("使用指南", jp3);

		// 把JP1放到南部
		this.add(jp1, "South");
		this.add(jtp, "Center");
		this.add(jbl1, "North");
		this.setSize(350, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	//以下是检查登录部分
	public boolean Check(Object o) {
		Socket s;
		 boolean b = false;
			try {
				s = new Socket("127.0.0.1", 1000);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(o);
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				try {
					Message ms = (Message) ois.readObject();
					// 这里是验证用户登录的地方
					if (ms.getMsType().equals("1")) {
						// 创建一个该QQ和服务器连接的通讯线程
						ClientToServerThread ccst = new ClientToServerThread(s);
						// 启动该通信线程
						ccst.start();
						//将该线程加入ClientToServerThread中
						ClientToServerThread.addClientToServerThread(((User)o).getUserId(), ccst);
						b = true;
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}
			return b;
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jp1_jb1){
		    User u = new User();//新建一个用户，存储用户ID和密码，以便检查
			u.setUserId(jp2_jtf.getText());//trim() 方法用于删除字符串的头尾空白符。
			u.setPassword(new String(jp2_jpf.getPassword()));
			//如果成立则新建一个FriendList类的界面，并且关闭该界面
			if(Check(u))
			{
				//发送一个要求返回在线好友的请求包
				try {
					//把创建好友列表提前
					FriendList friendList = new FriendList(u.getUserId());
					FriendList.addFriendList(u.getUserId(), friendList);
					//发送一个要求返回在线好友的请求包
					ObjectOutputStream oos = new ObjectOutputStream(ClientToServerThread.getClientToServerThread(u.getUserId()).getS().getOutputStream());
				Message m = new Message();
				m.setMsType(MessageType.message_get_onLineFriend);
				//指明我要的是这个QQ号的好友列表
				m.setSender(u.getUserId());
				oos.writeObject(m);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.dispose();
			}else{
				//验证错误，跳出提示
				JOptionPane.showMessageDialog(this, "用户名或者密码错误");
			}
		}else if(e.getSource()==jp1_jb2) {
			this.dispose();
		}
		
	}
}
