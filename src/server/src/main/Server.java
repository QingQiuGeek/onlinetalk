package main;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import object.Message;
import object.User;
/**
 * ����������
 */
public class Server extends Thread{

	public void run() {
		try {
			// ��10000����
			System.out.println("�������ڼ���");
			ServerSocket ss = new ServerSocket(1000);
			while (true) {
				// ���� �ȴ�����
				Socket s = ss.accept();
				// ���շ�������Ϣ
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				User u = (User) ois.readObject();//�������Կͻ��˵�user����
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				Message m = new Message();
				if (u.getPassword().equals("123")) {
					//��ʾ�ڷ�������־
					ServerMain.print(u.getUserId()+"������\n");
					m.setMsType("1");
					oos.writeObject(m);//���޸�֮���user����д������
					// ����һ����ͻ������ӵ��߳�
					ServerToClientThread stct = new ServerToClientThread(s);
					// ��HM�д���ͻ���
					ServerToClientThread.addClientThread(u.getUserId(), stct);
					stct.start();
					
					//֪ͨ���������û�
					stct.notifyOther(u.getUserId(),1);
				} else {
					m.setMsType("2");
					oos.writeObject(m);

					// �ر�����
					s.close();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("e is "+ e);
		}
	}

}
