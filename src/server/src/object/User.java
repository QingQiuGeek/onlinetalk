package object;
import java.io.Serializable;

/**
 * �û���Ϣ��
 *
 */
public class User implements Serializable{

	private String userId;
	private String password;
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
