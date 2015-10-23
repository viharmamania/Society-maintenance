package com.vhi.server.api;

import com.vhi.server.model.User;

public interface UserApi {

	public boolean addUser(User user);
	
	public boolean changeUser(User user);
	
	public boolean deleteUser(); //parameter will either be ID/name or User object
}
