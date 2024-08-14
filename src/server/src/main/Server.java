package main;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import object.Message;
import object.User;
/**
 * 服务器部分
 */
public class Server extends Thread{

	public void run() {
		try {
			// 在10000监听
			System.out.println("服务器在监听");
			ServerSocket ss = new ServerSocket(1000);
			while (true) {
				// 阻塞 等待连接
				Socket s = ss.accept();
				// 接收发来的信息
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				User u = (User) ois.readObject();//接受来自客户端的user对象
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				Message m = new Message();
				if (u.getPassword().equals("123")) {
					//显示在服务器日志
					ServerMain.print(u.getUserId()+"上线了\n");
					m.setMsType("1");
					oos.writeObject(m);//将修改之后的user重新写回流中
					// 单开一个与客户端连接的线程
					ServerToClientThread stct = new ServerToClientThread(s);
					// 向HM中传入客户端
					ServerToClientThread.addClientThread(u.getUserId(), stct);
					stct.start();
					
					//通知其他在线用户
					stct.notifyOther(u.getUserId(),1);
				} else {
					m.setMsType("2");
					oos.writeObject(m);

					// 关闭连接
					s.close();
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("e is "+ e);
		}
	}

}
