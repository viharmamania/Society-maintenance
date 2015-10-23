package com.vhi.server.api;

import com.vhi.server.model.Society;

public interface SocietyApi {
	public boolean createSociety(Society society);

	public boolean editSociety(Society society);
	
	public boolean deleteSociety(); 	//parameter will either be id or whole object
}
