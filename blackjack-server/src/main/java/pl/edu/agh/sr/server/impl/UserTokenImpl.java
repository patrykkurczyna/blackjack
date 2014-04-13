package pl.edu.agh.sr.server.impl;

import pl.edu.agh.sr.api.UserToken;

public class UserTokenImpl implements UserToken{

	private static final long serialVersionUID = 1L;
	String userName;
	Integer id;
	
	public UserTokenImpl(String userName, Integer id) {
		this.id = id;
		this. userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}

	public Integer getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserTokenImpl other = (UserTokenImpl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
