package com.vhi.hsm.controller.api;

import com.vhi.hsm.model.User;

public interface UserApi {

	User addUser();

	boolean saveUser(User user);

	User readUser(int userId);

	boolean deleteUser(int userId);
}
