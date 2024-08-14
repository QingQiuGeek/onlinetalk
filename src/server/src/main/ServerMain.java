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
 * ����������
 */
public class ServerMain extends JFrame implements ActionListener,MouseListener {

	JPanel jp1,jp2;
	JButton jb1, jb2, jb3;
	static JTextArea jta;
	JTextArea jta2;
	JScrollPane jsp,jsp2;//jsp2Ϊ�û��б� jsp3Ϊ�ı���
	JTabbedPane jtp;
	static JLabel []jbls;
	

	public static void main(String[] args) {
		 new ServerMain();
	}

	public ServerMain() {
		jp1 = new JPanel();
		jta = new JTextArea();
		jb1 = new JButton("����������");
		jb2 = new JButton("�رշ�����");
		jb3 = new JButton("��ʾ�û��б�");
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
		jtp.add("��������־",jsp);
		jtp.add("�����û�",jsp2);
		this.add(jtp,"Center");
		this.setDefaultCloseOperation(3);
		this.setSize(400, 400);
		this.setTitle("��������");
		this.setVisible(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		Server server = new Server();
		if (e.getSource() == jb1) {
			jta.append("�����ѿ���\n");

			server.start();
		}
		if (e.getSource() == jb2) {
			server.stop();
			jta.append("�������ѹر�\n");
		}
		if (e.getSource() == jb3) {
			printUser();
			jta.append("�û���ʾ���\n");
		}

	}
//printuser������ʾ���������û�
	public void printUser() {
		String[] s = ServerToClientThread.getAllOnLineUserId().split(" ");
		jta.append("Ŀǰ�����û��У�\n");
		for (int i = 0; i < s.length; i++) {
			jta.append(s[i]+"\n");
		}
		jta.append("����"+s.length+"���û�\n");
	}
	
	public static void updateFriend_add(String iam) {
		jbls[Integer.parseInt(iam)-1].setEnabled(true);
	}
	
	public static void updateFriend_delete(String iam) {
		jbls[Integer.parseInt(iam)-1].setEnabled(false);
	}
	//�˷���������ʾ��������־
	public static void print(String m) {
		jta.append(m);
	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//��Ӧ�û�˫���¼��õ����ѱ��
		if(e.getClickCount()==2){}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		String client_User=((JLabel) e.getSource()).getText();
		jl.setForeground(Color.black);
		int result=JOptionPane.showConfirmDialog(this,"�Ƿ���������","ѡ���û�"+
		((JLabel)e.getSource()).getText(),JOptionPane.YES_NO_CANCEL_OPTION);
		if(result==0) {
			//������ǿ���û����߹���
			//������ɾ���ͻ����߳�
			jta.append("����ǿ��"+client_User+"����\n");
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
			//������ɾ��������߳�
			ServerToClientThread.notifyOther(client_User,2);
			ServerToClientThread ST=ServerToClientThread.getClientThread(client_User);
			ServerToClientThread.deleteClientThtead(client_User);
			
			ST.stop();
		}
		//JOptionPane.showMessageDialog(this,"ѡ���û�"+((JLabel)e.getSource()).getText()+"\n");
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

