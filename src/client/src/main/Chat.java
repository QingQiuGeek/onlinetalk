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
 * ������� 
 */
public class Chat extends JFrame implements ActionListener {

	JTextArea jta1,jta2;
	JTextField jtf;
	JButton jb;
	JPanel jp,jp2;
	JScrollPane jsp;
	String ownerId, friendId;
	JButton jb3;//jb3ΪȺ���еķ���

	public Chat(String Type,String ownerId, String friend) {
			this.ownerId = ownerId;
			this.friendId = friend;
			//��������
			if(Type=="1") {
			jta1 = new JTextArea();
			jtf = new JTextField(15);
			jb = new JButton("����");
			jb.addActionListener(this);
			jp = new JPanel();
			jp2 = new JPanel();
			jsp= new JScrollPane(jta1);
			jp.add(jtf);
			jp.add(jb);
			this.setDefaultCloseOperation(1);
			this.add(jsp,BorderLayout.CENTER);
			this.add(jp,BorderLayout.SOUTH);
			this.setTitle(ownerId + "���ں�" + friend + "����");
			this.setSize(500, 300);
			this.setVisible(true);
		}else if(Type=="2") {//��������
			this.setTitle(ownerId+"���ڲμ�Ⱥ��");
			jta2 = new JTextArea();
			jtf = new JTextField(15);
			jb3 = new JButton("����");
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
	
	
//�����ǹ���Chat�Ĳ���
/**************************************************************/
		private static HashMap hm = new HashMap<String,Chat>();
		
		//����
		public static void addChat(String OIdAndFId,Chat chat){
			hm.put(OIdAndFId, chat);	
		}
		//ȡ��
		public static Chat getChat(String OIdAndFId){
			return (Chat)hm.get(OIdAndFId);
			
		}
/**********************************************************************/
	// ��ʾ��Ϣ�ķ���
	public void showMessage(Message m,int Type) {
		if(Type==1) {
					String info = m.getSendTime()+"  "+m.getSender() + "��" + m.getGetter() + "˵:\n" + m.getCon()
					+ "\r\n";
					if (m.getCon() != null)
						this.jta1.append(info);
		}else if(Type==2) {
			String info =m.getSendTime()+"  "+m.getSender()+"˵:\n"+m.getCon()+"\n";
			if(m.getCon()!=null) {
				this.jta2.append(info);
			}
		}

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jb) {
			// ��ȡ��Ϣ
			Message m = new Message();
			m.setSender(this.ownerId);
			m.setGetter(this.friendId);
			m.setCon(jtf.getText());
			m.setSendTime(new Date().toString());
			m.setMsType(MessageType.message_comm_mes);
			//�Լ���ʾ
			showMessage(m,1);
			// ���͸�������

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
			// ��ȡ��Ϣ
			Message m = new Message();
			m.setSender(this.ownerId);
			m.setGetter("all");
			m.setCon(jtf.getText());
			m.setSendTime(new Date().toString());
			m.setMsType(MessageType.message_sendtoall);
			// ���͸�������
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
