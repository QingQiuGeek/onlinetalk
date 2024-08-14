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
 * �ͻ��˵�¼����
 * 
 */

public class Login extends JFrame implements ActionListener {

	// ���山�����
	JLabel jbl1;
	// �����в����
	// �в���2��JPanel����һ��ѡ�����
	JTabbedPane jtp;
	JPanel jp2, jp3;
	JLabel jp2_jbl1, jp2_jbl2,  jp3_jbl1, jp3_jbl2;
	JTextField jp2_jtf, jp3_jtf;
	JPasswordField jp2_jpf, jp3_jpf;
	// �����ϲ����
	JPanel jp1;
	JButton jp1_jb1, jp1_jb2;

	public static void main(String[] args) {
		new Login();
	}

	public Login() {
		// ����
		jbl1 = new JLabel(new ImageIcon("client/src/res/tx.png"));// ���Է�ͼƬ
		// �ϲ�
		jp1 = new JPanel();
		jp1_jb1 = new JButton("��¼");
		jp1_jb1.addActionListener(this);
		jp1_jb2 = new JButton("ȡ��");
		jp1_jb2.addActionListener(this);
		// ��������ť�ŵ�JP1��
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);
		// �в�	
		jp2 = new JPanel(new GridLayout(3, 3));//���ú�һ��3X3�Ĳ���
		jp2_jbl1 = new JLabel("�ǳ�", JLabel.CENTER);//��һ������Ϊ��ʾ�����֣��ڶ���Ϊ����
		jp2_jbl2 = new JLabel("����", JLabel.CENTER);
		jp2_jtf = new JTextField();
		jp2_jpf = new JPasswordField();

		jp3 = new JPanel(new GridLayout(3, 3));

		jp3_jbl1 = new JLabel("Name", JLabel.CENTER);
		jp3_jbl2 = new JLabel("QQ����", JLabel.CENTER);
		jp3_jtf = new JTextField();
		jp3_jpf = new JPasswordField();
		// �ѿؼ�����˳����뵽jp2��
		jp2.add(jp2_jbl1);
		jp2.add(jp2_jtf);
		jp2.add(jp2_jbl2);
		jp2.add(jp2_jpf);
		// �ѿؼ�����˳����뵽jp3��
		jp3.add(jp3_jbl1);
		jp3.add(jp3_jpf);
		jtp = new JTabbedPane();
		jtp.add("�˺ŵ�¼", jp2);
		jtp.add("ʹ��ָ��", jp3);

		// ��JP1�ŵ��ϲ�
		this.add(jp1, "South");
		this.add(jtp, "Center");
		this.add(jbl1, "North");
		this.setSize(350, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	//�����Ǽ���¼����
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
					// ��������֤�û���¼�ĵط�
					if (ms.getMsType().equals("1")) {
						// ����һ����QQ�ͷ��������ӵ�ͨѶ�߳�
						ClientToServerThread ccst = new ClientToServerThread(s);
						// ������ͨ���߳�
						ccst.start();
						//�����̼߳���ClientToServerThread��
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
		    User u = new User();//�½�һ���û����洢�û�ID�����룬�Ա���
			u.setUserId(jp2_jtf.getText());//trim() ��������ɾ���ַ�����ͷβ�հ׷���
			u.setPassword(new String(jp2_jpf.getPassword()));
			//����������½�һ��FriendList��Ľ��棬���ҹرոý���
			if(Check(u))
			{
				//����һ��Ҫ�󷵻����ߺ��ѵ������
				try {
					//�Ѵ��������б���ǰ
					FriendList friendList = new FriendList(u.getUserId());
					FriendList.addFriendList(u.getUserId(), friendList);
					//����һ��Ҫ�󷵻����ߺ��ѵ������
					ObjectOutputStream oos = new ObjectOutputStream(ClientToServerThread.getClientToServerThread(u.getUserId()).getS().getOutputStream());
				Message m = new Message();
				m.setMsType(MessageType.message_get_onLineFriend);
				//ָ����Ҫ�������QQ�ŵĺ����б�
				m.setSender(u.getUserId());
				oos.writeObject(m);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.dispose();
			}else{
				//��֤����������ʾ
				JOptionPane.showMessageDialog(this, "�û��������������");
			}
		}else if(e.getSource()==jp1_jb2) {
			this.dispose();
		}
		
	}
}
