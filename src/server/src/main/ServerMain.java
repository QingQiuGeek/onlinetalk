package main;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import object.Message;

/**
 * 服务器界面
 */
public class ServerMain extends JFrame implements ActionListener,MouseListener {

	JPanel jp1,jp2;
	JButton jb1, jb2, jb3;
	static JTextArea jta;
	JTextArea jta2;
	JScrollPane jsp,jsp2;//jsp2为用户列表 jsp3为文本域
	JTabbedPane jtp;
	static JLabel []jbls;
	

	public static void main(String[] args) {
		 new ServerMain();
	}

	public ServerMain() {
		jp1 = new JPanel();
		jta = new JTextArea();
		jb1 = new JButton("启动服务器");
		jb2 = new JButton("关闭服务器");
		jb3 = new JButton("显示用户列表");
		jsp = new JScrollPane(jta);
		jp2 = new JPanel(new GridLayout(50,1));
		jsp2 = new JScrollPane(jp2);
		jp2.setSize(50,60);
		jtp = new JTabbedPane();
		jp2.setSize(20,20);
		
		jbls = new JLabel[50];
		for(int i = 0;i<jbls.length;i++){
			jbls[i]= new JLabel(i+1+"", new ImageIcon("server/src/res/tx.png"),JLabel.LEFT);
			jbls[i].setEnabled(false);
			jbls[i].addMouseListener(this);
			jp2.add(jbls[i]);
			
		}

		jp1.add(jb1,"West");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		jp1.add(jb2);
		jp1.add(jb3,"East"); 

		this.add(jp1, "North");
		jtp.add("服务器日志",jsp);
		jtp.add("管理用户",jsp2);
		this.add(jtp,"Center");
		this.setDefaultCloseOperation(3);
		this.setSize(400, 400);
		this.setTitle("服务器端");
		this.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		Server server = new Server();
		if (e.getSource() == jb1) {
			jta.append("服务已开启\n");

			server.start();
		}
		if (e.getSource() == jb2) {
			server.stop();
			jta.append("服务器已关闭\n");
		}
		if (e.getSource() == jb3) {
			printUser();
			jta.append("用户显示完毕\n");
		}

	}
//printuser用于显示所有在线用户
	public void printUser() {
		String[] s = ServerToClientThread.getAllOnLineUserId().split(" ");
		jta.append("目前在线用户有：\n");
		for (int i = 0; i < s.length; i++) {
			jta.append(s[i]+"\n");
		}
		jta.append("共有"+s.length+"个用户\n");
	}
	
	public static void updateFriend_add(String iam) {
		jbls[Integer.parseInt(iam)-1].setEnabled(true);
	}
	
	public static void updateFriend_delete(String iam) {
		jbls[Integer.parseInt(iam)-1].setEnabled(false);
	}
	//此方法用于显示服务器日志
	public static void print(String m) {
		jta.append(m);
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//响应用户双击事件得到好友编号
		if(e.getClickCount()==2){}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		String client_User=((JLabel) e.getSource()).getText();
		jl.setForeground(Color.black);
		int result=JOptionPane.showConfirmDialog(this,"是否踢他下线","选中用户"+
		((JLabel)e.getSource()).getText(),JOptionPane.YES_NO_CANCEL_OPTION);
		if(result==0) {
			//这里是强制用户下线功能
			//以下是删除客户端线程
			jta.append("您已强制"+client_User+"下线\n");
			Message m=new Message();
			m.setSender(client_User);
			m.setMsType(MessageType.message_delete_client);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ServerToClientThread.getClientThread(client_User).s.getOutputStream());
				oos.writeObject(m);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//以下是删除服务端线程
			ServerToClientThread.notifyOther(client_User,2);
			ServerToClientThread ST=ServerToClientThread.getClientThread(client_User);
			ServerToClientThread.deleteClientThtead(client_User);
			
			ST.stop();
		}
		//JOptionPane.showMessageDialog(this,"选中用户"+((JLabel)e.getSource()).getText()+"\n");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		jl.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		jl.setForeground(Color.black);
	}
	
}

