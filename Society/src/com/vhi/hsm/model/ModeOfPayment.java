package com.vhi.hsm.model;

import java.util.Arrays;

public enum ModeOfPayment {
	SELECT("select"),CASH("cash"), CHEQUE("cheque");

	private final String mode;

	private ModeOfPayment(String str) {
		this.mode = str;
	}

	String getMode() {
		return this.mode;
	}
	
	public static String[] getNames() {
		return Arrays.stream(ModeOfPayment.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
	}

}
