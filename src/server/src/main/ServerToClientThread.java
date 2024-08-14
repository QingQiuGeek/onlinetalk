package main;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import object.Message;

/**
 * 这是服务器和一个客户端连接的通讯线程
 */
public class ServerToClientThread extends Thread{
	Socket s;
	public ServerToClientThread(Socket s){
		this.s = s;
	}
	//该线程通知其他在线用户
	public static void notifyOther(String iam,int Type){

		//得到所有在线的人的线程
		Iterator it = hm.keySet().iterator();
		if(Type==1) {	
			//使服务器端该用户上线
			ServerMain.updateFriend_add(iam);
			while(it.hasNext()){
			Message m = new Message();
			m.setCon(iam);
			m.setMsType(MessageType.message_ret_onLineFriend);
			//取出在线人的id
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
			//使服务器端该用户下线
			ServerMain.print(iam+"下线了\n");
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
		//该线程接收客户端信息
		while(true){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Message m = (Message)ois.readObject();
			//对从客户端取得的消息类型判断
			if(m.getMsType().equals(MessageType.message_comm_mes)){
			//转发
			//取得接收人的通讯线程
			ServerToClientThread sc = getClientThread(m.getGetter());
			
			ObjectOutputStream oos = new ObjectOutputStream(sc.s.getOutputStream());
			oos.writeObject(m);
			}else if(m.getMsType().equals(MessageType.message_get_onLineFriend)){
				//返回好友列表
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
						System.out.println("得到在线好友"+OnLineUserId);
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
//这里是管理客户端线程的部分
	public static HashMap hm = new HashMap<String, ServerToClientThread>();

	// 向HM中添加客户端通讯线程
	public static void addClientThread(String uid, ServerToClientThread ct) {
		hm.put(uid, ct);//哈希表添加方法
	}
	
	public static ServerToClientThread getClientThread(String uid) {
		return (ServerToClientThread) hm.get(uid);//

	}
	public static void deleteClientThtead(String uid) {
		System.out.println("删除元素"+uid);
		hm.remove(uid);
		Iterator it = hm.keySet().iterator();//调用 it.next() 会返回迭代器的下一个元素，并且更新迭代器的状态。
		String res ="";
		while(it.hasNext()){                 //调用 it.hasNext() 用于检测集合中是否还有元素。
			System.out.println(it.next());
		}
	}
	public static String getAllOnLineUserId(){
		//迭代器完成
		Iterator it = hm.keySet().iterator();//调用 it.next() 会返回迭代器的下一个元素，并且更新迭代器的状态。
		String res ="";
		while(it.hasNext()){                 //调用 it.hasNext() 用于检测集合中是否还有元素。
			res+= it.next().toString()+" ";
		}
		return res;
	}
	
/****************************************************/
}
	                

