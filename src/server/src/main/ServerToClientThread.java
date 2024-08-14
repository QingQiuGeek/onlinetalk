package main;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import object.Message;

/**
 * ���Ƿ�������һ���ͻ������ӵ�ͨѶ�߳�
 */
public class ServerToClientThread extends Thread{
	Socket s;
	public ServerToClientThread(Socket s){
		this.s = s;
	}
	//���߳�֪ͨ���������û�
	public static void notifyOther(String iam,int Type){

		//�õ��������ߵ��˵��߳�
		Iterator it = hm.keySet().iterator();
		if(Type==1) {	
			//ʹ�������˸��û�����
			ServerMain.updateFriend_add(iam);
			while(it.hasNext()){
			Message m = new Message();
			m.setCon(iam);
			m.setMsType(MessageType.message_ret_onLineFriend);
			//ȡ�������˵�id
			String OnLineUserId = it.next().toString();
			try {
			ObjectOutputStream oos = new ObjectOutputStream(getClientThread(OnLineUserId).s.getOutputStream());
			m.setGetter(OnLineUserId);
			oos.writeObject(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}else if(Type==2) {
			//ʹ�������˸��û�����
			ServerMain.print(iam+"������\n");
			ServerMain.updateFriend_delete(iam);
			while(it.hasNext()) {
				Message m=new Message();
				m.setSender(iam);
				m.setCon(iam);
				m.setMsType(MessageType.message_deleted);
				String OnLineUserId = it.next().toString();
				try {
					ObjectOutputStream oos = new ObjectOutputStream(getClientThread(OnLineUserId).s.getOutputStream());
					m.setGetter(OnLineUserId);
					oos.writeObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	}
	public void run(){
		//���߳̽��տͻ�����Ϣ
		while(true){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Message m = (Message)ois.readObject();
			//�Դӿͻ���ȡ�õ���Ϣ�����ж�
			if(m.getMsType().equals(MessageType.message_comm_mes)){
			//ת��
			//ȡ�ý����˵�ͨѶ�߳�
			ServerToClientThread sc = getClientThread(m.getGetter());
			
			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
			oos.writeObject(m);
			}else if(m.getMsType().equals(MessageType.message_get_onLineFriend)){
				//���غ����б�
				String res = getAllOnLineUserId();
				Message m2 = new Message();
				m2.setMsType(MessageType.message_ret_onLineFriend);
				m2.setCon(res);
				m2.setGetter(m.getSender());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(m2);
			}else if(m.getMsType().equals(MessageType.message_sendtoall)) {
					Iterator it = hm.keySet().iterator();
					while(it.hasNext()) {
						String OnLineUserId = it.next().toString();
						System.out.println("�õ����ߺ���"+OnLineUserId);
						m.setGetter(OnLineUserId);
						ObjectOutputStream oos = new ObjectOutputStream(getClientThread(OnLineUserId).s.getOutputStream());
						oos.writeObject(m);
					}
			}else if(m.getMsType().equals(MessageType.message_delete_client)) {	
				ServerToClientThread ST=getClientThread(m.getSender());
				deleteClientThtead(m.getSender());
				notifyOther(m.getSender(),2);
				ST.stop();
				
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
/*****************************************************/
//�����ǹ���ͻ����̵߳Ĳ���
	public static HashMap hm = new HashMap<String, ServerToClientThread>();

	// ��HM����ӿͻ���ͨѶ�߳�
	public static void addClientThread(String uid, ServerToClientThread ct) {
		hm.put(uid, ct);//��ϣ����ӷ���
	}
	
	public static ServerToClientThread getClientThread(String uid) {
		return (ServerToClientThread) hm.get(uid);//

	}
	public static void deleteClientThtead(String uid) {
		System.out.println("ɾ��Ԫ��"+uid);
		hm.remove(uid);
		Iterator it = hm.keySet().iterator();//���� it.next() �᷵�ص���������һ��Ԫ�أ����Ҹ��µ�������״̬��
		String res ="";
		while(it.hasNext()){                 //���� it.hasNext() ���ڼ�⼯�����Ƿ���Ԫ�ء�
			System.out.println(it.next());
		}
	}
	public static String getAllOnLineUserId(){
		//���������
		Iterator it = hm.keySet().iterator();//���� it.next() �᷵�ص���������һ��Ԫ�أ����Ҹ��µ�������״̬��
		String res ="";
		while(it.hasNext()){                 //���� it.hasNext() ���ڼ�⼯�����Ƿ���Ԫ�ء�
			res+= it.next().toString()+" ";
		}
		return res;
	}
	
/****************************************************/
}
	                

