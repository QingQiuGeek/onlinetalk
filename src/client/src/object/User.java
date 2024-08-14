package object;
import java.io.Serializable;

/**
 * 用户信息类
 */
public class User implements Serializable{

	private String userId;//用户ID
	private String password;//密码
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
