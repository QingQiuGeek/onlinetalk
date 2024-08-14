package object;
import java.io.Serializable;


public class Message implements Serializable{
	private String msType,sender,getter,con,sendTime;

	public String getMsType() {
		return msType;
	}
	public void setMsType(String msType) {
		this.msType = msType;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getGetter() {
		return getter;
	}
	public void setGetter(String getter) {
		this.getter = getter;
	}
	public String getCon() {
		return con;
	}
	public void setCon(String con) {
		this.con = con;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
}
