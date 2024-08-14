package main;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

import object.Message;

/*
 * 客户端和服务器端保持通讯的线程
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
				//普通的消息包
				if(m.getMsType().equals(MessageType.message_comm_mes)){
				FriendList.updateFriend_message(m);
				// 把从服务器得到的消息显示到该显示的聊天界面
				Chat chat = Chat.getChat(m.getGetter() + " "+ m.getSender());
				chat.showMessage(m,1);
				}
				//好友列表消息包
				if(m.getMsType().equals(MessageType.message_ret_onLineFriend)){
					String getter = m.getGetter();
					//修改响应的好友头像
					FriendList friendlist = FriendList.getFriendList(getter);
					//更新好友
					if(friendlist!= null)
					friendlist.updateFriend(m);
				}
				//这里是群发消息
				if(m.getMsType().equals(MessageType.message_sendtoall)) {
						Chat chat = Chat.getChat(m.getGetter());
						System.out.println("得到chat类ownid="+chat.ownerId);
						chat.showMessage(m,2);
					
				}
				//这里是删除客户端线程的部分
				if(m.getMsType().equals(MessageType.message_deleted)) {
					String getter = m.getGetter();
					//修改响应的好友头像
					FriendList friendlist = FriendList.getFriendList(getter);
					//更新好友
					if(friendlist!= null)
					friendlist.updateFriend_delete(m);
				}
				//这里是服务端强制下线的部分
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
	//这里是
	private static HashMap<String, ClientToServerThread> hm = new HashMap<String,ClientToServerThread>();
	
	//把创建好的ClientToServerThread放入到hm中
	
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
