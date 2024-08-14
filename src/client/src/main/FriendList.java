package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import object.Message;

/**
 * 好友列表
 *
 */
public class FriendList extends JFrame implements MouseListener,ActionListener{

	JPanel jp1,jp2,jp3,jp4;//jp3为群聊面板
	JTextArea jta;
	JButton jphy_jb1,jb2,jb3,jb4;//jb2为发送 ,jb3，为群聊天室，jb4为菜单
	JScrollPane jsp1,jsp2;
	static String ownerId;
	static JLabel []jbls;
	
	public FriendList(String ownerId) {
		this.ownerId = ownerId;
		jp1 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new GridLayout(50,1));//50行，1列。4*4
		jp2.setSize(50,60);
		jp3=new JPanel(new GridLayout(3,1));
		jta=new JTextArea();
		
		jphy_jb1 = new JButton("我的好友");
		jb3=new JButton("群聊天室");
		jb4=new JButton("菜单");
		jb2=new JButton("退出");
		
		jb2.setSize(1,1);
		jb2.addActionListener(this);
		
		// 初始化50个好友
		jbls = new JLabel[50];
		for(int i = 0;i<jbls.length;i++){
			jbls[i]= new JLabel(""+(i+1), new ImageIcon("src/tx.PNG"),JLabel.LEFT);
			jbls[i].setEnabled(false);
			if(jbls[i].getText().equals(ownerId))//将登陆者设置为可见
			{
				jbls[i].setEnabled(true);
			}
			jbls[i].addMouseListener(this);
			jp2.add(jbls[i]);
			
		}
		jb3.addActionListener((ActionListener) this);

		jsp1 = new JScrollPane(jp2);//将好友面板添加到滚动条
		
		jp1.add(jphy_jb1,"North");
		jp1.add(jsp1);
		jp1.add(jp3,"South");
		jp3.add(jb3);
		jp3.add(jb4);
		jp3.add(jb2);

		
		this.setTitle(ownerId);
		this.setDefaultCloseOperation(3);
		this.add(jp1,"Center");

		this.setSize(200,600);
		this.setVisible(true);
	}
	//更新在线好友上线
	public void updateFriend(Message m){
		String onLineFriend[] = m.getCon().split(" ");
		for(int i = 0;i<onLineFriend.length;i++)
		{
			jbls[Integer.parseInt(onLineFriend[i])-1].setEnabled(true);
		}
	}
	//此方法用于更新好友下线
	public void updateFriend_delete(Message m) {
		String onLineFriend = m.getSender();
		jbls[Integer.parseInt(onLineFriend)-1].setEnabled(false);
	}
	//此方法用于更新好友消息状态
	public static void updateFriend_message(Message m) {
		String onLineFriend = m.getSender();
		jbls[Integer.parseInt(onLineFriend)-1].setForeground(Color.RED);
	}
	public void being_deleted() {
		int result=JOptionPane.showConfirmDialog(this,"您已被踢下线", "Notify",JOptionPane.YES_NO_CANCEL_OPTION );
		if(result==0) {
			this.dispose();
		}
	}

/*******************************************************************/
//这里是管理用户列表部分
	
	private static HashMap hm = new HashMap<String ,FriendList>();
	public static void addFriendList(String qqId,FriendList friendList){
		hm.put(qqId,friendList);
		
	}
	public static FriendList getFriendList(String qqId){
		return ((FriendList)hm.get(qqId));
	}
	
/**********************************************************************/
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//响应用户双击事件得到好友编号
		if(e.getClickCount()==2)
		{
			//得到该好友编号
			String friendNum = ((JLabel)e.getSource()).getText();
			System.out.println("你点击的朋友编号是"+friendNum);
			Chat chat = new Chat("1",this.ownerId,friendNum);
			//把聊天界面加入到管理类中
			Chat.addChat(this.ownerId+" "+friendNum, chat);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb3) {
			Chat chat2 = new Chat("2",this.ownerId,null);
			Chat.addChat(this.ownerId, chat2);
	}else if(e.getSource()==jb2) {
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(ClientToServerThread.getClientToServerThread(ownerId).getS().getOutputStream());
			Message m = new Message();
			m.setMsType(MessageType.message_delete_client);
			//指明我要的是这个QQ号的好友列表
			m.setSender(ownerId);
			oos.writeObject(m);
			//m.setMsType(MessageType.message_get_onLineFriend);
			//oos.writeObject(m);
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ClientToServerThread ST=ClientToServerThread.getClientToServerThread(ownerId);
		ClientToServerThread.deleteClientToServerThread(ownerId);
		ST.stop();
		this.dispose();
		
	}
		}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		jl.setForeground(Color.black);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//JLabel jl = (JLabel)e.getSource();
		//jl.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
