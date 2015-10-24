package com.vhi.hsm.controller.api;

import com.vhi.hsm.model.User;

public interface UserApi {

	public boolean addUser(User user);
	
	public boolean changeUser(User user);
	
	public boolean deleteUser(); //parameter will either be ID/name or User object add/save/read/delete
}
