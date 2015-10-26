package com.vhi.hsm.controller.api;

import com.vhi.hsm.model.User;

public interface UserApi {

	User createUser();

	boolean saveUser(User user);

	User readUser(String userName);

	boolean deleteUser(String userName);
}
