package com.vhi.hsm.controller.manager;

import com.vhi.hsm.model.Society;

public class SocietyManager {

	public static boolean registerSociety(String societyName, String societyAddress, String regNumber, String regDate) {
		Society society = Society.create();
		society.setAddress(societyAddress);
		society.setName(societyName);
		society.setRegistrationDate(regDate);
		society.setRegistrationNumber(regNumber);
		return Society.save(society, true);
	}

}
