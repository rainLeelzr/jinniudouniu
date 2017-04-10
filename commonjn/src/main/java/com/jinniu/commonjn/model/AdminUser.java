package com.jinniu.commonjn.model;

public class AdminUser implements Entity {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	protected Integer id;
	
	/**  */
	protected java.util.Date addTime;
	
	/**  */
	protected String email;
	
	/**  */
	protected String lastIp;
	
	/**  */
	protected java.util.Date lastLogin;
	
	/**  */
	protected String password;
	
	/**  */
	protected String userName;
	
 	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public java.util.Date getAddTime() {
		return addTime;
	}
	
	public void setAddTime(java.util.Date addTime) {
		this.addTime = addTime;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getLastIp() {
		return lastIp;
	}
	
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	
	public java.util.Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(java.util.Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
 	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id = ").append(id).append(", ");
		builder.append("addTime = ").append(addTime).append(", ");
		builder.append("email = ").append(email).append(", ");
		builder.append("lastIp = ").append(lastIp).append(", ");
		builder.append("lastLogin = ").append(lastLogin).append(", ");
		builder.append("password = ").append(password).append(", ");
		builder.append("userName = ").append(userName);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AdminUser other = (AdminUser) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}