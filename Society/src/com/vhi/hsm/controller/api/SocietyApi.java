package com.vhi.hsm.controller.api;

import com.vhi.hsm.model.Society;

public interface SocietyApi {
	public boolean createSociety(Society society);

	public boolean editSociety(Society society);

	public boolean deleteSociety(); // parameter will either be id or whole
									// object
}
