package main;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import object.Message;

/*
 * �ͻ��˺ͷ������˱���ͨѶ���߳�
 */

public class ClientToServerThread extends Thread {

	private Socket s;

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public ClientToServerThread(Socket s) {

		this.s = s;
	}

	public void run() {
		while (true) {
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(s.getInputStream());
				Message m = (Message) ois.readObject();
				//��ͨ����Ϣ��
				if(m.getMsType().equals(MessageType.message_comm_mes)){
				FriendList.updateFriend_message(m);
				// �Ѵӷ������õ�����Ϣ��ʾ������ʾ���������
				Chat chat = Chat.getChat(m.getGetter() + " "+ m.getSender());
				chat.showMessage(m,1);
				}
				//�����б���Ϣ��
				if(m.getMsType().equals(MessageType.message_ret_onLineFriend)){
					String getter = m.getGetter();
					//�޸���Ӧ�ĺ���ͷ��
					FriendList friendlist = FriendList.getFriendList(getter);
					//���º���
					if(friendlist!= null)
					friendlist.updateFriend(m);
				}
				//������Ⱥ����Ϣ
				if(m.getMsType().equals(MessageType.message_sendtoall)) {
						Chat chat = Chat.getChat(m.getGetter());
						System.out.println("�õ�chat��ownid="+chat.ownerId);
						chat.showMessage(m,2);
					
				}
				//������ɾ���ͻ����̵߳Ĳ���
				if(m.getMsType().equals(MessageType.message_deleted)) {
					String getter = m.getGetter();
					//�޸���Ӧ�ĺ���ͷ��
					FriendList friendlist = FriendList.getFriendList(getter);
					//���º���
					if(friendlist!= null)
					friendlist.updateFriend_delete(m);
				}
				//�����Ƿ����ǿ�����ߵĲ���
				if(m.getMsType().equals(MessageType.message_delete_client)) {
					FriendList friendlist = FriendList.getFriendList(m.getSender());
					friendlist.being_deleted();
					ClientToServerThread ST=getClientToServerThread(m.getSender());
					deleteClientToServerThread(m.getSender());
					ST.stop();
				}

			} catch (Exception e) {
			  e.printStackTrace();
			}

		}
	}
	
	/*****************************************/
	//������
	private static HashMap<String, ClientToServerThread> hm = new HashMap<String,ClientToServerThread>();
	
	//�Ѵ����õ�ClientToServerThread���뵽hm��
	
	public static void addClientToServerThread(String Id,ClientToServerThread ccst){
		hm.put(Id, ccst);
	}
	public static void deleteClientToServerThread(String Id)
	{
		hm.remove(Id);
	}
	public static ClientToServerThread getClientToServerThread(String Id){
		return (ClientToServerThread)hm.get(Id);
		}
	
	
	/*******************************************/

}
