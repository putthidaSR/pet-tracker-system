package tcss559.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="users") 
public class UsersXML {
	
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}
	
	@XmlElement(name="user")  
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	
}
