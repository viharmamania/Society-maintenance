package com.vhi.hsm.controller.api;

import com.vhi.hsm.model.Society;

public interface SocietyApi {
	Society createSociety();

	boolean saveSociety(Society society);

	Society readSociety(int societyId);

	boolean deleteSociety(int societyId);
}
