package main;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import object.Message;

/**
 * 聊天界面 
 */
public class Chat extends JFrame implements ActionListener {

	JTextArea jta1,jta2;
	JTextField jtf;
	JButton jb;
	JPanel jp,jp2;
	JScrollPane jsp;
	String ownerId, friendId;
	JButton jb3;//jb3为群聊中的发送

	public Chat(String Type,String ownerId, String friend) {
			this.ownerId = ownerId;
			this.friendId = friend;
			//单人聊天
			if(Type=="1") {
			jta1 = new JTextArea();
			jtf = new JTextField(15);
			jb = new JButton("发送");
			jb.addActionListener(this);
			jp = new JPanel();
			jp2 = new JPanel();
			jsp= new JScrollPane(jta1);
			jp.add(jtf);
			jp.add(jb);
			this.setDefaultCloseOperation(1);
			this.add(jsp,BorderLayout.CENTER);
			this.add(jp,BorderLayout.SOUTH);
			this.setTitle(ownerId + "正在和" + friend + "聊天");
			this.setSize(500, 300);
			this.setVisible(true);
		}else if(Type=="2") {//多人聊天
			this.setTitle(ownerId+"正在参加群聊");
			jta2 = new JTextArea();
			jtf = new JTextField(15);
			jb3 = new JButton("发送");
			jsp= new JScrollPane(jta2);
			jb3.addActionListener(this);
			jp =new JPanel();
			jp.add(jtf);
			jp.add(jb3);
			this.setDefaultCloseOperation(1);
			this.add(jsp,BorderLayout.CENTER);
			this.add(jp,BorderLayout.SOUTH);
			this.setSize(500, 300);
			this.setVisible(true);
		}
		

	}
	
	
//这里是管理Chat的部分
/**************************************************************/
		private static HashMap hm = new HashMap<String,Chat>();
		
		//加入
		public static void addChat(String OIdAndFId,Chat chat){
			hm.put(OIdAndFId, chat);	
		}
		//取出
		public static Chat getChat(String OIdAndFId){
			return (Chat)hm.get(OIdAndFId);
			
		}
/**********************************************************************/
	// 显示消息的方法
	public void showMessage(Message m,int Type) {
		if(Type==1) {
					String info = m.getSendTime()+"  "+m.getSender() + "对" + m.getGetter() + "说:\n" + m.getCon()
					+ "\r\n";
					if (m.getCon() != null)
						this.jta1.append(info);
		}else if(Type==2) {
			String info =m.getSendTime()+"  "+m.getSender()+"说:\n"+m.getCon()+"\n";
			if(m.getCon()!=null) {
				this.jta2.append(info);
			}
		}

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) {
			// 获取信息
			Message m = new Message();
			m.setSender(this.ownerId);
			m.setGetter(this.friendId);
			m.setCon(jtf.getText());
			m.setSendTime(new Date().toString());
			m.setMsType(MessageType.message_comm_mes);
			//自己显示
			showMessage(m,1);
			// 发送给服务器

			try {
				ObjectOutputStream oos = new ObjectOutputStream(ClientToServerThread.getClientToServerThread(ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jtf.setText("");
		}
		if(e.getSource()==jb3) {
			// 获取信息
			Message m = new Message();
			m.setSender(this.ownerId);
			m.setGetter("all");
			m.setCon(jtf.getText());
			m.setSendTime(new Date().toString());
			m.setMsType(MessageType.message_sendtoall);
			// 发送给服务器
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ClientToServerThread.getClientToServerThread(ownerId).getS().getOutputStream());
				oos.writeObject(m);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			jtf.setText("");
			
			
		}
	}

}
